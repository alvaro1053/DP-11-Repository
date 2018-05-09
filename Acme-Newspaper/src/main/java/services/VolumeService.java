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
		result.setCustomersSubscribed(new ArrayList<Customer>());
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
		
		//Comprobamos si los newspapers han cambiado
		if(volume.getId() != 0){
			Volume oldVolume = this.findOne(volume.getId());
			
			Collection<Newspaper> oldNewspapers = new ArrayList<Newspaper>(oldVolume.getNewspapers());
			Collection<Newspaper> actualNewspapers = new ArrayList<Newspaper>(volume.getNewspapers());
			//Si ha cambiado debemos subscribir a todos los usuarios subscritos a los nuevos newspapers que se hayan añadido
			if(!(oldNewspapers.equals(actualNewspapers))){
				Collection<Newspaper> newNewspapers = new ArrayList<Newspaper>(actualNewspapers);
				newNewspapers.removeAll(oldNewspapers);
				Collection<Newspaper> newspapersRemoved = new ArrayList<Newspaper>(oldNewspapers);
				newspapersRemoved.removeAll(actualNewspapers);
				
				
				Collection<Customer> subscribed = new ArrayList<Customer>(volume.getCustomersSubscribed());
				
				//Para la creditCard, puesto que no sabemos cual es pondremos una ficticia puesto que ya el Customer está subscrito, por lo que ya ha pagado	
				CreditCard creditCard = new CreditCard();
				creditCard.setHolderName("Ficticia");
				creditCard.setBrandName("Ficticia");
				creditCard.setExpirationMonth(12);
				creditCard.setExpirationYear(98);
				creditCard.setCVV(234);
				creditCard.setNumber("5540500001000004");
				for(Customer c : subscribed ){
					for (Newspaper n : newNewspapers){
					Subscription subscription = new Subscription();
					subscription.setCustomer(c);
					subscription.setNewspaper(n);
					subscription.setCreditCard(creditCard);
					this.subscriptionService.updateVolumeSubscription(subscription);
				}
					for (Newspaper n : newspapersRemoved) {
						if (n.getIsPrivate()) {
							Subscription toRemove = this.subscriptionService
									.findByCustomerAndNewspaperint(c.getId(),
											n.getId());
							this.subscriptionService
									.updateDeleteByVolumen(toRemove);
						}
					}
				}
			}

		}
		
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
		if(volumeForm.getId() != 0){
		Volume copy = this.findOne(volumeForm.getId());
		volume.setCustomersSubscribed(copy.getCustomersSubscribed());
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
		Collection<Volume> toUpdate,updated;
		Collection<Customer> toUpdate2, updated2;
		
		Customer principal = this.customerService.findByPrincipal();
		Collection<Newspaper> newspapers = volume.getNewspapers();
		Collection<Subscription> subscriptions = principal.getSubscriptions();
		
		//Eliminamos los newspapers a los que ya está subscrito el principal
		for (Subscription s : subscriptions){
			if(newspapers.contains(s.getNewspaper())){
				newspapers.remove(s.getNewspaper());
			}
			}
		//Le subscribimos a los restantes
		for(Newspaper n : newspapers){
			Subscription subscription = this.subscriptionService.create();
			subscription.setCreditCard(creditCard);
			subscription.setNewspaper(n);
			subscription.setCustomer(principal);
			if(n.getIsPrivate() == true){
			this.subscriptionService.save(subscription);
			}
		}
		toUpdate = principal.getVolumesSubscribed();
		toUpdate.add(volume);
		updated = new ArrayList<Volume>(toUpdate);
		principal.setVolumesSubscribed(updated);
		
		toUpdate2 = volume.getCustomersSubscribed();
		toUpdate2.add(principal);
		updated2 = new ArrayList<Customer>(toUpdate2);
		volume.setCustomersSubscribed(updated2);
		
		
		}
	}


