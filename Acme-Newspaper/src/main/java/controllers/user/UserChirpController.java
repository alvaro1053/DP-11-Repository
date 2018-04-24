package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.AbstractController;

import domain.Chirp;
import domain.User;

import forms.ChirpForm;


import services.ChirpService;
import services.UserService;

@Controller
@RequestMapping("/chirp/user")
public class UserChirpController extends AbstractController{
	
	@Autowired
	ChirpService chirpService;
	
	//Auxilliary servicies
	@Autowired
	UserService userService;
	
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		ChirpForm chirpForm;
		
		chirpForm = this.chirpService.create();
		
		result = this.createEditModelAndView(chirpForm);
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ChirpForm chirpForm, BindingResult binding,RedirectAttributes redir){
		ModelAndView result;
		Chirp chirp;
		
		chirp = this.chirpService.reconstruct(chirpForm, binding);
		if(binding.hasErrors()){
			result = this.createEditModelAndView(chirpForm);
		}else{
			try{
				this.chirpService.save(chirp);
				result = new ModelAndView("redirect:../../");
				String successfulMessage = "master.page.chirp.commit.ok";
				redir.addFlashAttribute("message", successfulMessage);
			}catch(Throwable oops){
				result = this.createEditModelAndView(chirpForm, "chirp.commit.error");
			}
		}
		
		
		return result;
		
	}
	
	@RequestMapping(value = "/followingChirps", method = RequestMethod.GET)
	public ModelAndView streamChirps(){
		ModelAndView result;
		User principal;
		Collection<Chirp> followingChirps;
		principal = this.userService.findByPrincipal();
		
		followingChirps = this.chirpService.findByUserFollowers(principal.getId());
		
		result = new ModelAndView("chirp/list");
		result.addObject("followingChirps", followingChirps);
				
		return result;
		
	}
	
	//Ancillary methods
	protected ModelAndView createEditModelAndView(ChirpForm chirpForm) {
		ModelAndView result; 
		
		result = this.createEditModelAndView(chirpForm, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(ChirpForm chirpForm,
			String message) {
		ModelAndView result;
		
		
		result = new ModelAndView("chirp/create");
		result.addObject("chirpForm", chirpForm);
		result.addObject("message", message);
	
		return result;
	}

}
