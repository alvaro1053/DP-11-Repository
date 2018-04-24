package services;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class CustomisationServiceTest extends AbstractTest {

	@Autowired
	private CustomisationService customisationService;

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private NewspaperService newspaperService;
	
	@Autowired
	private ChirpService chirpService;
	
	@Test
	public void driverListTaboo(){
		
		Object testingData[][] = {
		//Requisitos B 17.1 An actor who is authenticated as an administrator must be able to: Manage a list of taboo words.
							
								//Un admin puede listar las taboo words
								{"admin", null},
								
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateListTaboo(((String) testingData[i][0]), (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
		}
	}

	protected void templateListTaboo (String username, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			this.customisationService.findCustomisation();
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
	
	@Test
	public void driverListArticlesNewspapersChirpsTaboo(){
		
		Object testingData[][] = {
		//Requisitos B 17.2 An actor who is authenticated as an administrator must be able to: List the articles that contain taboo words.
		//Requisitos B 17.3 An actor who is authenticated as an administrator must be able to: List the newspapers that contain taboo words.
		//Requisitos B 17.4 An actor who is authenticated as an administrator must be able to: List the chirps that contain taboo words.

							
								//Un admin puede listar las taboo words
								{"admin", null},
								//Un user puede listar las taboo words
								{"user1", IllegalArgumentException.class},
								//Un customer puede listar las taboo words
								{"customer1", IllegalArgumentException.class},
								
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateListArticlesTaboo(((String) testingData[i][0]), (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
		}
	}

	protected void templateListArticlesTaboo (String username, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			this.articleService.findArticlesWithTabooWords();
			this.newspaperService.findNewspapersWithTabooWords();
			this.chirpService.findChirpsWithTabooWords();

			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	

	}

