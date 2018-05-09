package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import domain.Agent;
import forms.ActorForm;

import security.UserAccount;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class AgentServiceTest extends AbstractTest {
	
	@Autowired
	AgentService		agentService;
		
	@Test
	public void driverRegisterAgent(){
		
/*
3. An actor who is not authenticated must be able to:
	1. Register to the system as an agent.
*/
			Object testingData[][] = {
					//Test positivo, probando el registro de un agente con todos sus campos
					{"agentPrueba1", "surn1","email@prueba.com", "111222333", "calle1",true,"prueba1", "prueba1pass", "prueba1pass",null},
					//Test negativo, probando el registro de un agente con el nombre vacio
					{"", "surn2","email@prueba.com", "111222333", "calle2",true,"prueba2", "prueba2pass", "prueba2pass",NullPointerException.class},
					//Test negativo, probando el registro de un agente con el apellido vacio
					{"agentPrueba3", "","email@prueba.com", "111222333", "calle3",true,"prueba3", "prueba3pass", "prueba3pass",NullPointerException.class},
					//Test negativo, probando el registro de un agente con el email con un pattern incorrecto
					{"agentPrueba4", "surn4","emailMAl", "111222333", "calle4",true,"prueba4", "prueba4pass", "prueba4pass",NullPointerException.class},
					//Test negativo, probando el registro de un agente con el email vacio
					{"agentPrueba5", "surn5","", "111222333", "calle5",true,"prueba5", "prueba5pass", "prueba5pass",NullPointerException.class},
					//Test negativo, probando el registro de un agente con la contraseña de seguridad de verificación distinta de la original
					{"agentPrueba6", "surn6","email@prueba.com", "111222333", "calle6",true,"prueba6", "prueba6pass", "mal",NullPointerException.class},
					//Test negativo, probando el registro de un agente con el campo de los términos y condiciones no marcados
					{"agentPrueba7", "surn7","email@prueba.com", "111222333", "calle7",false,"prueba7", "prueba7pass", "prueba7pass",NullPointerException.class},
					//Test negativo, probando el registro de un agente con el nombre de usuario a vacío
					{"agentPrueba8", "surn8","email@prueba.com", "111222333", "calle8",true,"", "prueba8pass", "prueba8pass",NullPointerException.class},
					//Test negativo, probando el registro de un agente con un nombre de usuario repetido
					{"agentPrueba9", "surn9","email@prueba.com", "111222333", "calle8",true,"agent1", "prueba9pass", "prueba9pass",DataIntegrityViolationException.class},
					//Test positivo, probando el registro de un agente con el número de teléfono con un signo +
					{"agentPrueba10", "surn10","email@prueba.com", "+111222333", "calle10",true,"prueba10", "prueba10pass", "prueba10pass",null},
			};
			for(int i = 0; i < testingData.length; i++){
				this.startTransaction();
				templateRegisterAgent(((String) testingData[i][0]),((String) testingData[i][1]),((String) testingData[i][2]),((String) testingData[i][3]),((String) testingData[i][4]),((Boolean) testingData[i][5]),((String) testingData[i][6]),((String) testingData[i][7]),((String) testingData[i][8]),((Class<?>) testingData[i][9]));
				this.rollbackTransaction();
			}
	}


	protected void templateRegisterAgent(String username,String surname, String email,String phone, String address, Boolean checkTerms, String userAccountName, String userAccountPassword, String userAccountConfirmPassword, Class<?> expected) {
		Class<?> caught = null;
		Agent agent = this.agentService.create();
		
		//==== Registrar datos =========
		ActorForm actorForm = registeringActorForm(username, surname, email, phone, address, checkTerms, userAccountName, userAccountPassword, userAccountConfirmPassword);
		BindingResult binding = null;
		try{
			agent = this.agentService.reconstruct(actorForm, binding);
			agent = this.agentService.save(agent);
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
}
