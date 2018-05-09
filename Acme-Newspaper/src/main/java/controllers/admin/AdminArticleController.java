
package controllers.admin;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.AbstractController;

import services.AdminService;
import services.ArticleService;
import domain.Admin;
import domain.Advertisement;
import domain.Article;

@Controller
@RequestMapping("/article/admin")
public class AdminArticleController extends AbstractController {

	//Autowired
	@Autowired
	ArticleService	articleService;
	
	@Autowired
	AdminService	adminService;


	//Constructor
	public AdminArticleController() {
		super();
	}


	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(String filter) {
		ModelAndView result;
		Collection<Article> articles;
		Admin principal = this.adminService.findByPrincipal();
		final String uri = "/admin";
		
		articles = this.articleService.findByFilter(filter);
		
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("uri", uri);
		result.addObject("principal", principal);
		return result;
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId, RedirectAttributes redir) {
		ModelAndView result;
		Article article;
		Advertisement advertChoosen;
		Admin principal = this.adminService.findByPrincipal();
		final String uri = "/admin";

		try{
		article = this.articleService.findOne(articleId);

		advertChoosen = this.articleService.findRandomAdvert(article);
		
		
		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("uri", uri);
		result.addObject("principal", principal);
		result.addObject("advert", advertChoosen);

		} catch (Throwable oops){
			result = new ModelAndView("redirect:/article/admin/list.do");	
			redir.addFlashAttribute("message", "article.permission");
		}
		return result;

	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int articleId, RedirectAttributes redir) {
	ModelAndView result;
	Article article;
	
	article = this.articleService.findOne(articleId);

	try {
		this.articleService.delete(article);
		result = new ModelAndView("redirect:../list.do");
		String successfulMessage = "article.commit.ok";
		redir.addFlashAttribute("message", successfulMessage);
	} catch (Throwable oops) {
		result = new ModelAndView("redirect:../list.do");
		String errorMessage = "article.commit.error";
		redir.addFlashAttribute("message", errorMessage);
	}

	return result;
}
	

}
