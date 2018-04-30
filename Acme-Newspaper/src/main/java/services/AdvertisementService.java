package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Admin;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;
import forms.AdvertisementForm;

import repositories.AdvertisementRepository;


@Transactional
@Service
public class AdvertisementService {

	@Autowired
	AdvertisementRepository advertisementRepository;
	
	@Autowired
	AgentService agentService;
	
	@Autowired
	CustomisationService	customisationService;
	
	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	AdminService			adminService;
	
	@Autowired
	Validator validator;
	
	public AdvertisementService(){
		super();
	}
	
	public Advertisement create(){
		Advertisement result;
		Agent principal;
		
		principal = this.agentService.findByPrincipal();
		
		result = new Advertisement();
		result.setAgent(principal);
		result.setTabooWords(false);
		
		return result;
	}

	public Advertisement findOne(int advertisementId) {
		Advertisement result;
		
		result = this.advertisementRepository.findOne(advertisementId);
		
		return result;
	}

	public Advertisement reconstruct(AdvertisementForm advertisementForm, BindingResult binding) {
		Advertisement result;
		Collection<String> tabooWords;
		
		result = create();
		result.setBannerURL(advertisementForm.getBannerURL());
		result.setTargetPageURL(advertisementForm.getTargetPageURL());
		result.setTitle(advertisementForm.getTitle());
		result.setCreditCard(advertisementForm.getCreditCard());
		
		if(!(advertisementForm.getNewspapers() == null)){		
			result.setNewspapers(advertisementForm.getNewspapers());
		}
		
		tabooWords = this.customisationService.findCustomisation().getTabooWords();
		for (String word : tabooWords) {
			if(result.getTitle().toLowerCase().contains(word))
				result.setTabooWords(true);
		}
		
		validator.validate(result, binding);
		
		//Comprobación de que la creditcard no caduca en el mes actual o se encuentra caducada
		this.subscriptionService.checkDate(result.getCreditCard(), binding);
		
		return result;
	}

	public void save(Advertisement advertisement) {
		Assert.notNull(advertisement);
		Assert.notNull(advertisement.getAgent());
		Advertisement saved;
		Collection<Newspaper> relatedNewspapers;
		Collection<Advertisement> beforeUpdate;
		
		relatedNewspapers = advertisement.getNewspapers();
		
		saved = this.advertisementRepository.save(advertisement);
		
		for (Newspaper newspaper : relatedNewspapers) {
			beforeUpdate = newspaper.getAdverts();
			beforeUpdate.add(saved);
			newspaper.setAdverts(beforeUpdate);
		}
		
	}

	public Collection<Advertisement> findAdvertisementWithTabooWords() {
		Collection<Advertisement> result;
		
		result = this.advertisementRepository.findAdvertisementWithTabooWords();
		
		return result;
	}

	public void delete(Advertisement advert) {
		Admin admin;
		Collection<Advertisement> updated;
		Agent agent;
		final Collection<Newspaper> newspapers;
		Assert.notNull(advert);
		
		
		admin = this.adminService.findByPrincipal();
		Assert.notNull(admin);
		
		newspapers = advert.getNewspapers();
		agent = advert.getAgent();
		
		for(Newspaper newspaper : newspapers){
			updated = newspaper.getAdverts();
			updated.remove(advert);
			newspaper.setAdverts(updated);
		}
		
		updated = agent.getAdvertisements();
		updated.remove(advert);
		agent.setAdvertisements(updated);
		
		this.advertisementRepository.delete(advert);
	}
}
