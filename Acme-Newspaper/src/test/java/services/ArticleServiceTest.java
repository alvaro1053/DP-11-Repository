package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Article;
import domain.Newspaper;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class ArticleServiceTest extends AbstractTest {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private NewspaperService newspaperService;
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateArticle(){
		
        final Date moment = new Date(System.currentTimeMillis());
        Collection<String> photosURL = new ArrayList<String>();
        photosURL.add("http://www.picture1.com");
        photosURL.add("http://www.picture2.com");

		Object testingData[][] = {
				//Requisito C 6.3: Write an article and attach it to any newspaper that has not been published, yet.
				//Note that articles may be saved in draft mode, which allows to modify them later, or final model,
				//which freezes them forever. 
				//==========================================================================//

				//Tests POSITIVOS 
				//
				//Pruebo a escribir un artículo que se asocie a un periódico sin publicar, y que no esté en modo final.
				{"user1", "newspaper3", "title", moment, "summary", "body", photosURL, true, null},
				//==========================================================================//

				//Tests NEGATIVOS
				//
				//Pruebo que NO me deje escribir un artículo que se asocie a un periódico PUBLICADO, y que no esté en modo final.
				{"user1", "newspaper1", "title", moment, "summary", "body", photosURL, true, IllegalArgumentException.class},
				
				//Pruebo que NO me deje escribir un artículo con título vacío
				{"user1", "newspaper3", "", moment, "summary", "body", photosURL, true, javax.validation.ConstraintViolationException.class},
				
				
				};
		
		
				for (int i = 0; i < testingData.length; i++){
				this.startTransaction();
				templateCreateArticle((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Date) testingData[i][3],
						(String) testingData[i][4], (String) testingData[i][5], (List<String>) testingData[i][6], (boolean) testingData[i][7], (Class<?>) testingData[i][8]);
				this.rollbackTransaction();
				}
	}
	
	protected void templateCreateArticle(final String username, final int newspaperId, final String title, final Date moment, final String summary,
			final String body, final List<String> photosURL, final boolean isDraft, final Class<?> expected) {
		Class<?> caught;
		Article article;
		Newspaper newspaper;
		caught = null;
		try{
			super.authenticate(username);
			article = this.articleService.create();
			newspaper = this.newspaperService.findOne(newspaperId);
			
			article.setNewspaper(newspaper);

			article.setTitle(title);
			article.setSummary(summary);
			article.setBody(body);
			article.setMoment(moment);
			article.setPhotosURL(photosURL);
			article.setIsDraft(isDraft);
			this.articleService.save(article);
			this.articleService.flush();
		
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
	
/*	
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditArticle(){
		
        final Date moment = new Date(System.currentTimeMillis());
        Collection<String> photosURL = new ArrayList<String>();
        photosURL.add("http://www.picture1.com");
        photosURL.add("http://www.picture2.com");

		Object testingData[][] = {
				//Requisito C 6.3: Write an article and attach it to any newspaper that has not been published, yet.
				//Note that articles may be saved in draft mode, which allows to modify them later, or final model,
				//which freezes them forever. 
				//==========================================================================//

				//Tests POSITIVOS 
				//
				//Pruebo a escribir un artículo que se asocie a un periódico sin publicar, y que no esté en modo final.
				{"user1", "article1", "newspaper3", "title", moment, "summary", "body", photosURL, true, null},
				//Tests NEGATIVOS
				//

				
				
				};
		
		
				for (int i = 0; i < testingData.length; i++){
				this.startTransaction();
				templateEditArticle((String) testingData[i][0],super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (String) testingData[i][3], (Date) testingData[i][4],
						(String) testingData[i][5], (String) testingData[i][6], (List<String>) testingData[i][7], (boolean) testingData[i][8], (Class<?>) testingData[i][9]);
				this.rollbackTransaction();
				}
	}
	
	protected void templateEditArticle(final String username, final int articleId, final int newspaperId, final String title, final Date moment, final String summary,
			final String body, final List<String> photosURL, final boolean isDraft, final Class<?> expected) {
		Class<?> caught;
		Article article;
		Newspaper newspaper;
		caught = null;
		try{
			super.authenticate(username);
			article = this.articleService.findOne(articleId);
			newspaper = this.newspaperService.findOne(newspaperId);
			
			article.setNewspaper(newspaper);

			article.setTitle(title);
			article.setSummary(summary);
			article.setBody(body);
			article.setMoment(moment);
			article.setPhotosURL(photosURL);
			article.setIsDraft(isDraft);
			this.articleService.save(article);
			this.articleService.flush();
		
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
*/	
	
		@Test
		public void driverDelete() {
			final Object testingData[][] = {
					//Requisito C 7.1 An actor who is authenticated as an administrator must be able to: Remove an article that he or she thinks is inappropriate.

					//TEST POSITIVO
					//Pruebo que el admin puede eliminar el artículo 3
					{"admin", "article4", null}, 
					//
					//==========================================================================//

					//TEST NEGATIVO
					//
					//Se elimina el article4 incorrectamente porque solo lo puede eliminar el admin
					{"user1", "article4", IllegalArgumentException.class},
					//Se elimina el article1 incorrectamente porque su newspaper ya esta publicado
					{"customer1", "article1", IllegalArgumentException.class}
			};
			for (int i = 0; i < testingData.length; i++)
				this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
		}
		private void templateDelete(final String username, final int articleId, final Class<?> expected) {
			Article article;
			Class<?> caught;

			caught = null;
			try {
				super.authenticate(username);
				article = this.articleService.findOne(articleId);
				this.articleService.delete(article);

				this.articleService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
			}

			this.checkExceptions(expected, caught);

			super.unauthenticate();
		}
	
	
	
	@Test
	public void driverSearchArticle(){
		Object testingData[][] = {
								//Requisitos C 6.4: Search for a published article using a single key word that must appear somewhere
								//in its title, summary, or body.
								//==========================================================================//

								//Tests POSITIVOS 
								//
								//Un test en el que buscamos messi, hay un artículo sobre messi así que debe devolverlo 
								{"user1", "messi", null},
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
			templateSearchArticle(((String) testingData[i][0]), (String) testingData[i][1],  (Class<?>) testingData[i][2]);
			this.rollbackTransaction();
		}
	}

	protected void templateSearchArticle(String username, String filter, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try{
			super.authenticate(username);
			Collection<Article> res = new ArrayList<Article>();
			res = this.articleService.findByFilter(filter);
			Assert.isTrue(!res.isEmpty());
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
		
	}
		
	}

