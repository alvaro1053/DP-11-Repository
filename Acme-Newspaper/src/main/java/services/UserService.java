
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Article;
import domain.Chirp;
import domain.MailMessage;
import domain.Newspaper;
import domain.User;
import forms.ActorForm;

@Service
@Transactional
public class UserService {

	// Managed Repository
	@Autowired
	private UserRepository	userRepository;
	
	@Autowired
	private FolderService folderService;

	@Autowired
	private Validator		validator;


	// Supporting services

	// Constructors

	public UserService() {
		super();
	}

	// Simple CRUD methods
	public User create() {
		User result;

		result = new User();
		result.setArticles(new ArrayList<Article>());
		result.setChirps(new ArrayList<Chirp>());
		result.setFollowers(new ArrayList<User>());
		result.setFollows(new ArrayList<User>());
		result.setNewspapers(new ArrayList<Newspaper>());
		result.setReceivedMessages(new ArrayList<MailMessage>());
		result.setSentMessages(new ArrayList<MailMessage>());
		result.setFolders(this.folderService.createSystemFolders());
		
		return result;
	}

	public User save(final User User) {
		User saved;
		Assert.notNull(User);

		if (User.getId() == 0) {
			final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			User.getUserAccount().setPassword(passwordEncoder.encodePassword(User.getUserAccount().getPassword(), null));
		}

		saved = this.userRepository.save(User);

		//TEST ASSERT - Testing if the user is in the system after saving him/her
		Assert.isTrue(this.userRepository.findAll().contains(saved));
		//TEST ASSERT =========================================
		return saved;
	}

	public User findOne(final int UserId) {
		User result;
		result = this.userRepository.findOne(UserId);
		return result;
	}

	public Collection<User> findAll() {
		Collection<User> result;
		result = this.userRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	//Other business methods
	public User findByPrincipal() {
		User result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;

	}

	public User findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		User result;
		result = this.userRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public User reconstruct(final ActorForm actorForm, final BindingResult binding) {
		final User user = this.create();
		user.setName(actorForm.getName());
		user.setSurname(actorForm.getSurname());
		user.setEmail(actorForm.getEmail());
		user.setId(actorForm.getId());
		user.setPostalAddress(actorForm.getAddress());
		user.setVersion(actorForm.getVersion());
		user.setPhone(actorForm.getPhone());
		user.setUserAccount(actorForm.getUserAccount());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority("USER");
		authorities.add(auth);
		user.getUserAccount().setAuthorities(authorities);

		this.validator.validate(actorForm, binding);
		if (!(actorForm.getConfirmPassword().equals((actorForm.getUserAccount().getPassword()))) || actorForm.getConfirmPassword() == null)
			binding.rejectValue("confirmPassword", "user.passwordMiss");
		if ((actorForm.getCheck() == false))
			binding.rejectValue("check", "user.uncheck");
		return user;
	}

	public void flush() {
		this.userRepository.flush();
	}

	public void follow(final User userToBeFollowed) {
		User principal;
		Collection<User> usersToBeAdded;
		principal = this.findByPrincipal();

		//TEST ASSERT - Testing if the user principal already follows the user to be followed
		Assert.isTrue(!principal.getFollows().contains(userToBeFollowed));
		//TEST ASSERT

		usersToBeAdded = principal.getFollows();
		usersToBeAdded.add(userToBeFollowed);
		principal.setFollows(usersToBeAdded);

		usersToBeAdded = userToBeFollowed.getFollowers();
		usersToBeAdded.add(principal);
		userToBeFollowed.setFollowers(usersToBeAdded);

	}

	public void unfollow(final User userToBeUnfollowed) {
		User principal;
		Collection<User> usersToBeUnfollowed;
		principal = this.findByPrincipal();

		//TEST ASSERT - Testing if the user principal follows the user to be unfollowed
		Assert.isTrue(principal.getFollows().contains(userToBeUnfollowed));
		//TEST ASSERT

		usersToBeUnfollowed = principal.getFollows();

		if (usersToBeUnfollowed.contains(userToBeUnfollowed)) {
			usersToBeUnfollowed.remove(userToBeUnfollowed);
			principal.setFollows(usersToBeUnfollowed);
		}

		usersToBeUnfollowed = userToBeUnfollowed.getFollowers();

		if (usersToBeUnfollowed.contains(principal)) {
			usersToBeUnfollowed.remove(principal);
			userToBeUnfollowed.setFollowers(usersToBeUnfollowed);
		}

	}
}
