package services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import services.UserService;

import repositories.NewspaperRepository;
import domain.Actor;
import domain.Admin;
import domain.Article;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.User;
import domain.Volume;
import forms.NewspaperForm;

@Service
@Transactional
public class NewspaperService {

	//Managed Repository ----
	@Autowired
	private NewspaperRepository	newspaperRepository;
	//Services
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ActorService actorService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private SubscriptionService subcriptionService;
	@Autowired
	private Validator validator;
	@Autowired
	private CustomisationService customisationService;
	

	//Constructors
	public NewspaperService() {
		super();
	}

	public Newspaper create() {
		Newspaper result;
		User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);
		result = new Newspaper();
		result.setUser(principal);
		result.setArticles(new ArrayList<Article>());
		result.setSubscriptions(new ArrayList<Subscription>());
		return result;
	}

	public Collection<Newspaper> findAll() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findAll();

		return result;
	}
	
	public Collection<Newspaper> findByFilter(final String filter) {
		Actor actor = this.actorService.findByPrincipal();
		Collection<Newspaper> newspapers = new ArrayList<Newspaper>();
		if(actor == null && filter == ""|| filter== null){
			newspapers = this.newspaperRepository.publishedNewspapers();
		}else if(actor instanceof Admin && filter == ""|| filter== null){
			newspapers = this.newspaperRepository.findAll();
		}else if(actor instanceof User && filter == ""|| filter== null){
			newspapers = this.newspaperRepository.findByFilter(filter);
		}else if(actor instanceof Customer && filter == ""|| filter== null){
			newspapers = this.newspaperRepository.publishedNewspapers();
		}else{
			newspapers = this.newspaperRepository.findByFilterPublished(filter);
		}
		return newspapers;
	}

	public void delete(final Newspaper newspaper) {
		Collection<Newspaper>updated2;
		Admin principal = adminService.findByPrincipal();
		Assert.notNull(principal);
		
		User creator = newspaper.getUser();
		Collection<Newspaper> creatorsNewspapers = creator.getNewspapers();
		creatorsNewspapers.remove(newspaper);
		creator.setNewspapers(creatorsNewspapers);
		
		Collection<Subscription> subs = new ArrayList<Subscription>(newspaper.getSubscriptions());
		
		for(Subscription s : subs){
			this.subcriptionService.delete(s);
		}	
		
		Collection<Volume> volumes = newspaper.getVolumen();
		for(Volume v : volumes){
			Collection<Newspaper> newspapers = v.getNewspapers();

			if(newspapers.contains(newspaper)){
				updated2 = new ArrayList<Newspaper>(newspapers);
				updated2.remove(newspaper);
				v.setNewspapers(updated2);
			}
		}
		this.newspaperRepository.delete(newspaper);

	}

	public Newspaper save(final Newspaper newspaper) {
		Newspaper result;
		Collection<String> tabooWords;
		User principal = userService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(newspaper.getUser().equals(principal));
		
		if(newspaper.getPublicationDate() == null){
			newspaper.setPublicationDate(new Date(System.currentTimeMillis() - 1));
		}
		
		tabooWords = this.customisationService.findCustomisation().getTabooWords();
		for (String word : tabooWords) {
			if(newspaper.getTitle().toLowerCase().contains(word))
				newspaper.setTabooWords(true);
			if(newspaper.getDescription().toLowerCase().contains(word))
				newspaper.setTabooWords(true);
		}
		
		result = this.newspaperRepository.save(newspaper);
		
		Collection<Newspaper> creatorsNewspapers = principal.getNewspapers();
		creatorsNewspapers.add(result);
		principal.setNewspapers(creatorsNewspapers);
		
	
		return result;
	}

	public Newspaper findOne(final int newspaperId) {
		Newspaper result;

		result = this.newspaperRepository.findOne(newspaperId);
		Assert.notNull(result);

		return result;
	}
	

	public Collection<Newspaper> publishedNewspapers(){
		Collection<Newspaper>result;
		
		result = this.newspaperRepository.publishedNewspapers();
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Newspaper> notPublishedNewspapers(){
		Collection<Newspaper>result;
		
		result = this.newspaperRepository.notPublishedNewspapers();
		Assert.notNull(result);
		
		return result;
	}
	
	public void publish (Newspaper newspaper){
		User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.getNewspapers().contains(newspaper));
		Date now = new Date(System.currentTimeMillis());
		Assert.isTrue(newspaper.getPublicationDate().after(now));
		for (Article a : newspaper.getArticles()){
			Assert.isTrue(a.getIsDraft() == false);
		}
		newspaper.setPublicationDate(new Date(System.currentTimeMillis() - 1));
	}
	
	public Newspaper reconstruct (NewspaperForm newspaperForm, BindingResult binding){
		Newspaper newspaper = this.create();
		
		newspaper.setTitle(newspaperForm.getTitle());
		newspaper.setDescription(newspaperForm.getDescription());
		newspaper.setPictureURL(newspaperForm.getPictureURL());
		newspaper.setIsPrivate(newspaperForm.getIsPrivate());
		newspaper.setId(newspaperForm.getId());
		newspaper.setVersion(newspaperForm.getVersion());
		newspaper.setPublicationDate(newspaperForm.getPublicationDate());
		newspaper.setTabooWords(false);
		
		validator.validate(newspaperForm, binding);
		return newspaper;
	}

	public Collection<Newspaper> findNewspapersWithTabooWords() {
		Collection<Newspaper> result;
		Admin admin = this.adminService.findByPrincipal();
		Assert.notNull(admin);
		
		result = this.newspaperRepository.findNewspapersWithTabooWords();
		
		return result;
	}
	

	public void changePrivacity (int newspaperId){
		User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Newspaper newspaper = this.findOne(newspaperId);
		Assert.isTrue(user.getNewspapers().contains(newspaper));
		if(newspaper.getIsPrivate() == true){
			newspaper.setIsPrivate(false);
		} else{
			newspaper.setIsPrivate(true);
		}
	}

	public void flush() {
		this.newspaperRepository.flush();
	}

	public Collection<Newspaper> findPlacedAdsByAgent(int agentId) {
		Collection<Newspaper> result;
		
		result = this.newspaperRepository.findPlacedAdsByAgent(agentId);
		
		return result;
	}

	public Collection<Newspaper> findNotPlacedAdsByAgent(int agentId) {
		Collection<Newspaper> result;
		
		result = this.newspaperRepository.findNotPlacedAdsByAgent(agentId);
		
		return result;
	}

	public Collection <Newspaper> selectSubscribedNewspapers (){
		Customer principal = this.customerService.findByPrincipal();
		Collection<Newspaper> res = this.newspaperRepository.selectSubscribedNewspapers(principal.getId());
		return res;
		
	}

}