package services;



import java.util.Calendar;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.Volume;
import forms.VolumeForm;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class VolumeServiceTest extends AbstractTest {

	@Autowired
	private VolumeService volumeService;
	
	@Autowired
	private NewspaperService newspaperService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private SubscriptionService subscriptionService;
	
	@Test
	public void diverListVolume(){ 
		Object testingData[][]= {
				
/*
  	8.1 An actor who is not authenticated must be able to:List the volumes in the system and browse their newspapers as long as they are
		authorised (for instance, a private newspaper cannot be fully displayed to
		unauthenticated actors).
*/
				//==========================================================================//
				//Tests POSITIVOS 
				//compruebo que puedo listar los volúmenes y ver los periódicos del volumen 1 sin estar logeado
				{null, "volume1",null},
				//compruebo que puedo listar los volúmenes y ver los periódicos del volumen 1 logeado como user
				{"user1", "volume1", null},
				{"user1", "volume2", null},
				//compruebo que puedo listar los volúmenes y ver los periódicos del volumen 1 logeado como admin
				{"admin", "volume1", null},
				//compruebo que puedo listar los volúmenes y ver los periódicos del volumen 1 logeado como customer
				{"customer1", "volume1", null},

		};
		for (int i = 0; i < testingData.length; i++){
			templateListVolume((String) testingData[i][0], super.getEntityId((String) testingData[i][1]),(Class<?>) testingData[i][2]);
		}
	}
	
	protected void templateListVolume(final String username, final int volumeId, final Class<?> expected){
		
		Class<?> caught;
		Volume volume;
		caught = null;
		
		try{
			authenticate(username);
			this.volumeService.findAll();
			volume = this.volumeService.findOne(volumeId);
			volume.getNewspapers();
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	
	@Test
	public void driverCreateVolume(){
		/*
		 * 10.1 An actor who is authenticated as a user must be able to: Create a volume with as many published newspapers as he or she wishes. Note that
				the newspapers in a volume can be added or removed at any time. The same
				newspaper may be used to create different volumes.
		 */
		
		
		Calendar cal= Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		
		Object testingData[][] = {
	
								//Tests POSITIVOS 
								//Poder crear y guardar un volumen correctamente
								{"user1", "prueba1Title", "Prueba1Description", year, null},
								//=======================================================================
								//Tests NEGATIVOS
								//Intentar crear y guardar un newspaper estando NO registrado
								{"", "prueba2Title", "Prueba2Description", year, IllegalArgumentException.class},
								//Intentar crear y guardar un newspaper con el título en blanco
								{"user1", "", "Prueba3Description", year, NullPointerException.class},
								//Intentar crear y guardar un newspaper con la descripción en blanco
								{"user2", "prueba4Title", "", year, NullPointerException.class},
								//Intentar crear y guardar un newspaper con un año no válido (1900-3000)
								{"user2", "prueba5Title", "Prueba5Description", 1899, NullPointerException.class},
								
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateCreateVolume(((String) testingData[i][0]), (String) testingData[i][1], ((String) testingData[i][2]),((int) testingData[i][3]), (Class<?>) testingData[i][4]);
			this.rollbackTransaction();
		}
	}

	protected void templateCreateVolume(final String username,final String title, final String description, final int year, final Class<?> expected) {
			Class<?> caught;
			caught = null;
			VolumeForm volumeForm;
			Volume	volume = new Volume();
			Collection<Newspaper> newspapers;

			try{
				super.authenticate(username);
				
				newspapers = this.newspaperService.findAll();
				
				volumeForm = new VolumeForm();
				
				volumeForm.setTitle(title);
				volumeForm.setDescription(description);
				volumeForm.setYear(year);
				volumeForm.setNewspapers(newspapers);
				
				BindingResult binding = null;
				try{
					volume = this.volumeService.reconstruct(volumeForm, binding);
				}catch(Throwable oops){
					caught = oops.getClass();
				}
				
				this.volumeService.save(volume);
				
			}catch(Throwable oops){
				caught = oops.getClass();
			}
			
			checkExceptions(expected, caught);
		
	}
	
	@Test
	public void driverSubscribe(){
		Object testingData[][] = {

				/*
				 * 9.1 An actor who is authenticated as a customer must be able to:Subscribe to a volume by providing a credit card. Note that subscribing to a volume
						implies subscribing automatically to all of the newspapers of which it is composed,
						including newspapers that might be published after the subscription takes place.
				 */

								//Tests POSITIVOS 
								//
								//Un customer se subscribe normalmente a un volumen
								{"customer1", "volume1", null},
								//Tests NEGATIVOS
								//Intentar subscribirse a un volumen logeado como user
								{"user1", "volume1",  IllegalArgumentException.class},
								//Intentar subscribirse a un volumen logeado como user
								{"admin", "volume1",  IllegalArgumentException.class}
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateSubscribe(((String) testingData[i][0]), this.getEntityId((String) testingData[i][1]),  (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}

	protected void templateSubscribe(String username, int volumeId, Class<?> expected) {
		Class<?> caught;
		caught = null;
		Volume volume;
		try{
			super.authenticate(username);
	
			volume = this.volumeService.findOne(volumeId);
			
			CreditCard creditCard = new CreditCard();
			creditCard.setHolderName("HolderTest");
			creditCard.setBrandName("Visa");
			creditCard.setNumber("5220277771031876");
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(21);
			creditCard.setCVV(424);
			
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
			
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
	}
