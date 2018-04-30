package controllers.agent;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;

import forms.AdvertisementForm;

import services.AdvertisementService;
import services.AgentService;
import services.NewspaperService;

@Controller
@RequestMapping("advertisement/agent")
public class AgentAdvertisementController {
	
	@Autowired
	AdvertisementService 	advertisementService;

	@Autowired
	NewspaperService		newspaperService;
	
	@Autowired
	AgentService			agentService;
	
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		AdvertisementForm advertisementForm;
		
		advertisementForm = new AdvertisementForm();
		
		result = this.createEditModelAndView(advertisementForm);
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final AdvertisementForm advertisementForm, BindingResult binding){
		ModelAndView result;
		Advertisement advertisement;
		
		advertisement = this.advertisementService.reconstruct(advertisementForm, binding);
		if(binding.hasErrors()){
			result = this.createEditModelAndView(advertisementForm);
		}else{
			try{
				this.advertisementService.save(advertisement);
				result = new ModelAndView("redirect:../../");
			}catch(Throwable oops){
				result = this.createEditModelAndView(advertisementForm, "advertisement.commit.error");
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/listPlacedAds", method = RequestMethod.GET)
	public ModelAndView listPlacedAds(){
		ModelAndView result;
		Collection<Newspaper> listPlacedAds;
		Agent principal;
		
		principal = this.agentService.findByPrincipal();
		listPlacedAds = this.newspaperService.findPlacedAdsByAgent(principal.getId());
		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", listPlacedAds);
		result.addObject("location", "newspaper.placedAds");
		
		return result;
	}
	
	@RequestMapping(value = "/listNotPlacedAds", method = RequestMethod.GET)
	public ModelAndView listNotPlacedAds(){
		ModelAndView result;
		Collection<Newspaper> listPlacedAds;
		Agent principal;
		
		principal = this.agentService.findByPrincipal();
		listPlacedAds = this.newspaperService.findNotPlacedAdsByAgent(principal.getId());
		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", listPlacedAds);
		result.addObject("location", "newspaper.NotPlacedAds");
		
		return result;
	}
	
	

	protected ModelAndView createEditModelAndView(AdvertisementForm advertisementForm) {
		ModelAndView result;
		
		result = this.createEditModelAndView(advertisementForm, null);
		
		return result;
	}

	protected ModelAndView createEditModelAndView(AdvertisementForm advertisementForm, String message) {
		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.publishedNewspapers();
		
		result = new ModelAndView("advertisement/edit");
		result.addObject("advertisementForm", advertisementForm);
		result.addObject("newspapers", newspapers);
		result.addObject("message", message);
		
		return result;
	}
}