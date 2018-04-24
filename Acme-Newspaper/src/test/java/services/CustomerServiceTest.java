package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import domain.Customer;
import forms.ActorForm;

import security.UserAccount;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class CustomerServiceTest extends AbstractTest {
	
	@Autowired
	CustomerService		customerService;
	
	
	@Test
	public void driverRegisterCustomer(){
		//Requisitos A 21.1: An actor who is not authenticated must be able to: register to the system as a customer
			Object testingData[][] = {
					{"customerPrueba1", "surn1","email@prueba.com", "111222333", "calle1",true,"prueba1", "prueba1pass", "prueba1pass",null},
					{"", "surn2","email@prueba.com", "111222333", "calle2",true,"prueba2", "prueba2pass", "prueba2pass",NullPointerException.class},
					{"customerPrueba3", "","email@prueba.com", "111222333", "calle3",true,"prueba3", "prueba3pass", "prueba3pass",NullPointerException.class},
					{"customerPrueba4", "surn4","emailMAl", "111222333", "calle4",true,"prueba4", "prueba4pass", "prueba4pass",NullPointerException.class},
					{"customerPrueba5", "surn5","", "111222333", "calle5",true,"prueba5", "prueba5pass", "prueba5pass",NullPointerException.class},
					{"customerPrueba6", "surn6","email@prueba.com", "111222333", "calle6",true,"prueba6", "prueba6pass", "mal",NullPointerException.class},
					{"customerPrueba7", "surn7","email@prueba.com", "111222333", "calle7",false,"prueba7", "prueba7pass", "prueba7pass",NullPointerException.class},
					{"customerPrueba8", "surn8","email@prueba.com", "111222333", "calle8",true,"", "prueba8pass", "prueba8pass",NullPointerException.class},
					{"customerPrueba9", "surn9","email@prueba.com", "111222333", "calle8",true,"customer1", "prueba9pass", "prueba9pass",DataIntegrityViolationException.class},
					{"customerPrueba10", "surn10","email@prueba.com", "+111222333", "calle10",true,"prueba10", "prueba10pass", "prueba10pass",null},
					
					
			};
			for(int i = 0; i < testingData.length; i++){
				this.startTransaction();
				templateRegisterCustomer(((String) testingData[i][0]),((String) testingData[i][1]),((String) testingData[i][2]),((String) testingData[i][3]),((String) testingData[i][4]),((Boolean) testingData[i][5]),((String) testingData[i][6]),((String) testingData[i][7]),((String) testingData[i][8]),((Class<?>) testingData[i][9]));
				this.rollbackTransaction();
			}
	}


	protected void templateRegisterCustomer(String username,String surname, String email,String phone, String address, Boolean checkTerms, String customerAccountName, String customerAccountPassword, String customerAccountConfirmPassword, Class<?> expected) {
		Class<?> caught = null;
		Customer customer = this.customerService.create();
		
		//==== Registrar datos =========
		ActorForm actorForm = registeringActorForm(username, surname, email, phone, address, checkTerms, customerAccountName, customerAccountPassword, customerAccountConfirmPassword);
		BindingResult binding = null;
		try{
			customer = this.customerService.reconstruct(actorForm, binding);
			customer = this.customerService.save(customer);
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		checkExceptions(expected, caught);
	}


	protected ActorForm registeringActorForm(String name, String surname, String email,String phone, String address, Boolean checkTerms, String customerAccountName, String customerAccountPassword, String customerAccountConfirmPassword) {
		ActorForm actorForm = new ActorForm();
		
		actorForm.setName(name);
		actorForm.setSurname(surname);
		actorForm.setEmail(email);

		actorForm.setPhone(phone);
		actorForm.setAddress(address);
		
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(customerAccountName);
		userAccount.setPassword(customerAccountPassword);
		actorForm.setConfirmPassword(customerAccountConfirmPassword);
		actorForm.setUserAccount(userAccount);
		
		actorForm.setCheck(checkTerms);	
		
		return actorForm;
	}

}
