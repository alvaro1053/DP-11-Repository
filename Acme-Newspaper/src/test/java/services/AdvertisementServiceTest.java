package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Advertisement;
import domain.CreditCard;
import domain.Newspaper;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class AdvertisementServiceTest extends AbstractTest {
	
	@Autowired
	AdvertisementService advertisementService;

	@Autowired
	NewspaperService	newspaperService;
	
	
	@Test
	public void registerAdvertisementAndListNewspapersPlaced(){
//		4. An actor who is authenticated as an agent must be able to:
//			2. Register an advertisement and place it in a newspaper.
//			3. List the newspapers in which they have placed an advertisement.
//
//		Vamos a involucrar para el test los siguientes requisitos en un caso de uso en el que un 
//		agente va a listar los periodicos en los que ha colocado anuncios y va a registrar un nuevo
//		anuncio en un periódico.

		
		Object testingData[][] = {
				//Caso de test 1: POSITIVO. Comportamiento: Como se describe anteriormente.
				{"agent1", "agent1","title1", "https://i.imgur.com/mz4KOzY.png", "https://i.imgur.com/mz4KOzY.png", "newspaper1",null},
				{"user1", "agent1","title1", "https://i.imgur.com/mz4KOzY.png", "https://i.imgur.com/mz4KOzY.png", "newspaper1",IllegalArgumentException.class},
				{"agent1", "agent1","", "https://i.imgur.com/mz4KOzY.png", "https://i.imgur.com/mz4KOzY.png", "newspaper1",IllegalArgumentException.class}
				
		};
		
		for(int i = 0; i < testingData.length ;i++){
			this.startTransaction();
			templateRegisterAdvertisementAndListNewspapersPlaced(((String) testingData[i][0]),super.getEntityId((String) testingData[i][1]),((String) testingData[i][2]),((String) testingData[i][3]),((String) testingData[i][4]),super.getEntityId((String) testingData[i][5]),((Class<?>) testingData[i][6]));
			this.rollbackTransaction();
		}
	}


	protected void templateRegisterAdvertisementAndListNewspapersPlaced(String username, int agentId, String title, String bannerURL, String targetPageURL, int newspaperId, Class<?> expected) {
		Class<?> caught;
		Collection<Newspaper> newspapers;
		Advertisement advertisement;
		Newspaper newspaper;
		newspapers = new ArrayList<Newspaper>();
		
		caught = null;
		
		newspaper = this.newspaperService.findOne(newspaperId);
		newspapers.add(newspaper);
		
		try{
			super.authenticate(username);
			
			advertisement = this.advertisementService.create();
			this.newspaperService.findPlacedAdsByAgent(agentId);
			
			advertisement.setTitle(title);
			advertisement.setBannerURL(bannerURL);
			advertisement.setTargetPageURL(targetPageURL);
			advertisement.setNewspapers(newspapers);
			advertisement.setCreditCard(generateCreditCard());
			
			this.advertisementService.save(advertisement);
			
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		checkExceptions(expected, caught);
	}
	
	protected CreditCard generateCreditCard(){
		CreditCard creditCard;
		
		creditCard = new CreditCard();
		creditCard.setHolderName("HolderTest");
		creditCard.setBrandName("Visa");
		creditCard.setNumber("5220277771031876");
		creditCard.setExpirationMonth(12);
		creditCard.setExpirationYear(21);
		creditCard.setCVV(424);
		
		return creditCard;
	}
	
	@Test
	public void registerAdvertisementAndListNewspapersNotPlaced(){
//	4. An actor who is authenticated as an agent must be able to:
//		2. Register an advertisement and place it in a newspaper.
//		4. List the newspapers in which they have not placed any advertisements.
//
//	Vamos a involucrar para el test los siguientes requisitos en un caso de uso en el que un 
//	agente va a listar los periodicos en los que ha colocado anuncios y va a registrar un nuevo
//	anuncio en un periódico.

		
		Object testingData[][] = {
				//Caso de test 1: POSITIVO. Comportamiento: Como se describe anteriormente.
				{"agent1", "agent1","title1", "https://i.imgur.com/mz4KOzY.png", "https://i.imgur.com/mz4KOzY.png", "newspaper1",null},
				{"customer", "agent1","title1", "https://i.imgur.com/mz4KOzY.png", "https://i.imgur.com/mz4KOzY.png", "newspaper1",IllegalArgumentException.class},
				{"agent1", "agent1","title1", "", "https://i.imgur.com/mz4KOzY.png", "newspaper1",IllegalArgumentException.class},
				{"agent1", "agent1","title1", "https://i.imgur.com/mz4KOzY.png", "", "newspaper1",IllegalArgumentException.class}
				
		};
		
		for(int i = 0; i < testingData.length ;i++){
			this.startTransaction();
			templateRegisterAdvertisementAndListNewspapersNotPlaced(((String) testingData[i][0]),super.getEntityId((String) testingData[i][1]),((String) testingData[i][2]),((String) testingData[i][3]),((String) testingData[i][4]),super.getEntityId((String) testingData[i][5]),((Class<?>) testingData[i][6]));
			this.rollbackTransaction();
		}
	}
	
	protected void templateRegisterAdvertisementAndListNewspapersNotPlaced(String username, int agentId, String title, String bannerURL, String targetPageURL, int newspaperId, Class<?> expected) {
		Class<?> caught;
		Collection<Newspaper> newspapers;
		Advertisement advertisement;
		Newspaper newspaper;
		newspapers = new ArrayList<Newspaper>();
		
		caught = null;
		
		newspaper = this.newspaperService.findOne(newspaperId);
		newspapers.add(newspaper);
		
		try{
			super.authenticate(username);
			
			advertisement = this.advertisementService.create();
			this.newspaperService.findNotPlacedAdsByAgent(agentId);
			
			advertisement.setTitle(title);
			advertisement.setBannerURL(bannerURL);
			advertisement.setTargetPageURL(targetPageURL);
			advertisement.setNewspapers(newspapers);
			advertisement.setCreditCard(generateCreditCard());
			
			this.advertisementService.save(advertisement);
			
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		checkExceptions(expected, caught);
	}
	
	
	@Test
	public void listAdvertisementsTabooAndDeleteAdvertisement(){
//	4. An actor who is authenticated as an agent must be able to:
//		2. Register an advertisement and place it in a newspaper.
//		4. List the newspapers in which they have not placed any advertisements.
//
//	Vamos a involucrar para el test los siguientes requisitos en un caso de uso en el que un 
//	agente va a listar los periodicos en los que ha colocado anuncios y va a registrar un nuevo
//	anuncio en un periódico.

		
		Object testingData[][] = {
				//Caso de test 1: POSITIVO. Comportamiento: Como se describe anteriormente.
				{"admin",null},
				//Caso de test 2: NEGATIVO. Comportamiento: Un usuario intentar realizar la función de un administrador.
				{"user1", IllegalArgumentException.class},
				//Caso de test 2: NEGATIVO. Comportamiento: Un cliente intentar realizar la función de un administrador.
				{"customer1", IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length ;i++){
			this.startTransaction();
			templateListAdvertisementsTabooAndDeleteAdvertisement(((String) testingData[i][0]),((Class<?>) testingData[i][1]));
			this.rollbackTransaction();
		}
	}
	
	protected void templateListAdvertisementsTabooAndDeleteAdvertisement(String username, Class<?> expected) {
		Class<?> caught;
		Advertisement advertisement = null;
		Collection<Advertisement> advertisementsWithTabooWords;
		caught = null;

		try{
			super.authenticate(username);
			
			advertisementsWithTabooWords = this.advertisementService.findAdvertisementWithTabooWords();
			
			advertisement = advertisementsWithTabooWords.iterator().next();
			
			this.advertisementService.delete(advertisement);
			
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		checkExceptions(expected, caught);
	}
}
