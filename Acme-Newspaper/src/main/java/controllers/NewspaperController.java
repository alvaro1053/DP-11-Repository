
package controllers;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Actor;
import domain.Advertisement;
import domain.Article;
import domain.Newspaper;

import services.ActorService;
import services.ArticleService;
import services.NewspaperService;


@Controller
@RequestMapping("/newspaper")
public class NewspaperController extends AbstractController {

	// Services

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService	articleService;
	
	@Autowired
	private ActorService	actorService;

	// Constructors

	public NewspaperController() {
		super();
	}

	//Display
		@RequestMapping(value = "/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int newspaperId, RedirectAttributes redir) {
			ModelAndView result;
			Newspaper newspaper;
			Collection<Article> articles;
			Advertisement advertChoosen;
			final String uri = "";

			try{
				
			
			newspaper = this.newspaperService.findOne(newspaperId);
			articles = this.articleService.articlesOfNewspaper(newspaperId);
			advertChoosen = this.newspaperService.findRandomAdvert(newspaper);

			result = new ModelAndView("newspaper/display");
			result.addObject("articles", articles);
			result.addObject("newspaper", newspaper);
			result.addObject("uri", uri);
			result.addObject("principal", null);
			result.addObject("advert", advertChoosen);
			}catch (Throwable oops){
				result = new ModelAndView("redirect:/newspaper/list.do");	
				redir.addFlashAttribute("message", "article.permission"); 
			}//da igual que sea de artículo, el mensaje es el mismo
			
			return result;

	}
		
	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String filter) {
		Actor principal = this.actorService.findByPrincipal();
		ModelAndView result;
		Collection<Newspaper> newspapers;
		final String uri = "";
		newspapers = this.newspaperService.findByFilter(filter);
		result = new ModelAndView("newspaper/list");
		if(principal != null){
			result.addObject("principal",principal);
		}
		result.addObject("newspapers", newspapers);
		result.addObject("uri", uri);
		return result;
	}	


}
