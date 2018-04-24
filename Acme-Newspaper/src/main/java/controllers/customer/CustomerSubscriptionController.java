
package controllers.customer;



import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.AbstractController;

import domain.Newspaper;
import domain.Subscription;
import forms.SubscriptionForm;

import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;


@Controller
@RequestMapping("/subscription/customer")
public class CustomerSubscriptionController extends AbstractController{

	// Services

	@Autowired
	private NewspaperService	newspaperService;
	
	@Autowired
	private CustomerService	customerService;
	
	@Autowired
	private SubscriptionService	subscriptionService;


	// Constructors

	public CustomerSubscriptionController() {
		super();
	}

	//Creation
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView filter(@RequestParam final int newspaperId, RedirectAttributes redir) {
		ModelAndView result;
		SubscriptionForm subscription = new SubscriptionForm();
		try{
		this.customerService.findByPrincipal();
		Newspaper newspaper = this.newspaperService.findOne(newspaperId);
		subscription.setNewspaper(newspaper);
		
		result = createEditModelAndView(subscription);
		}catch(Throwable oops){
			result = new ModelAndView("redirect:../../newspaper/list.do");
			redir.addFlashAttribute("message", "newspaper.permision");
		}
		
		

		return result;
	}
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SubscriptionForm subscriptionForm, final BindingResult binding) {
		ModelAndView result;
		Subscription subscription = this.subscriptionService.reconstruct(subscriptionForm, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(subscriptionForm);
		} else
			try {
				this.subscriptionService.save(subscription);
				result = new ModelAndView("redirect:../../newspaper/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(subscriptionForm, "subscription.commit.error");
			}

		return result;
	}
		
		
	// Ancillary methods ------------------------------------------------------

		protected ModelAndView createEditModelAndView(final SubscriptionForm subscription) {
			ModelAndView result;

			result = this.createEditModelAndView(subscription, null);

			return result;
		}

		protected ModelAndView createEditModelAndView(final SubscriptionForm subscription, final String message) {
			ModelAndView result;

			result = new ModelAndView("subscription/edit");
			result.addObject("subscriptionForm", subscription);
			result.addObject("message", message);

			return result;
		}

}
