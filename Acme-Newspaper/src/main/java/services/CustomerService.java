
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

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Customer;
import domain.MailMessage;
import domain.Subscription;
import domain.Volume;
import forms.ActorForm;
import forms.EditActorForm;

@Service
@Transactional
public class CustomerService {

	// Managed Repository
	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting services
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private Validator		validator;

	// Constructors

	public CustomerService() {
		super();
	}

	// Simple CRUD methods
	public Customer create() {
		Customer result;
		result = new Customer();
		
		result.setSubscriptions(new ArrayList<Subscription>());
		result.setReceivedMessages(new ArrayList<MailMessage>());
		result.setSentMessages(new ArrayList<MailMessage>());
		result.setFolders(this.folderService.createSystemFolders());
		result.setVolumesSubscribed(new ArrayList<Volume>());
		
		return result;
	}

	public Customer save(final Customer customer) {
		Customer saved;
		Assert.notNull(customer);

		if (customer.getId() == 0) {
			final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			customer.getUserAccount().setPassword(passwordEncoder.encodePassword(customer.getUserAccount().getPassword(), null));
		}
		

		saved = this.customerRepository.save(customer);
		
		//TEST ASSERT - Testing if the customer is in the system after saving him/her
		Assert.isTrue(this.customerRepository.findAll().contains(saved));
		//TEST ASSERT =========================================
		return saved;
	}

	public Customer findOne(final int customerId) {
		Customer result;
		result = this.customerRepository.findOne(customerId);
		return result;
	}

	public Collection<Customer> findAll() {
		Collection<Customer> result;
		result = this.customerRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	//Other business methods
	public Customer findByPrincipal() {
		Customer result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;

	}

	public Customer findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Customer result;
		result = this.customerRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public Customer reconstruct(final ActorForm actorForm, final BindingResult binding) {
		final Customer customer = this.create();
		customer.setName(actorForm.getName());
		customer.setSurname(actorForm.getSurname());
		customer.setEmail(actorForm.getEmail());
		customer.setPostalAddress(actorForm.getAddress());
		customer.setId(actorForm.getId());
		customer.setVersion(actorForm.getVersion());
		customer.setPhone(actorForm.getPhone());
		customer.setUserAccount(actorForm.getUserAccount());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority("CUSTOMER");
		authorities.add(auth);
		customer.getUserAccount().setAuthorities(authorities);

		this.validator.validate(actorForm, binding);
		if (!(actorForm.getConfirmPassword().equals((actorForm.getUserAccount().getPassword()))) || actorForm.getConfirmPassword() == null)
			binding.rejectValue("confirmPassword", "user.passwordMiss");
		if ((actorForm.getCheck() == false))
			binding.rejectValue("check", "user.uncheck");
		return customer;
	}

	public EditActorForm construct(Customer customer, EditActorForm editActorForm) {
				
		editActorForm.setId(customer.getId());
		editActorForm.setVersion(customer.getVersion());
		editActorForm.setName(customer.getName());
		editActorForm.setSurname(customer.getSurname());
		editActorForm.setEmail(customer.getEmail());
		editActorForm.setPhone(customer.getPhone());
		editActorForm.setAddress(customer.getPostalAddress());


		
		return editActorForm;
	}

	public Customer reconstruct(EditActorForm editActorForm, BindingResult binding) {
		Customer customer;
		
		customer = this.findByPrincipal();
		
		customer.setName(editActorForm.getName());
		customer.setSurname(editActorForm.getSurname());
		customer.setEmail(editActorForm.getEmail());
		customer.setId(editActorForm.getId());
		customer.setPostalAddress(editActorForm.getAddress());
		customer.setVersion(editActorForm.getVersion());
		customer.setPhone(editActorForm.getPhone());
	
		
		this.validator.validate(editActorForm, binding);

		return customer;
	}
}
