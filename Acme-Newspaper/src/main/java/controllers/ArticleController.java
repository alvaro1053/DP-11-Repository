
package controllers;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.ArticleService;
import domain.Advertisement;
import domain.Article;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	//Autowired
	@Autowired
	ArticleService	articleService;


	//Constructor
	public ArticleController() {
		super();
	}



	//Display
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId,  RedirectAttributes redir) {
		ModelAndView result;
		Article article = new Article();
		final String uri = "";
		Advertisement advertChoosen = null;
		try{
		
		article = this.articleService.findOne(articleId);
	
		
		advertChoosen = this.articleService.findRandomAdvert(article);
		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("uri", uri);
		result.addObject("principal", null);
		result.addObject("advert", advertChoosen);
		} catch (Throwable oops){
		result = new ModelAndView("redirect:/article/list.do");	
		redir.addFlashAttribute("message", "article.permission");
		}
		
		return result;

	}
	
	// Listing ----------------------------------------------------------------

		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView list(final String filter) {
			ModelAndView result;
			Collection<Article> articles;

			articles = this.articleService.findByFilter(filter);
			result = new ModelAndView("article/list");
			result.addObject("articles", articles);

			return result;

		}
}
