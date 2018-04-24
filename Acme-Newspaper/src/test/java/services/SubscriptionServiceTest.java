package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;

import domain.Subscription;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class SubscriptionServiceTest extends AbstractTest {

	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private NewspaperService newspaperService;
	
	
	@Test
	public void driverSubscribe(){
		Object testingData[][] = {
//				22. An actor who is authenticated as a customer can:
//					1. Subscribe to a private newspaper by providing a valid credit card.
								//==========================================================================//

								//Tests POSITIVOS 
								//
								//Un customer de subscribe normalmente
								{"customer1", "newspaper3", null},
								//Tests NEGATIVOS
								//Un customer se subscribe a un newspaper que ya está subscrito
								{"customer1", "newspaper2", IllegalArgumentException.class},
								//Intentar subscribir con una clase que no sea customer
								{"user1", "newspaper2",  IllegalArgumentException.class},
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateSubscribe(((String) testingData[i][0]), this.getEntityId((String) testingData[i][1]),  (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}

	protected void templateSubscribe(String username, int newspaperId, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			
			Subscription subscription = this.subscriptionService.create();
			subscription.setCustomer(this.customerService.findByPrincipal());
			subscription.setNewspaper(this.newspaperService.findOne(newspaperId));
			
			CreditCard creditCard = new CreditCard();
			creditCard.setHolderName("HolderTest");
			creditCard.setBrandName("Visa");
			creditCard.setNumber("5220277771031876");
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(21);
			creditCard.setCVV(424);
			
			subscription.setCreditCard(creditCard);
			
			
			this.subscriptionService.save(subscription);
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
	}