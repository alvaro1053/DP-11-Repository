package controllers.customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;
import domain.Customer;

import forms.EditActorForm;

@Controller
@RequestMapping("/customer/customer")
public class CustomerCustomerController {
	
	@Autowired
	CustomerService customerService;
	
	
	
	
	//Display Personal profile
	@RequestMapping(value = "/displayProfile", method = RequestMethod.GET)
	public ModelAndView displayPersonalProfile() {
		ModelAndView result;
		Customer principal;

		principal = this.customerService.findByPrincipal();


		result = new ModelAndView("customer/display");
		result.addObject("customer", principal);
		result.addObject("principal", principal);

		return result;
	}
	
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView showEditPersonalProfile(){
		ModelAndView result;
		Customer customer;
		EditActorForm editActorForm;
		editActorForm = new EditActorForm();
		
		customer = this.customerService.findByPrincipal();
		
		editActorForm = this.customerService.construct(customer, editActorForm);
		
		result = this.createEditModelAndView(editActorForm);
		
		return result;
	}
	


	@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final EditActorForm editActorForm, BindingResult binding){
		ModelAndView result;
		Customer customer;
		
		if(!editActorForm.getName().isEmpty() && !editActorForm.getSurname().isEmpty() && !editActorForm.getEmail().isEmpty())
			customer = this.customerService.reconstruct(editActorForm, binding);
		else{
			result = this.createEditModelAndView(editActorForm, "customer.commit.error");
			return result;
		}
			
		
		customer = this.customerService.reconstruct(editActorForm, binding);
		
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(editActorForm);
		}else{
			try{
				this.customerService.save(customer);
				result = new ModelAndView("redirect:/customer/customer/displayProfile.do");
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
		
		requestURI = "customer/customer/editProfile.do";
		
		result = new ModelAndView("user/edit");
		result.addObject("editActorForm", editActorForm);
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);
		
		return result;
	}


}
