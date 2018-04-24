
package controllers;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
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
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article;
		final String uri = "";

		article = this.articleService.findOne(articleId);
		
		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("uri", uri);
		result.addObject("principal", null);
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
