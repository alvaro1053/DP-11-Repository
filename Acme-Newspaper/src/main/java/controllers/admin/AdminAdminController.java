package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Advertisement;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;

import services.AdvertisementService;
import services.ArticleService;
import services.ChirpService;
import services.NewspaperService;


@Controller
@RequestMapping("/admin/admin")
public class AdminAdminController {

	@Autowired
	ArticleService articleService;
	
	@Autowired
	NewspaperService newspaperService;
	
	@Autowired
	AdvertisementService	advertisementService;
	
	@Autowired
	ChirpService	chirpService;
	
	
	
	@RequestMapping(value = "/listArticles", method = RequestMethod.GET)
	public ModelAndView listArticles(){
		ModelAndView result;
		Collection<Article> articles;
		
		articles = this.articleService.findArticlesWithTabooWords();
		
		result = new ModelAndView("admin/listArticles");
		result.addObject("articles", articles);
		
		
		return result;
	}
	
	
	@RequestMapping(value = "/listNewspapers", method = RequestMethod.GET)
	public ModelAndView listNewspapers(){
		ModelAndView result;
		Collection<Newspaper> newspapers;
		
		newspapers = this.newspaperService.findNewspapersWithTabooWords();
		
		result = new ModelAndView("admin/listNewspapers");
		result.addObject("newspapers", newspapers);
		
		
		return result;
	}
	
	@RequestMapping(value = "/listChirps", method = RequestMethod.GET)
	public ModelAndView listChirps(){
		ModelAndView result;
		Collection<Chirp> chirps;
		
		chirps = this.chirpService.findChirpsWithTabooWords();
		
		result = new ModelAndView("admin/listChirps");
		result.addObject("chirps", chirps);
		
		
		return result;
	}
	
	@RequestMapping(value = "/listAdverts", method = RequestMethod.GET)
	public ModelAndView listAdverts(){
		ModelAndView result;
		Collection<Advertisement> adverts;
		
		adverts = this.advertisementService.findAdvertisementWithTabooWords();
		
		result = new ModelAndView("advertisement/list");
		result.addObject("advertisements", adverts);
		
		return result;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int chirpId){
		ModelAndView result;
		Chirp chirp;
		
		try{
			chirp = this.chirpService.findOne(chirpId);
			
			this.chirpService.delete(chirp);
			result = new ModelAndView("redirect:listChirps.do");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:listChirps.do");
			
		}

		
		return result;
	}
	
	
	

	@RequestMapping(value = "/deleteAdvert", method = RequestMethod.GET)
	public ModelAndView deleteAdvert(@RequestParam int advertisementId){
		ModelAndView result;
		Advertisement advert;
		
		try{
			advert = this.advertisementService.findOne(advertisementId);
			this.advertisementService.delete(advert);
			result = new ModelAndView("redirect:listAdverts.do");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:listAdverts.do");
		}
		
		
		return result;
	}
	
	
	
}
