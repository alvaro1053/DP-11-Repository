
package controllers;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Newspaper;
import domain.Volume;

import services.ActorService;
import services.VolumeService;


@Controller
@RequestMapping("/volume")
public class VolumeController extends AbstractController {

	// Services
	@Autowired
	private VolumeService	volumeService;
	
	@Autowired
	private ActorService	actorService;

	// Constructors

	public VolumeController() {
		super();
	}

	//Display
		@RequestMapping(value = "/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int volumeId) {
			final ModelAndView result;
			Volume volume;
			Collection<Newspaper> newspapers;
			final String uri = "";

			volume = this.volumeService.findOne(volumeId);
			newspapers = volume.getNewspapers();

			result = new ModelAndView("volume/display");
			result.addObject("volume", volume);
			result.addObject("newspapers", newspapers);
			result.addObject("uri", uri);
			result.addObject("principal", null);
			return result;

	}
		
	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String filter) {
		Actor principal = this.actorService.findByPrincipal();
		ModelAndView result;
		Collection<Volume> volumes;
		final String uri = "";
		volumes = this.volumeService.findAll();
		result = new ModelAndView("volume/list");
		if(principal != null){
			result.addObject("principal",principal);
		}
		result.addObject("volumes", volumes);
		result.addObject("uri", uri);
		return result;
	}	


}
