package services;

import java.util.ArrayList;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import services.UserService;
import repositories.VolumeRepository;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.User;
import domain.Volume;
import forms.VolumeForm;


@Service
@Transactional
public class VolumeService {

	//Managed Repository ----
	@Autowired
	private VolumeRepository	volumeRepository;
	
	//Services
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private Validator validator;
	

	//Constructors
	public VolumeService() {
		super();
	}

	public Volume create() {
		Volume result;
		User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);
		result = new Volume();
		result.setUser(principal);
		result.setNewspapers(new ArrayList<Newspaper>());
		return result;
	}

	public Collection<Volume> findAll() {
		Collection<Volume> result;

		result = this.volumeRepository.findAll();

		return result;
	}
	

	public Volume save(final Volume volume) {
		Volume result;
		User principal = userService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(volume.getUser().equals(principal));
		
		result = this.volumeRepository.save(volume);
		
		Collection<Volume> creatorsVolumes = principal.getVolumes();
		creatorsVolumes.add(result);
		principal.setVolumes(creatorsVolumes);
		
	
		return result;
	}

	public Volume findOne(final int volumeId) {
		Volume result;

		result = this.volumeRepository.findOne(volumeId);
		Assert.notNull(result);

		return result;
	}
	
	public Volume reconstruct (VolumeForm volumeForm, BindingResult binding){
		User user;
		
		user = this.userService.findByPrincipal();
		Volume volume = this.create();
		
		volume.setTitle(volumeForm.getTitle());
		volume.setDescription(volumeForm.getDescription());

		volume.setYear(volumeForm.getYear());
		volume.setNewspapers(volumeForm.getNewspapers());
		volume.setId(volumeForm.getId());
		volume.setVersion(volumeForm.getVersion());
		
		if(volume.getUser().getId() != 0){
			volume.setUser(user);
		}
	
		validator.validate(volumeForm, binding);
		return volume;
	}
	
	public VolumeForm reconstructForm(final Volume volume) {
		VolumeForm result;

		result = new VolumeForm();

		result.setId(volume.getId());
		result.setVersion(volume.getVersion());
		result.setTitle(volume.getTitle());
		result.setDescription(volume.getDescription());
		result.setYear(volume.getYear());
		result.setNewspapers(volume.getNewspapers());

		return result;
	}


	public void flush() {
		this.volumeRepository.flush();
	}
	
	public void subscribe(Volume volume, CreditCard creditCard){
		Customer principal = this.customerService.findByPrincipal();
		Collection<Newspaper> newspapers = volume.getNewspapers();
		Collection<Subscription> subscriptions = principal.getSubscriptions();
		for (Subscription s : subscriptions){
			if(newspapers.contains(s.getNewspaper())){
				newspapers.remove(s.getNewspaper());
			}
			}
		for(Newspaper n : newspapers){
			Subscription subscription = this.subscriptionService.create();
			subscription.setCreditCard(creditCard);
			subscription.setNewspaper(n);
			subscription.setCustomer(principal);
			if(n.getIsPrivate() == true){
			this.subscriptionService.save(subscription);
			}
		}
		}
	}


