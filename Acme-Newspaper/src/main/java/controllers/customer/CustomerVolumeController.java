
package controllers.customer;

import java.util.Collection;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Customer;
import domain.Newspaper;
import domain.Volume;

import services.CustomerService;
import services.VolumeService;

@Controller
@RequestMapping("/volume/customer")
public class CustomerVolumeController extends AbstractController{

	// Services

	@Autowired
	private VolumeService	volumeService;
	
	@Autowired
	private CustomerService	customerService;


	// Constructors

	public CustomerVolumeController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String filter) {
		ModelAndView result;
		Collection<Volume> volumes;
		final Customer principal = this.customerService.findByPrincipal();
		Boolean suscrito = false;
		final String uri = "/customer";
		volumes = this.volumeService.findAll();
		
		for(Volume vol: volumes){
			if(principal.getVolumesSubscribed().contains(vol)){
				suscrito = true;
			}else{
				suscrito = false;
			}
		}
		
		result = new ModelAndView("volume/list");
		result.addObject("volumes", volumes);
		result.addObject("suscrito", suscrito);
		result.addObject("principal",principal);
		result.addObject("uri", uri);
		return result;
	}	

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId) {
		final ModelAndView result;
		Volume volume;
		Collection<Newspaper>newspapers;
		Boolean suscrito = false;
		final Customer principal = this.customerService.findByPrincipal();
		final String uri = "/customer";

		volume = this.volumeService.findOne(volumeId);
		newspapers = volume.getNewspapers();		
		
		if(principal.getVolumesSubscribed().contains(volume)){
			suscrito = true;
		}else{
			suscrito = false;
		}
		


		result = new ModelAndView("volume/display");
		result.addObject("newspapers", newspapers);
		result.addObject("suscrito", suscrito);
		result.addObject("volume", volume);
		result.addObject("uri", uri);
		result.addObject("principal", principal);
		return result;

	}
}
