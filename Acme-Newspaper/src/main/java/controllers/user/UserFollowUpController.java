package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Article;
import domain.FollowUp;
import domain.User;


import services.ArticleService;
import services.FollowUpService;
import services.UserService;

@Controller
@RequestMapping("/followUp/user")
public class UserFollowUpController {
	
	@Autowired
	FollowUpService followUpService;
	
	@Autowired
	ArticleService	articleService;
	
	@Autowired
	UserService		userService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView result;
		Collection<Article> articles;
		User principal;
		
		principal = this.userService.findByPrincipal();
		articles = this.articleService.articlesPublishedByUser(principal.getId());
		
		result = new ModelAndView("followUp/list");
		result.addObject("articles", articles);
		
		
		return result;
	}
	
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		FollowUp followUp;
		Collection<Article> articles;

		articles = this.articleService.articlesPublished();
		followUp = this.followUpService.create();
		
		result = this.createEditModelAndView(followUp);
		result.addObject("followUp", followUp);
		result.addObject("articles", articles);
		
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid FollowUp followUp, BindingResult binding){
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(followUp);
		}else{
			try{
				this.followUpService.save(followUp);
				result = new ModelAndView("redirect:../../article/user/display.do?articleId="+followUp.getArticle().getId());
			}catch(Throwable oops){
				result = this.createEditModelAndView(followUp, "followUp.commit.error");
			}
		}
		return result;
	}



	protected ModelAndView createEditModelAndView(FollowUp followUp) {
		ModelAndView result; 
		
		result = this.createEditModelAndView(followUp, null);
		
		return result;
	}


	protected ModelAndView createEditModelAndView(FollowUp followUp, String message) {
		ModelAndView result; 
		Collection<Article> articles;
		User principal;
		
		principal = this.userService.findByPrincipal();
		articles = this.articleService.articlesPublishedByUser(principal.getId());
		
		result = new ModelAndView("followUp/create");
		result.addObject("message", message);
		result.addObject("articles", articles);
		
		
		return result;
	}
	
}
