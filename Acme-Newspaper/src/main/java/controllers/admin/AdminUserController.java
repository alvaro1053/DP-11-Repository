
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.AdminService;
import services.UserService;
import controllers.AbstractController;
import domain.User;

@Controller
@RequestMapping("/user/admin")
public class AdminUserController extends AbstractController {

	//Autowired
	@Autowired
	UserService		userService;

	@Autowired
	AdminService	adminService;


	//Constructor
	public AdminUserController() {
		super();
	}

	//list
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final String uri = "/admin";
		ModelAndView result;
		Collection<User> users;

		users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("uri", uri);
		result.addObject("users", users);
		return result;
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, RedirectAttributes redir) {
		ModelAndView result;
		User user;
		final String uri = "/admin";

		try {
		user = this.userService.findOne(userId);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("principal", null);
		result.addObject("uri", uri);
		} catch (Throwable oops){
			result = new ModelAndView("redirect:/user/list.do");	
			redir.addFlashAttribute("message", "article.permission");
		}
		return result;

	}
}
