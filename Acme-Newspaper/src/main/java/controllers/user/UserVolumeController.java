
package controllers.user;

import java.util.Calendar;
import java.util.Collection;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.AbstractController;

import domain.Newspaper;
import domain.User;
import domain.Volume;
import forms.VolumeForm;

import services.NewspaperService;
import services.UserService;
import services.VolumeService;


@Controller
@RequestMapping("/volume/user")
public class UserVolumeController extends AbstractController {

	// Services
	@Autowired
	private VolumeService	volumeService;
	
	@Autowired
	private NewspaperService	newspaperService;
	
	@Autowired
	private UserService	userService;

	// Constructors

	public UserVolumeController() {
		super();
	}

	//Display
		@RequestMapping(value = "/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int volumeId) {
			final ModelAndView result;
			Volume volume;
			Collection<Newspaper> newspapers;
			final String uri = "/user";

			volume = this.volumeService.findOne(volumeId);
			newspapers = volume.getNewspapers();

			result = new ModelAndView("volume/display");
			result.addObject("volume", volume);
			result.addObject("newspapers", newspapers);
			result.addObject("uri", uri);
			result.addObject("principal", null);
			return result;

	}
	
	// Creation ---------------------------------------------------------------

		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public ModelAndView create() {
			ModelAndView result;
			VolumeForm volume = new VolumeForm();
			Calendar cal= Calendar.getInstance();
			int year= cal.get(Calendar.YEAR);
			volume.setYear(year);

			result = this.createEditModelAndView(volume);

			return result;
			}
	// Edit -------------------------------------------------------------------
		
		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int volumeId,RedirectAttributes redir) {
			ModelAndView result;
			User principal;
			Volume volume;
			VolumeForm volumeForm;
			try{
			principal = this.userService.findByPrincipal();
			Assert.notNull(principal);

			volume = this.volumeService.findOne(volumeId);
			Assert.notNull(volume);
			volumeForm = this.volumeService.reconstructForm(volume);

			result = this.createEditModelAndView(volumeForm);

			} catch (Throwable oops){
			result = new ModelAndView("redirect:../../volume/list.do");
			redir.addFlashAttribute("message", "rende.permision");
			}

			return result;
		}
		
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView save(@Valid final VolumeForm volumeForm, final BindingResult binding) {
			ModelAndView result;
			Volume volume = this.volumeService.reconstruct(volumeForm,binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(volumeForm);
			else
				try {
					this.volumeService.save(volume);
					result = new ModelAndView("redirect:../../volume/list.do");
				} catch (final Throwable oops) {
					String errorMessage = "volume.commit.error";
					result = this.createEditModelAndView(volumeForm, errorMessage);
				}

			return result;
		}
		
		protected ModelAndView createEditModelAndView(final VolumeForm volumeForm) {
			ModelAndView result;

			result = this.createEditModelAndView(volumeForm, null);

			return result;
		}
		
		protected ModelAndView createEditModelAndView(final VolumeForm volumeForm, final String message) {
			ModelAndView result;
			Collection<Newspaper> newspapers;
			
			newspapers = this.newspaperService.publishedNewspapers();


			result = new ModelAndView("volume/edit");
			result.addObject("volumeForm", volumeForm);
			result.addObject("newspapers", newspapers);
			result.addObject("message", message);

			return result;
		}


}
