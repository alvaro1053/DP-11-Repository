
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.UserService;
import domain.Article;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	//Autowired
	@Autowired
	UserService		userService;

	@Autowired
	ArticleService	articleService;


	//Constructor
	public UserController() {
		super();
	}

	//list
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final String uri = "";
		ModelAndView result;
		Collection<User> users;
	

		users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("uri", uri);

		
		return result;
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		final ModelAndView result;
		User user;
		final String uri = "article/display.do";
		final String requestURI = "user/display.do";
		Collection<Article> articles;
		Collection<Chirp> chirps;

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
		return result;

	}

}
