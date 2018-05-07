
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.ArticleService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Chirp;
import domain.User;
import forms.EditActorForm;

@Controller
@RequestMapping("/user/user")
public class UserUserController extends AbstractController {

	//Autowired
	@Autowired
	UserService		userService;

	@Autowired
	ArticleService	articleService;


	//Constructor
	public UserUserController() {
		super();
	}

	//list
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final String uri = "/user";
		ModelAndView result;
		final User principal = this.userService.findByPrincipal();
		Collection<User> users;
		users = this.userService.findAll();

		users.remove(principal);

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("principal", principal);
		result.addObject("uri", uri);
		return result;
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId, RedirectAttributes redir) {
		ModelAndView result;
		try{
		final User principal = this.userService.findByPrincipal();
		User user;
		String requestURI = "user/user/display.do";
		String uri = "/user";
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
		result.addObject("principal", principal);
		result.addObject("chirps", chirps);
		} catch(Throwable oops){
			result = new ModelAndView("redirect:../../");
			String successfulMessage = "user.commit.error";
			redir.addFlashAttribute("message", successfulMessage);
		}
		return result;

	}

	//Display Personal profile
	@RequestMapping(value = "/displayUserProfile", method = RequestMethod.GET)
	public ModelAndView displayPersonalProfile() {
		ModelAndView result;
		User principal;
		Collection<Article> articles;
		Collection<Chirp> chirps;
		String uri;
		String requestURI;
		
		requestURI = "user/user/displayUserProfile.do";
		uri = "/user";

		principal = this.userService.findByPrincipal();

		articles = this.articleService.articlesPublishedByUser(principal.getId());
		chirps = principal.getChirps();

		result = new ModelAndView("user/display");
		result.addObject("user", principal);
		result.addObject("principal", principal);
		result.addObject("uri", uri);
		result.addObject("requestURI", requestURI);
		result.addObject("articles", articles);
		result.addObject("chirps", chirps);

		return result;
	}
	
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView showEditPersonalProfile(){
		ModelAndView result;
		User user;
		EditActorForm editActorForm;
		editActorForm = new EditActorForm();
		
		user = this.userService.findByPrincipal();
		
		editActorForm = this.userService.construct(user, editActorForm);
		
		result = this.createEditModelAndView(editActorForm);
		
		return result;
	}
	


	@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final EditActorForm editActorForm, BindingResult binding){
		ModelAndView result;
		User user;
		
		user = this.userService.reconstruct(editActorForm, binding);
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(editActorForm);
		}else{
			try{
				this.userService.save(user);
				result = new ModelAndView("redirect:/user/user/displayUserProfile.do");
			}catch (Throwable oops){
				result = this.createEditModelAndView(editActorForm);
			}
		}
		
		return result;
	}

	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam final int userId, RedirectAttributes redir) {
		ModelAndView result;
		User principal;
		User userToBeFollowed;
		try{
		
		principal = this.userService.findByPrincipal();
		userToBeFollowed = this.userService.findOne(userId);

		if (userId != principal.getId()) {
			this.userService.follow(userToBeFollowed);
			result = new ModelAndView("redirect:/user/user/list.do");
		} else
			result = new ModelAndView("redirect:/user/user/list.do");
		} catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			String successfulMessage = "user.commit.error";
			redir.addFlashAttribute("message", successfulMessage);
		}
		return result;
	}

	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public ModelAndView unfollow(@RequestParam final int userId, RedirectAttributes redir) {
		ModelAndView result;
		User principal;
		User userToBeUnfollowed;
		try{
		principal = this.userService.findByPrincipal();
		userToBeUnfollowed = this.userService.findOne(userId);

		if (userId != principal.getId()) {
			this.userService.unfollow(userToBeUnfollowed);
			result = new ModelAndView("redirect:/user/user/list.do");
		} else
			result = new ModelAndView("redirect:/user/user/list.do");
		} catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			String successfulMessage = "user.commit.error";
			redir.addFlashAttribute("message", successfulMessage);
		}
		return result;
	}

	@RequestMapping(value = "/followingList", method = RequestMethod.GET)
	public ModelAndView followingList() {
		ModelAndView result;
		Collection<User> users;
		User principal;
		String uri;
		Boolean followingTitle = true;

		uri = "/user";

		principal = this.userService.findByPrincipal();

		users = principal.getFollows();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("principal", principal);
		result.addObject("uri", uri);
		result.addObject("followingTitle", followingTitle);

		return result;
	}

	@RequestMapping(value = "/followersList", method = RequestMethod.GET)
	public ModelAndView followersList() {
		ModelAndView result;
		Collection<User> users;
		User principal;
		String uri;
		Boolean followersTitle = true;

		uri = "/user";

		principal = this.userService.findByPrincipal();

		users = principal.getFollowers();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("principal", principal);
		result.addObject("uri", uri);
		result.addObject("followersTitle", followersTitle);

		return result;
	}
	
	
	//Ancillary
	
	protected ModelAndView createEditModelAndView(EditActorForm editActorForm) {
		ModelAndView result;
		
		result = this.createEditModelAndView(editActorForm, null);
		
		return result;
	}

	protected ModelAndView createEditModelAndView(EditActorForm editActorForm, String message) {
		ModelAndView result;
		String requestURI;
		
		requestURI = "user/user/editProfile.do";
		
		result = new ModelAndView("user/edit");
		result.addObject("editActorForm", editActorForm);
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);
		
		return result;
	}

}
