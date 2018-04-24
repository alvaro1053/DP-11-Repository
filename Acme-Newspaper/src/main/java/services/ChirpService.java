
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChirpRepository;
import domain.Admin;
import domain.Chirp;
import domain.User;
import forms.ChirpForm;

@Service
@Transactional
public class ChirpService {

	// Managed Repository
	@Autowired
	private ChirpRepository		chirpRepository;

	// Managed services
	@Autowired
	private UserService			userService;

	@Autowired
	private AdminService		adminService;
	
	@Autowired
	private Validator			validator;
	
	@Autowired
	private CustomisationService	customisationService;


	//Constructor
	public ChirpService() {
		super();
	}

	public ChirpForm create() {
		User principal;
		ChirpForm chirpForm;
		Date moment;

		moment = new Date(System.currentTimeMillis() - 1);

		principal = this.userService.findByPrincipal();
		Assert.notNull(principal);

		chirpForm = new ChirpForm();

		chirpForm.setMoment(moment);

		return chirpForm;
	}

	public Chirp save(final Chirp chirp) {
		User principal;
		Date moment;
		Chirp result;
		Collection<String> tabooWords;

		moment = new Date(System.currentTimeMillis() - 1);

		Assert.isTrue(chirp.getId()==0);//Me aseguro de que no pueda editar un chirp, solo crearlo

		principal = this.userService.findByPrincipal();
		Assert.notNull(principal);

		chirp.setMoment(moment);
		chirp.setUser(principal);
		
		tabooWords = this.customisationService.findCustomisation().getTabooWords();
		for (String word : tabooWords) {
			if(chirp.getTitle().toLowerCase().contains(word))
				chirp.setTabooWords(true);
			if(chirp.getDescription().toLowerCase().contains(word))
				chirp.setTabooWords(true);
		}

		result = this.chirpRepository.save(chirp);

		return result;
	}

	public void delete(final Chirp chirp) {
		Admin principal;//Solo se puede borrar por un admin
		User user;
		Collection<Chirp> chirps, updated;

		principal = this.adminService.findByPrincipal();
		Assert.notNull(principal);

		//Eliminar relaciones

		user = chirp.getUser();
		chirps = user.getChirps();
		updated = new ArrayList<Chirp>(chirps);
		updated.remove(chirp);
		user.setChirps(chirps);

		this.chirpRepository.delete(chirp);

	}


	public Chirp findOne(final int chirpId) {
		final Chirp chirp = this.chirpRepository.findOne(chirpId);
		return chirp;

	}
	
	public Collection<Chirp> findByUserFollowers(final int userId){
		Collection<Chirp> result;
		User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);
		result = this.chirpRepository.findByUserFollowers(userId);
		
		return result;
		
	}
	
	public Collection<Chirp> findChirpsWithTabooWords(){
		Collection<Chirp> result;
		Admin admin = this.adminService.findByPrincipal();
		Assert.notNull(admin);
		
		result = this.chirpRepository.findChirpsWithTabooWords();
		
		return result;
	}

	public void flush(){
		this.chirpRepository.flush();
	}

	public Chirp reconstruct(ChirpForm chirpForm, BindingResult binding) {
		Chirp result = new Chirp();
		User principal;
		
		
		principal = this.userService.findByPrincipal();
		
		
		result.setTitle(chirpForm.getTitle());
		result.setDescription(chirpForm.getDescription());
		result.setMoment(chirpForm.getMoment());
		result.setUser(principal);
		result.setTabooWords(false);
		
		this.validator.validate(result, binding);
		
		return result;
	}
}
