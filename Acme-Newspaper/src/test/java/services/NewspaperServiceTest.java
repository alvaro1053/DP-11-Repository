package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;


import domain.Article;
import domain.Newspaper;
import forms.NewspaperForm;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class NewspaperServiceTest extends AbstractTest {

	@Autowired
	private NewspaperService newspaperService;
	
	@Autowired
	private ArticleService articleService;
	
	@Test
	public void diverListNewspaper(){ 
		Object testingData[][]= {
				
/*
  4.2 An actor who is not authenticated must be able to: List the newspapers that are published and browse their articles.
*/
				//==========================================================================//
				//Tests POSITIVOS 
				//compruebo que puedo listar los newspaper y ver los articles del newspaper 1 sin estar logeado
				{null, "newspaper1",null},
				//compruebo que puedo listar los newspaper y ver los articles del newspaper 1 logeado como user
				{"user1", "newspaper1", null},
				//compruebo que puedo listar los newspaper y ver los articles del newspaper 1 logeado como admin
				{"admin", "newspaper1", null},
				//compruebo que puedo listar los newspaper y ver los articles del newspaper 1 logeado como customer
				{"customer1", "newspaper1", null},
				{"user1", "newspaper2", null},

		};
		for (int i = 0; i < testingData.length; i++){
			templateListNewspaper((String) testingData[i][0], super.getEntityId((String) testingData[i][1]),(Class<?>) testingData[i][2]);
		}
	}
	
	protected void templateListNewspaper(final String username, final int newspaperId, final Class<?> expected){
		
		Class<?> caught;
		Newspaper newspaper;
		caught = null;
		
		try{
			authenticate(username);
			this.newspaperService.publishedNewspapers();
			newspaper = this.newspaperService.findOne(newspaperId);
			if(newspaper.getIsPrivate()){
				
			}
			this.articleService.articlesOfNewspaper(newspaperId);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	@Test
	public void driverSearchNewspaper(){
		Object testingData[][] = {
								//Requisitos C: 6.5: Search for a published newspaper using a single keyword that must appear somewhere
								//in its title or its description.
								//==========================================================================//

								//Tests POSITIVOS 
								//
								//Un test en el que buscamos marca, hay un newspaper llamado marca así que debe devolverlo 
								{"user1", "Marca", null},
								//Tests NEGATIVOS
								//No hay nada con esa palabra, por lo que debe dar fallo el Assert del test que comprueba que la lista no esté vacía
								{"user1", "curifisqui", IllegalArgumentException.class},
								//Logueandome con un usuario que noe existe
								{"customer34", "estoesuncasodepruebanegativo",  IllegalArgumentException.class},
								//Con un customer que sí existe pero que  devuelve la lista vacía
								{"customer1", "estoesuncasodepruebanegativo",  IllegalArgumentException.class},
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateSearchNewspaper(((String) testingData[i][0]), (String) testingData[i][1],  (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}

	protected void templateSearchNewspaper(String username, String filter, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			Collection<Newspaper> res = new ArrayList<Newspaper>();
			res = this.newspaperService.findByFilter(filter);
			Assert.isTrue(!res.isEmpty());
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
	@Test
	public void driverChangePrivacity(){
		Object testingData[][] = {
//				23. An actor who is authenticated as a user must be able to:
				//1. Decide on whether a newspaper that he or she’s created is public or private.
								//==========================================================================//

								//Tests POSITIVOS 
								//
								//El user 1 es propietario del newspaper 1 por lo que no debe haber problema
								{"user1", "newspaper1", null},
								//Tests NEGATIVOS
								//Un user intentando cambiar la privacidad de un newspaper que no le corresponde
								{"user3", "newspaper1", IllegalArgumentException.class},
								//Intentar cambiarlo con otro rol
								{"admin", "newspaper2",  IllegalArgumentException.class},
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateChangePrivacity(((String) testingData[i][0]), this.getEntityId((String) testingData[i][1]),  (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}

	protected void templateChangePrivacity(String username, int newspaperId, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			this.newspaperService.changePrivacity(newspaperId);
		
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
	@Test
	public void driverCreateNewspaper(){
		final Date moment = new Date(System.currentTimeMillis());
		Object testingData[][] = {
								//Requisitos C: 6.1: Create a newspaper. A user who has created a news paper
								//is commonly referred to as a publisher
								//==========================================================================//

								//Tests POSITIVOS 
								//Poder crear y guardar un newspaper como se haría en el sistema
								{"user1", "prueba1Title", "Prueba1Description", "https://image.freepik.com/free-photo/cute-cat-picture_1122-449.jpg",moment, false, null},
								//Tests NEGATIVOS
								//Intentar crear y guardar un newspaper estando NO registrado
								{"", "prueba2Title", "Prueba2Description", "https://image.freepik.com/free-photo/cute-cat-picture_1122-449.jpg",moment, false, IllegalArgumentException.class},
								//Tests NEGATIVOS
								//Intentar crear y guardar un newspaper con el título en blanco
								{"user1", "", "Prueba3Description", "https://image.freepik.com/free-photo/cute-cat-picture_1122-449.jpg",moment, false, NullPointerException.class},
								//Tests NEGATIVOS
								//Intentar crear y guardar un newspaper con la descripción en blanco
								{"user2", "prueba4Title", "", "https://image.freepik.com/free-photo/cute-cat-picture_1122-449.jpg",moment, false, NullPointerException.class}
								
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templateCreateNewspaper(((String) testingData[i][0]), (String) testingData[i][1], ((String) testingData[i][2]),((String) testingData[i][3]),((Date) testingData[i][4]), ((Boolean) testingData[i][5]),(Class<?>) testingData[i][6]);
			this.rollbackTransaction();
		}
	}

	protected void templateCreateNewspaper(String username, String title,
			String description, String pictureURL, Date publicationDate, Boolean privateCheck,
			Class<?> expected) {
			Class<?> caught;
			caught = null;
			NewspaperForm newspaperForm;
			Newspaper	newspaper = new Newspaper();
			
			try{
				super.authenticate(username);
				
				newspaperForm = new NewspaperForm();
				
				newspaperForm.setTitle(title);
				newspaperForm.setDescription(description);
				newspaperForm.setPictureURL(pictureURL);
				newspaperForm.setPublicationDate(publicationDate);
				newspaperForm.setIsPrivate(privateCheck);
				
				BindingResult binding = null;
				try{
					newspaper = this.newspaperService.reconstruct(newspaperForm, binding);
				}catch(Throwable oops){
					caught = oops.getClass();
				}
				
				this.newspaperService.save(newspaper);
				
			}catch(Throwable oops){
				caught = oops.getClass();
			}
			
			checkExceptions(expected, caught);
		
	}
	
	
	@Test
	public void driverPublishNewspaper(){
		Object testingData[][] = {
								//Requisitos C: 6.2: Publish a newspaper that he or she's created. Note that
								//no newspaper can be published until each of the articles which is composed
								//is saved in final mode.
								//==========================================================================//

								//Tests POSITIVOS 
								//Poder publicar un newspaper, el cual es posible publicar
								{"user1", "newspaper3", null},
								//Test NEGATIVO
								//Un usuario intenta hacer publish de un newspaper que él no ha creado
								{"user2", "newspaper3", IllegalArgumentException.class},
								//Un usuario intenta hacer publish de un newspaper que ya esta publicado
								{"user1", "newspaper1", IllegalArgumentException.class},
								//Un usuario no registrado NO puede publicar un newspaper
								{"", "newspaper3", IllegalArgumentException.class}			
								
		};
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			templatePublishNewspaper(((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]),(Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}

	private void templatePublishNewspaper(String username, int newspaperId,
			Class<?> expected) {
		Class<?> caught;
		caught = null;
		
		try{
			super.authenticate(username);
			Newspaper newspaper = this.newspaperService.findOne(newspaperId);
			for (Article a : newspaper.getArticles()){
				a.setIsDraft(false);
			}
			this.newspaperService.publish(newspaper);
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		checkExceptions(expected, caught);
	}
		
		@Test
		public void driverDelete() {
			final Object testingData[][] = {
					//Requisito C 7.1 An actor who is authenticated as an administrator must be able to: Remove an newspaper that he or she thinks is inappropriate.
					
					//TEST POSITIVO
					//Pruebo que el admin puede eliminar el artículo 3
					{"admin", "newspaper1", null}, 
					//
					//==========================================================================//

					//TEST NEGATIVO
					//
					//Se elimina el article4 incorrectamente porque no lo puede eliminar un user
	//				{"user1", "newspaper1", IllegalArgumentException.class},
					//Se elimina el article1 incorrectamente porque no lo puede eliminar un customer
	//				{"customer1", "newspaper1", IllegalArgumentException.class}
			};
			for (int i = 0; i < testingData.length; i++)
				this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
		}
		private void templateDelete(final String username, final int newspaperId, final Class<?> expected) {
			Newspaper newspaper;
			Class<?> caught;

			caught = null;
			try {
				super.authenticate(username);
				newspaper = this.newspaperService.findOne(newspaperId);
				this.newspaperService.delete(newspaper);

				this.newspaperService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
			}

			this.checkExceptions(expected, caught);

			super.unauthenticate();
		}

	}
