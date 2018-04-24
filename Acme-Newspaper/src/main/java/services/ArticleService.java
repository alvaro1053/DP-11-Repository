package services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArticleRepository;
import domain.Actor;
import domain.Admin;
import domain.Article;
import domain.Customer;
import domain.Newspaper;

import domain.User;
import forms.ArticleForm;

@Service
@Transactional
public class ArticleService {

	//Managed Repository ----
	@Autowired
	private ArticleRepository	articleRepository;
	//Services
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private Validator			validator;
	
	@Autowired
	private CustomisationService customisationService;
	
	

	//Constructors
	public ArticleService() {
		super();
	}

	public Article create() {
		User principal;
		Article article;
		
		Date moment;

		principal = userService.findByPrincipal();
		Assert.notNull(principal);

		moment = new Date(System.currentTimeMillis() - 1);
		
		article = new Article();
		article.setMoment(moment);
	
		return article;
	}
	
	public ArticleForm createForm() {
		User principal;
		ArticleForm articleForm;

		principal = this.userService.findByPrincipal();
		Assert.notNull(principal);
		articleForm = new ArticleForm();
		articleForm.setIsDraft(true);

		return articleForm;
	}
	
	public Collection<Article> findAll() {
		Collection<Article> result;

		result = this.articleRepository.findAll();

		return result;
	}

	public void delete(final Article article) {
		Admin admin;
		Collection<Article> updated, updated2;
		Assert.notNull(article);
		
		admin = this.adminService.findByPrincipal();
		Assert.notNull(admin);

		final Newspaper newspaper = article.getNewspaper();
		final Collection<Article> article1 = newspaper.getArticles();
		updated = new ArrayList<Article>(article1);
		updated.remove(article);
		newspaper.setArticles(updated);

		final User user = article.getUser();
		final Collection<Article> article2 = user.getArticles();
		updated2 = new ArrayList<Article>(article2);
		updated2.remove(article);
		user.setArticles(updated2);

		
		this.articleRepository.delete(article);

	}
	
	public void deleteUpdate(final Article article) {
		Collection<Article> updated, updated2;
		Assert.notNull(article);

		final Newspaper newspaper = article.getNewspaper();
		final Collection<Article> article1 = newspaper.getArticles();
		updated = new ArrayList<Article>(article1);
		updated.remove(article);
		newspaper.setArticles(updated);

		final User user = article.getUser();
		final Collection<Article> article2 = user.getArticles();
		updated2 = new ArrayList<Article>(article2);
		updated2.remove(article);
		user.setArticles(updated2);

		this.articleRepository.delete(article);

	}

	public void save(final Article article) {		
		Article result;
        final Date momentNow = new Date(System.currentTimeMillis());

		User principal = userService.findByPrincipal();
		Collection<String> tabooWords;
		Assert.notNull(principal);
		
		article.setMoment(new Date(System.currentTimeMillis() - 1));
			
	
		if(article.getId() != 0) { this.deleteUpdate(article); }


		tabooWords = this.customisationService.findCustomisation().getTabooWords();
		for (String word : tabooWords) {
			if(article.getTitle().toLowerCase().contains(word))
				article.setTabooWords(true);
			if(article.getSummary().toLowerCase().contains(word))
				article.setTabooWords(true);
			if(article.getBody().toLowerCase().contains(word))
				article.setTabooWords(true);
		}
		
		result = this.articleRepository.save(article);
		
		final Collection<Article> update = principal.getArticles();
		update.add(result);
		principal.setArticles(update);

		final Newspaper newspaper = result.getNewspaper();
		final Collection<Article> update2 = newspaper.getArticles();
		update2.add(result);
		newspaper.setArticles(update2);
		
		
		Assert.isTrue(article.getNewspaper().getPublicationDate().after(momentNow));
	}

	public Article findOne(final int articleId) {
		Article result;

		result = this.articleRepository.findOne(articleId);
		Assert.notNull(result);

		return result;
	}
	
	public Collection<Article> articlesPublishedByUser(final int userId){
		Collection<Article> result;
		
		result = this.articleRepository.articlesPublishedByUser(userId);
		Assert.notNull(result);
		
		return result;
	}

	public Collection<Article> findByFilter(final String filter) {

		Collection<Article> articles = new HashSet<Article>();
		Actor principal = this.actorService.findByPrincipal();
		if((principal instanceof User) && (filter == ""|| filter== null)){
			User user = (User) principal;
			articles = new HashSet<Article>(this.articleRepository.articlesPublished());
			articles.addAll(user.getArticles());
		} else if ((principal instanceof Admin) && (filter == ""|| filter== null)){
			articles = this.findAll();
		} else if ((principal instanceof Customer || principal == null) && (filter == ""|| filter== null)){
			articles = this.articleRepository.articlesPublished();
		} else {
			articles = this.articleRepository.findByFilter(filter);
		}
		
		return articles;
	}
	
	public Collection<Article> articlesOfNewspaper(int newspaperId){
		Collection<Article> result;
		
		result = this.articleRepository.articlesOfNewspaper(newspaperId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Article reconstruct(ArticleForm articleForm, BindingResult binding) {
		Article result = new Article();
		User principal;
		
		principal = this.userService.findByPrincipal();
		
		result.setId(articleForm.getId());
		result.setVersion(articleForm.getVersion());
		result.setTitle(articleForm.getTitle());
		result.setMoment(articleForm.getMoment());
		result.setSummary(articleForm.getSummary());
		result.setBody(articleForm.getBody());
		result.setPhotosURL(articleForm.getPhotosURL());
		result.setIsDraft(articleForm.getIsDraft());
		result.setNewspaper(articleForm.getNewspaper());
		result.setTabooWords(false);
		result.setFollowUps(null);		
		result.setUser(principal);
		
		this.validator.validate(result, binding);
		
		return result;
	}
		
	public Collection<Article> findArticlesWithTabooWords(){
		Collection<Article> result;
		Admin admin = this.adminService.findByPrincipal();
		Assert.notNull(admin);
		
		result = this.articleRepository.findArticlesWithTabooWords();
		Assert.notNull(result);
		
		return result;
	}
	
	public ArticleForm reconstructForm(final Article article) {
		ArticleForm result;

		result = new ArticleForm();

		result.setId(article.getId());
		result.setVersion(article.getVersion());
		result.setTitle(article.getTitle());
		result.setSummary(article.getSummary());
		result.setBody(article.getBody());
		result.setMoment(article.getMoment());
		result.setPhotosURL(article.getPhotosURL());
		result.setIsDraft(article.getIsDraft());
		result.setNewspaper(article.getNewspaper());

		return result;
	}

	public void flush() {
		this.articleRepository.flush();
	}
}