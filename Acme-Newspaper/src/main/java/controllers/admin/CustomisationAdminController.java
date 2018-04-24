package controllers.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Customisation;

import services.CustomisationService;


@Controller
@RequestMapping("/customisation/admin")
public class CustomisationAdminController {

	@Autowired
	CustomisationService customisationService;
	
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(){
		ModelAndView result;
		Customisation customisation;
		
		
		customisation = this.customisationService.findCustomisation();
		
		result = new ModelAndView("customisation/display");
		result.addObject("customisation", customisation);
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method= RequestMethod.GET)
	public ModelAndView edit(){
		ModelAndView result;
		Customisation customisation;
		
		customisation = this.customisationService.findCustomisation();
		
		result = this.createEditModelAndView(customisation);
		
		
		return result;	
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid Customisation customisation, BindingResult binding){
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(customisation);
		}else{
			try{
				this.customisationService.save(customisation);
				result = new ModelAndView("redirect:display.do");
			}catch(Throwable oops){
				result = this.createEditModelAndView(customisation, "customisation.commit.error");
			}
		}
		
		return result;
	}

	public ModelAndView createEditModelAndView(Customisation customisation, String message) {
		ModelAndView result;
		
		result = new ModelAndView("customisation/edit");
		result.addObject("customisation", customisation);
		result.addObject("message", message);
		
		return result;
	}

	protected ModelAndView createEditModelAndView(Customisation customisation) {
		ModelAndView result;
		
		
		result = this.createEditModelAndView(customisation, null);
		
		
		return result;
	}
	
	
	
}
