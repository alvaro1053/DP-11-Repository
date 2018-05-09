
package controllers.customer;

import java.util.Collection;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Advertisement;
import domain.Article;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;

import services.ArticleService;
import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;

@Controller
@RequestMapping("/newspaper/customer")
public class CustomerNewspaperController extends AbstractController{

	// Services

	@Autowired
	private NewspaperService	newspaperService;
	
	@Autowired
	private ArticleService	articleService;
	
	@Autowired
	private SubscriptionService	subscriptionService;
	
	@Autowired
	private CustomerService	customerService;


	// Constructors

	public CustomerNewspaperController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String filter) {
		ModelAndView result;
		Collection<Newspaper> newspapers;
		Collection<Subscription> subscritions;
		final Customer principal = this.customerService.findByPrincipal();
		Boolean suscrito = false;
		final String uri = "/customer";
		newspapers = this.newspaperService.findByFilter(filter);
		subscritions = this.subscriptionService.findAll();

		
		for(Subscription subs: subscritions){
			if(principal.getSubscriptions().contains(subs)){
				suscrito = true;
			}else{
				suscrito = false;
			}
		}
		
		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("suscrito", suscrito);
		result.addObject("principal",principal);
		result.addObject("uri", uri);
		return result;
	}	

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView result;
		Newspaper newspaper;
		Collection<Subscription> subscritions;
		Collection<Article>articles;
		Boolean suscrito = false;
		Advertisement advertChoosen;
		final Customer principal = this.customerService.findByPrincipal();
		final String uri = "/customer";

		newspaper = this.newspaperService.findOne(newspaperId);
		articles = this.articleService.articlesOfNewspaper(newspaperId);
		subscritions = this.subscriptionService.findAll();
		
		
		for(Subscription subs: subscritions){
			if(principal.getSubscriptions().contains(subs)){
				suscrito = true;
			}else{
				suscrito = false;
			}
		}
		
		advertChoosen = this.newspaperService.findRandomAdvert(newspaper);

		result = new ModelAndView("newspaper/display");
		result.addObject("articles", articles);
		result.addObject("suscrito", suscrito);
		result.addObject("newspaper", newspaper);
		result.addObject("uri", uri);
		result.addObject("principal", principal);
		result.addObject("advert", advertChoosen);
		
		return result;

	}
}
