package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import domain.User;
import forms.ActorForm;

import security.UserAccount;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class UserServiceTest extends AbstractTest {
	
	@Autowired
	UserService		userService;
	
	@Test
	public void diverListUser(){ 
		Object testingData[][]= {
				
/*
  4.3 An actor who is not authenticated must be able to: List the users of the system and display their profiles, which must include their personal
data and the list of articles that they have written as long as they are published
in a newspaper.
*/
				
				//Compruebo que puedo hacer listar los usuario y mostrar el perfil de un usuario sin estar logeado
				{null, "user1",null},
				//Compruebo que puedo hacer listar los usuario y mostrar el perfil de un usuario logeado como usuario
				{"user1", "user2", null},
				//Compruebo que puedo hacer listar los usuario y mostrar el perfil de un usuario logeado como admin
				{"admin", "user3", null},
				//Compruebo que puedo hacer listar los usuario y mostrar el perfil de un usuario logeado como customer
				{"customer1", "user1", null},

		};
		for (int i = 0; i < testingData.length; i++){
			templateListUser((String) testingData[i][0], super.getEntityId((String) testingData[i][1]),(Class<?>) testingData[i][2]);
		}
	}
	
	protected void templateListUser(final String username, final int userId, final Class<?> expected){
		
		Class<?> caught;
		caught = null;
		
		try{
			authenticate(username);
			this.userService.findAll();
			this.userService.findOne(userId);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	@Test
	public void driverRegisterUser(){
		
/*
4. An actor who is not authenticated must be able to:
	1. Register to the system as a user.
*/
			Object testingData[][] = {
					//Test positivo, probando el registro de un usuario con todos sus campos
					{"userPrueba1", "surn1","email@prueba.com", "111222333", "calle1",true,"prueba1", "prueba1pass", "prueba1pass",null},
					//Test negativo, probando el registro de un usuario con el nombre vacio
					{"", "surn2","email@prueba.com", "111222333", "calle2",true,"prueba2", "prueba2pass", "prueba2pass",NullPointerException.class},
					//Test negativo, probando el registro de un usuario con el apellido vacio
					{"userPrueba3", "","email@prueba.com", "111222333", "calle3",true,"prueba3", "prueba3pass", "prueba3pass",NullPointerException.class},
					//Test negativo, probando el registro de un usuario con el email con un pattern incorrecto
					{"userPrueba4", "surn4","emailMAl", "111222333", "calle4",true,"prueba4", "prueba4pass", "prueba4pass",NullPointerException.class},
					//Test negativo, probando el registro de un usuario con el email vacio
					{"userPrueba5", "surn5","", "111222333", "calle5",true,"prueba5", "prueba5pass", "prueba5pass",NullPointerException.class},
					//Test negativo, probando el registro de un usuario con la contraseña de seguridad de verificación distinta de la original
					{"userPrueba6", "surn6","email@prueba.com", "111222333", "calle6",true,"prueba6", "prueba6pass", "mal",NullPointerException.class},
					//Test negativo, probando el registro de un usuario con el campo de los términos y condiciones no marcados
					{"userPrueba7", "surn7","email@prueba.com", "111222333", "calle7",false,"prueba7", "prueba7pass", "prueba7pass",NullPointerException.class},
					//Test negativo, probando el registro de un usuario con el nombre de usuario a vacío
					{"userPrueba8", "surn8","email@prueba.com", "111222333", "calle8",true,"", "prueba8pass", "prueba8pass",NullPointerException.class},
					//Test negativo, probando el registro de un usuario con un nombre de usuario repetido
					{"userPrueba9", "surn9","email@prueba.com", "111222333", "calle8",true,"user1", "prueba9pass", "prueba9pass",DataIntegrityViolationException.class},
					//Test positivo, probando el registro de un usuario con el número de teléfono con un signo +
					{"userPrueba10", "surn10","email@prueba.com", "+111222333", "calle10",true,"prueba10", "prueba10pass", "prueba10pass",null},
					
			};
			for(int i = 0; i < testingData.length; i++){
				this.startTransaction();
				templateRegisterUser(((String) testingData[i][0]),((String) testingData[i][1]),((String) testingData[i][2]),((String) testingData[i][3]),((String) testingData[i][4]),((Boolean) testingData[i][5]),((String) testingData[i][6]),((String) testingData[i][7]),((String) testingData[i][8]),((Class<?>) testingData[i][9]));
				this.rollbackTransaction();
			}
	}


	protected void templateRegisterUser(String username,String surname, String email,String phone, String address, Boolean checkTerms, String userAccountName, String userAccountPassword, String userAccountConfirmPassword, Class<?> expected) {
		Class<?> caught = null;
		User user = this.userService.create();
		
		//==== Registrar datos =========
		ActorForm actorForm = registeringActorForm(username, surname, email, phone, address, checkTerms, userAccountName, userAccountPassword, userAccountConfirmPassword);
		BindingResult binding = null;
		try{
			user = this.userService.reconstruct(actorForm, binding);
			user = this.userService.save(user);
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		checkExceptions(expected, caught);
	}


	protected ActorForm registeringActorForm(String name, String surname, String email,String phone, String address, Boolean checkTerms, String userAccountName, String userAccountPassword, String userAccountConfirmPassword) {
		ActorForm actorForm = new ActorForm();
		
		actorForm.setName(name);
		actorForm.setSurname(surname);
		actorForm.setEmail(email);

		actorForm.setPhone(phone);
		actorForm.setAddress(address);
		
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(userAccountName);
		userAccount.setPassword(userAccountPassword);
		actorForm.setConfirmPassword(userAccountConfirmPassword);
		actorForm.setUserAccount(userAccount);
		
		actorForm.setCheck(checkTerms);	
		
		return actorForm;
	}
	
	/* 16.2: An actor who is authenticated as a user must be able to follow or 
	 *       unfollow another user. */
	
	@Test
	public void driverFollowUnfollowUser() {
		Object testingData[][] = {
			// User1 sigue y deja de seguir al user3
			{"user1", "user3", null},
			// User1 sigue al user2, que ya seguía anteriormente
			{"user1", "user2", IllegalArgumentException.class},
			// Usuario no autenticado sigue al user1
			{null, "user1", IllegalArgumentException.class}
		};
		
		for(int i = 0; i<testingData.length; i++) {
			this.startTransaction();
			templateFollowUnfollowUser((String) testingData[i][0], getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}
	
	protected void templateFollowUnfollowUser(String username, int userToBeFollowedId, Class<?> expected) {
		Class<?> caught;
		User principal, userToBeFollowed;
		
		caught=null;
		
		try{
			this.authenticate(username);
			
			userToBeFollowed = this.userService.findOne(userToBeFollowedId);
			
			principal = this.userService.findByPrincipal();
			this.userService.follow(userToBeFollowed);
			Assert.isTrue(principal.getFollows().contains(userToBeFollowed));
			Assert.isTrue(userToBeFollowed.getFollowers().contains(principal));
			
			this.userService.unfollow(userToBeFollowed);
			Assert.isTrue(!principal.getFollows().contains(userToBeFollowed));
			Assert.isTrue(!userToBeFollowed.getFollowers().contains(principal));
			
			this.userService.flush();
			
			this.unauthenticate();
		} catch (Throwable oops) {
			this.unauthenticate();
			caught = oops.getClass();
		}
		
		this.checkExceptions(expected, caught);
	}

}
