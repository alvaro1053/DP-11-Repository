
package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.AbstractController;

import services.ArticleService;
import services.UserService;
import domain.Article;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/user/customer")
public class CustomerUserController extends AbstractController {

	//Autowired
	@Autowired
	UserService		userService;

	@Autowired
	ArticleService	articleService;


	//Constructor
	public CustomerUserController() {
		super();
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, RedirectAttributes redir) {
		ModelAndView result;
		User user;
		final String uri = "/customer";
		final String requestURI = "user/display.do";
		Collection<Article> articles;
		Collection<Chirp> chirps;

		try{
		user = this.userService.findOne(userId);
		articles = this.articleService.articlesPublishedByUser(userId);
		chirps = user.getChirps();

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("uri", uri);
		result.addObject("requestURI", requestURI);
		result.addObject("articles", articles);
		result.addObject("principal", null);
		result.addObject("chirps", chirps);
		} catch (Throwable oops){
			result = new ModelAndView("redirect:/user/list.do");	
			redir.addFlashAttribute("message", "article.permission");
		}
		return result;

	}

}
