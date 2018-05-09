package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Agent;
import forms.EditActorForm;

import services.AgentService;

@Controller
@RequestMapping("agent/agent")
public class AgentAgentController {

	
	@Autowired
	AgentService agentService;
	
	//Display Personal profile
		@RequestMapping(value = "/displayProfile", method = RequestMethod.GET)
		public ModelAndView displayPersonalProfile() {
			ModelAndView result;
			Agent principal;

			principal = this.agentService.findByPrincipal();


			result = new ModelAndView("agent/display");
			result.addObject("agent", principal);
			result.addObject("principal", principal);

			return result;
		}
		
		@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
		public ModelAndView showEditPersonalProfile(){
			ModelAndView result;
			Agent agent;
			EditActorForm editActorForm;
			editActorForm = new EditActorForm();
			
			agent = this.agentService.findByPrincipal();
			
			editActorForm = this.agentService.construct(agent, editActorForm);
			
			result = this.createEditModelAndView(editActorForm);
			
			return result;
		}
		


		@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
		public ModelAndView edit(final EditActorForm editActorForm, BindingResult binding){
			ModelAndView result;
			Agent agent;
			
			if(!editActorForm.getName().isEmpty() && !editActorForm.getSurname().isEmpty() && !editActorForm.getEmail().isEmpty())
				agent = this.agentService.reconstruct(editActorForm, binding);
			else{
				result = this.createEditModelAndView(editActorForm, "agent.commit.error");
				return result;
			}
				
			
			agent = this.agentService.reconstruct(editActorForm, binding);
			
			if(binding.hasErrors()){
				result = this.createEditModelAndView(editActorForm);
			}else{
				try{
					this.agentService.save(agent);
					result = new ModelAndView("redirect:/agent/agent/displayProfile.do");
				}catch (Throwable oops){
					result = this.createEditModelAndView(editActorForm);
				}
			}
			
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
			
			requestURI = "agent/agent/editProfile.do";
			
			result = new ModelAndView("user/edit");
			result.addObject("editActorForm", editActorForm);
			result.addObject("message", message);
			result.addObject("requestURI", requestURI);
			
			return result;
		}

}
