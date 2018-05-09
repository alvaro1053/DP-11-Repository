
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
import domain.Subscription;

import services.ArticleService;
import services.CustomerService;
import services.SubscriptionService;

@Controller
@RequestMapping("/article/customer")
public class CustomerArticleController extends AbstractController{

	// Services

	@Autowired
	private ArticleService	articleService;
	
	@Autowired
	private SubscriptionService	subscriptionService;
	
	@Autowired
	private CustomerService	customerService;


	// Constructors

	public CustomerArticleController() {
		super();
	}
	
	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String filter) {
		ModelAndView result;
		Collection<Article> articles;
		Collection<Subscription> subscritions;
		final Customer principal = this.customerService.findByPrincipal();
		Boolean suscrito = false;
		final String uri = "/customer";
		articles = this.articleService.findByFilter(filter);
		subscritions = this.subscriptionService.findAll();

			
		for(Subscription subs: subscritions){
			if(principal.getSubscriptions().contains(subs)){
				suscrito = true;
			}else{
				suscrito = false;
			}
		}
			
			result = new ModelAndView("article/list");
			result.addObject("articles", articles);
			result.addObject("suscrito", suscrito);
			result.addObject("principal",principal);
			result.addObject("uri", uri);
			return result;
		}	

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		Collection<Subscription> subscritions;
		Article article;
		Boolean suscrito = false;
		Advertisement advertChoosen = null;
		final Customer principal = this.customerService.findByPrincipal();
		final String uri = "/customer";

		article = this.articleService.findOne(articleId);
		subscritions = this.subscriptionService.findAll();
		advertChoosen = this.articleService.findRandomAdvert(article);
		
		for(Subscription subs: subscritions){
			if(principal.getSubscriptions().contains(subs)){
				suscrito = true;
			}else{
				suscrito = false;
			}
		}

		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("suscrito", suscrito);
		result.addObject("uri", uri);
		result.addObject("principal", principal);
		result.addObject("advert", advertChoosen);
		return result;

	}
}
