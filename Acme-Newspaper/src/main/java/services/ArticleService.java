
package services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArticleRepository;
import domain.Actor;
import domain.Admin;
import domain.Advertisement;
import domain.Article;
import domain.Customer;
import domain.FollowUp;
import domain.Newspaper;
import domain.User;
import forms.ArticleForm;

@Service
@Transactional
public class ArticleService {

	//Managed Repository ----
	@Autowired
	private ArticleRepository		articleRepository;
	//Services
	@Autowired
	private UserService				userService;

	@Autowired
	private AdminService			adminService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;

	@Autowired
	private CustomisationService	customisationService;


	//Constructors
	public ArticleService() {
		super();
	}

	public Article create() {
		User principal;
		Article article;

		Date moment;

		principal = this.userService.findByPrincipal();
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

	public void save(final Article article) {
		Article result;
		final Date momentNow = new Date(System.currentTimeMillis());

		final User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);

		article.setMoment(new Date(System.currentTimeMillis() - 1));

		Assert.isTrue(article.getNewspaper().getPublicationDate().after(momentNow));

		result = this.articleRepository.save(article);

		final Collection<Article> update = new HashSet<>(principal.getArticles());
		update.add(result);
		principal.setArticles(update);

	}

	public Article findOne(final int articleId) {
		Article result;

		result = this.articleRepository.findOne(articleId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Article> articlesPublishedByUser(final int userId) {
		Collection<Article> result;

		result = this.articleRepository.articlesPublishedByUser(userId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Article> findByFilter(final String filter) {

		Collection<Article> articles = new HashSet<Article>();
		final Actor principal = this.actorService.findByPrincipal();
		if ((principal instanceof User) && (filter == "" || filter == null)) {
			final User user = (User) principal;
			articles = new HashSet<Article>(this.articleRepository.articlesPublished());
			articles.addAll(user.getArticles());
		}else if ((principal instanceof User) && (filter != "" || filter != null)){
			articles = new HashSet<Article>(articles = this.articleRepository.findByFilter(filter));
			articles.addAll(this.articleRepository.findByFilterByUser(filter, principal.getId()));		
		} else if ((principal instanceof Admin) && (filter == "" || filter == null))
			articles = this.articleRepository.findAll();
		else if ((principal instanceof Customer || principal == null) && (filter == "" || filter == null))
			articles = this.articleRepository.articlesPublished();
		else
			articles = this.articleRepository.findByFilter(filter);

		return articles;
	}

	public Collection<Article> articlesOfNewspaper(final int newspaperId) {
		Collection<Article> result;

		result = this.articleRepository.articlesOfNewspaper(newspaperId);
		Assert.notNull(result);

		return result;
	}

	public Article reconstruct(final ArticleForm articleForm, final BindingResult binding) {
		final Article result = this.create();
		User principal;
		List<String> photos;
		Collection<String> tabooWords;

		principal = this.userService.findByPrincipal();
		if (articleForm.getPhotosURL() == null)
			photos = new ArrayList<>();
		else
			photos = new ArrayList<>(articleForm.getPhotosURL());

		result.setId(articleForm.getId());
		result.setVersion(articleForm.getVersion());
		result.setTitle(articleForm.getTitle());
		result.setMoment(articleForm.getMoment());
		result.setSummary(articleForm.getSummary());
		result.setBody(articleForm.getBody());
		result.setPhotosURL(photos);
		result.setIsDraft(articleForm.getIsDraft());
		result.setNewspaper(articleForm.getNewspaper());
		result.setTabooWords(false);
		result.setFollowUps(new ArrayList<FollowUp>());
		result.setUser(principal);

		tabooWords = this.customisationService.findCustomisation().getTabooWords();
		for (final String word : tabooWords) {
			if (result.getTitle().toLowerCase().contains(word))
				result.setTabooWords(true);
			if (result.getSummary().toLowerCase().contains(word))
				result.setTabooWords(true);
			if (result.getBody().toLowerCase().contains(word))
				result.setTabooWords(true);
		}

		this.validator.validate(result, binding);

		return result;
	}
	public Collection<Article> findArticlesWithTabooWords() {
		Collection<Article> result;
		final Admin admin = this.adminService.findByPrincipal();
		Assert.notNull(admin);

		result = this.articleRepository.findArticlesWithTabooWords();
		Assert.notNull(result);

		return result;
	}

	public ArticleForm reconstructForm(final Article article) {
		ArticleForm result;
		List<String> photos;

		result = this.createForm();
		photos = new ArrayList<>(article.getPhotosURL());

		result.setId(article.getId());
		result.setVersion(article.getVersion());
		result.setTitle(article.getTitle());
		result.setSummary(article.getSummary());
		result.setBody(article.getBody());
		result.setMoment(article.getMoment());
		result.setPhotosURL(photos);
		result.setIsDraft(article.getIsDraft());
		result.setNewspaper(article.getNewspaper());

		return result;
	}

	public void flush() {
		this.articleRepository.flush();
	}

	public Advertisement findRandomAdvert(final Article article) {
		Advertisement result = null;
		List<Advertisement> adverts = new ArrayList<Advertisement>();
		adverts = (List<Advertisement>) article.getNewspaper().getAdverts();

		if (adverts.size() >= 2) {
			int selectedOne;
			final int limit = adverts.size();
			final Random rand = new Random();
			selectedOne = rand.nextInt(limit);
			result = adverts.get(selectedOne);
		} else if (adverts.size() == 1)
			result = adverts.get(0);

		return result;
	}
	
	public Collection<Article> articlesPublished(){
		Collection<Article> result;
		
		result = this.articleRepository.articlesPublished();
		
		return result;
	}
}
