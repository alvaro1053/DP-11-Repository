
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

import domain.CreditCard;
import domain.Newspaper;
import domain.Subscription;
import domain.Volume;
import forms.SubscriptionForm;
import forms.SubscriptionVolumeForm;

import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;
import services.VolumeService;


@Controller
@RequestMapping("/subscription/customer")
public class CustomerSubscriptionController extends AbstractController{


	// Services
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private VolumeService	volumeService;
	
	
	@Autowired
	private CustomerService	customerService;
	
	@Autowired
	private SubscriptionService	subscriptionService;



	//Constructors
	public CustomerSubscriptionController() {
		super();
	}

	//Creation
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int newspaperId, RedirectAttributes redir) {
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
	
	
	//Creation for Volume
	
		@RequestMapping(value = "/createVolume", method = RequestMethod.GET)
		public ModelAndView createVolumeSubscription(@RequestParam final int volumeId, RedirectAttributes redir) {
			ModelAndView result;
			SubscriptionVolumeForm subscription = new SubscriptionVolumeForm();
			try{
			this.customerService.findByPrincipal();
			Volume volume = this.volumeService.findOne(volumeId);
			subscription.setVolume(volume);
			
			result = createEditModelAndViewOfVolume(subscription);
			}catch(Throwable oops){
				result = new ModelAndView("redirect:../../volume/list.do");
				redir.addFlashAttribute("message", "volume.permision");
			}
			
			

			return result;
		}
	
		//Edit
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SubscriptionForm subscriptionForm, final BindingResult binding) {
		ModelAndView result;
		Subscription subscription = this.subscriptionService.reconstruct(subscriptionForm, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(subscriptionForm);
		} else
			try {
				this.subscriptionService.save(subscription);
				result = new ModelAndView("redirect:../../newspaper/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(subscriptionForm, "v.commit.error");
			}

		return result;
	}
	
	//Edit of Volume
	
	@RequestMapping(value = "/editVolume", method = RequestMethod.POST, params = "save")
	public ModelAndView saveOfVolume(@Valid final SubscriptionVolumeForm subscriptionForm,
			final BindingResult binding) {
		ModelAndView result;
		this.subscriptionService.checkDate(subscriptionForm.getCreditCard(), binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndViewOfVolume(subscriptionForm);
		} else
			try {
				Volume volume = subscriptionForm.getVolume();
				CreditCard creditCard = subscriptionForm.getCreditCard();
				this.volumeService.subscribe(volume, creditCard);
				result = new ModelAndView("redirect:../../volume/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewOfVolume(subscriptionForm,
						"volume.commit.error");
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
		
		
		
		protected ModelAndView createEditModelAndViewOfVolume(final SubscriptionVolumeForm subscription) {
			ModelAndView result;

			result = this.createEditModelAndViewOfVolume(subscription, null);

			return result;
		}

		protected ModelAndView createEditModelAndViewOfVolume (final SubscriptionVolumeForm subscription, final String message) {
			ModelAndView result;

			result = new ModelAndView("subscription/subscribeVolume");
			result.addObject("subscriptionVolumeForm", subscription);
			result.addObject("message", message);

			return result;
		}

}
