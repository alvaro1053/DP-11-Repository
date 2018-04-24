package controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.FollowUp;

import services.FollowUpService;


@Controller
@RequestMapping("/followUp")
public class FollowUpController {
	
	@Autowired
	FollowUpService followUpService;
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int followUpId, RedirectAttributes red){
		ModelAndView result;
		FollowUp followUp;
		Date now;
		followUp = this.followUpService.findOne(followUpId);
		now = new Date();
	
		
		if(followUp.getPublicationDate().compareTo(now) >= 0){
			result = new ModelAndView("followUp/display");
			result.addObject("followUp", followUp);
		}else{
			result = new ModelAndView("redirect:../article/list.do");
			red.addFlashAttribute("message", "master.page.followUp.restricted");
		}
		
		return result;
	}

}
