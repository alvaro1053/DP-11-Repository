package services;

import java.sql.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Chirp;
import domain.User;
import forms.ChirpForm;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class ChirpServiceTest extends AbstractTest {

	@Autowired
	private ChirpService chirpService;
	@Autowired
	private UserService userService;
	
	@Test
	public void driverPostChirp(){
		
		Object testingData[][] = {
								//Requisitos B 16. An actor who is authenticated as a user must be able to:
								//1. Post a chirp. Chirps may not be changed or deleted once they are posted.
							
								//Tests POSITIVOS 
								//
								//Crear un chirp como un usuario
								{"user1", null,false, null},
								//Tests NEGATIVOS
								//Intentar editar un chrip ya creado
								{"user1", "chirp1",true, IllegalArgumentException.class},
								//Intentar crear un chirp con otro rol
								{"customer", null,false,  IllegalArgumentException.class},
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templatePostChirp(((String) testingData[i][0]), (String) testingData[i][1], (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);
			this.rollbackTransaction();
		}
	}

	protected void templatePostChirp(String username, String chirpId,Boolean editing, Class<?> expected) {
		Class<?> caught;
		caught = null;
		Chirp chirp = new Chirp();
		try{
			super.authenticate(username);
			if(editing == false){
			//Crear el chirp
			ChirpForm chirpForm = this.chirpService.create();
			chirpForm.setDescription("Test");
			chirpForm.setMoment(new Date(System.currentTimeMillis() - 1));
			chirpForm.setTitle("Title test");

			chirp = this.chirpService.reconstruct(chirpForm, null);
		} else{
			chirp = this.chirpService.findOne(this.getEntityId(chirpId));
			chirp.setDescription("test2");
		}
			this.chirpService.save(chirp);
			
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
	
	@Test
	public void driverDisplayStreamOfChirps(){
		
		Object testingData[][] = {
								//Requisitos B 16. An actor who is authenticated as a user must be able to:
								//1. Post a chirp. Chirps may not be changed or deleted once they are posted.
								//5. Display a stream with the chirps posted by all of the users that he or she follows.
								//Tests POSITIVOS 
								//
								//Mostrar Stream como usuario1
								{"user1",null},
								//Tests NEGATIVOS
								//Mostrar Stream como customer (No puede seguir a nadie)
								{"customer1", IllegalArgumentException.class},
								//Lo mismo pero con admin, que tampoco puede seguir a nadie
								{"admin", IllegalArgumentException.class},
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateDisplayStream(((String) testingData[i][0]), (Class<?>) testingData[i][1]);
			this.rollbackTransaction();
		}
	}

	protected void templateDisplayStream(String username, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			User user = this.userService.findByPrincipal();
			this.chirpService.findByUserFollowers(user.getId());
			
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
	@Test
	public void driverDeleteChirp(){
		
		Object testingData[][] = {
//				17. An actor who is authenticated as an administrator must be able to:
//					5. Remove a chirp that he or she thinks is inappropriate.
								//Tests POSITIVOS 
								//
								//Borrar un chirp con admin no debe de dar problemas
								{"admin","chirp1",null},
								//Tests NEGATIVOS
								//Ningun otro actor debe poder borrarlo, como los customers
								{"customer1","chirp1", IllegalArgumentException.class},
								//Los usuarios que sean propietarios del chirp tampoco deben poder
								{"user1","chirp1", IllegalArgumentException.class},
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateDeleteChirp(((String) testingData[i][0]), this.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}

	protected void templateDeleteChirp(String username, int chirpId, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			Chirp chirp = this.chirpService.findOne(chirpId);
			this.chirpService.delete(chirp);
			
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
		
	}

