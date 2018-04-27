package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Agent;

import forms.ActorForm;

import services.AgentService;


@Controller
@RequestMapping("/agent")
public class AgentRegisterController {
	
	@Autowired
	AgentService 	agentService;
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register(){
		ModelAndView result;
		ActorForm actorForm;
		
		actorForm = new ActorForm();
		
		result = this.createEditModelAndView(actorForm);
		result.addObject("permiso", true);
		
		
		return result;
	}

	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView save(ActorForm actorForm, BindingResult binding){
		ModelAndView result;
		
		Agent agent = this.agentService.create();
		agent = this.agentService.reconstruct(actorForm, binding);
		if(binding.hasErrors()){
			result = this.createEditModelAndView(actorForm);
			result.addObject("permiso", true);
		}else{
			try{
				this.agentService.save(agent);
				result = new ModelAndView("redirect:../");
			}catch(DataIntegrityViolationException error){
				binding.rejectValue("userAccount.username", "agent.username.error");
				result = this.createEditModelAndView(actorForm);
				result.addObject("permiso", true);
			}catch(Throwable oops){
				result = this.createEditModelAndView(actorForm, "agent.commit.error");
			}
		}	
		return result;
	}
	protected ModelAndView createEditModelAndView(ActorForm actorForm) {
		ModelAndView result;
		
		result = this.createEditModelAndView(actorForm, null);
		
		return result;
	}

	protected ModelAndView createEditModelAndView(ActorForm actorForm,
			String message) {
		ModelAndView result;
		
		result = new ModelAndView("agent/register");
		result.addObject("actorForm", actorForm);
		result.addObject("messsage", message);
		
		return result;
	}
	

}
