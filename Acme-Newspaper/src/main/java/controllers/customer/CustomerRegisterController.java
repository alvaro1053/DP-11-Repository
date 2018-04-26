package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Customer;
import forms.ActorForm;

import services.CustomerService;


@Controller
@RequestMapping("/customer")
public class CustomerRegisterController {
	
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView result;
		
		final ActorForm actorForm = new ActorForm();
		result = this.createEditModelAndView(actorForm);
		result.addObject("permiso", true);
	
		return result;	
	}
	
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ActorForm actorForm, final BindingResult binding){
		ModelAndView result;
		Customer customer = this.customerService.create();
		customer = this.customerService.reconstruct(actorForm, binding);
		if(binding.hasErrors()){
			result = this.createEditModelAndView(actorForm);
			result.addObject("permiso", true);
		}else{
			try{
				this.customerService.save(customer);
				result = new ModelAndView("redirect:../");
			}catch(final DataIntegrityViolationException error){	
				binding.rejectValue("userAccount.username", "customer.username.error");
				result = this.createEditModelAndView(actorForm);
				result.addObject("permiso", true);
			}catch(final Throwable oops){
				result = this.createEditModelAndView(actorForm, "customer.commit.error");
			}
		}
		
		
		return result;	
	}


	
	
	
// Ancillary methods -------------------------------------------------------------
	protected ModelAndView createEditModelAndView(ActorForm actorForm) {
		ModelAndView result;
		
		result = this.createEditModelAndView(actorForm, null);
		
		return result;
	}


	protected ModelAndView createEditModelAndView(ActorForm actorForm,
			String message) {
		ModelAndView result;
		
		result = new ModelAndView("customer/register");
		result.addObject("actorForm", actorForm);
		result.addObject("message", message);
		
		return result;
	}
	

}
