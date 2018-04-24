
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed Repository
	@Autowired
	private ActorRepository	actorRepository;

	// Supporting services
	@Autowired
	private UserService		userService;

	@Autowired
	private AdminService	adminService;


	// Constructors

	public ActorService() {
		super();
	}

	// Simple methods

	//findOne realizado de esta manera debido a que en ActorServiceTest daba null el findOne 
	//para las clases extendidas de Actor 
	public Actor findOne(final int actorId) {
		Actor result;
		result = this.actorRepository.findOne(actorId);
		if (result == null) {
			result = this.userService.findOne(actorId);
			if (result == null)
				result = this.adminService.findOne(actorId);
		}
		return result;
	}
	public Collection<Actor> findAll() {
		Collection<Actor> result;
		result = this.actorRepository.findAll();
		Assert.notNull(result);
		return result;

	}

	public Collection<Actor> findAllMinusPrincipal() {
		Collection<Actor> result;
		Actor principal;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		principal = this.findByPrincipal();
		Assert.notNull(principal);

		result.remove(principal);
		return result;

	}

	// Other business methods

	public Actor findByPrincipal() {

		Actor result;
		UserAccount userAccount;
		try{
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);
		}catch(Throwable oops){
			result = null;
		}

		return result;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Actor result;
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

}
