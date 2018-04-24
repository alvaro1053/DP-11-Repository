package controllers.admin;



import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;

import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("dashboard/admin")
public class DashboardAdminController extends AbstractController {

	
	//Autowired
	@Autowired
	AdminService 		adminService;
	

	
	//Constructor
	public DashboardAdminController(){
		super();
	}
	
	//Save
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		
		Double AverageNewspapersPerUser,StandardDesviationNewspapersPerUser, AverageArticlesPerUser, StandardDesviationArticlesPerUser, AverageArticlesPerNewspaper,
		StandardDesviationArticlesPerNewspaper,RatioUsersWithAtLeast1Newspaper, RatioUsersWithAtLeast1Article, AverageFollowsUpPerArticle, AverageFollowUpPerArticleOneWeek,
		AverageFollowUpPerArticleTwoWeek, AverageChirpsPerUser, StandardDesviationChirpsPerUser, RatioUsersWithMoreAvgChirps, RatioPublicVersusPrivate, 
		AverageArticlesPerPrivateNewspaper, AverageArticlesPerPublicNewspaper, ratioPublicVersusPrivatePerPublisher;
		Long AverageRatioOfPrivateVersusPublicNewspapers;
		
		Collection<Newspaper> NewspapersWithMoreArticlesThanAverage, NewspapersWithLessArticlesThanAverage;
		

		//Stadistics
		AverageNewspapersPerUser = this.adminService.AverageNewspapersPerUser();
		StandardDesviationNewspapersPerUser = this.adminService.StandardDesviationNewspapersPerUser();
		
		AverageArticlesPerUser = this.adminService.AverageArticlesPerUser();
		StandardDesviationArticlesPerUser = this.adminService.StandardDesviationArticlesPerUser();

		AverageArticlesPerNewspaper = this.adminService.AverageArticlesPerNewspaper();
		StandardDesviationArticlesPerNewspaper = this.adminService.StandardDesviationArticlesPerNewspaper();

		RatioUsersWithAtLeast1Newspaper = this.adminService.RatioUsersWithAtLeast1Newspaper();
		RatioUsersWithAtLeast1Article = this.adminService.RatioUsersWithAtLeast1Article();

		AverageFollowsUpPerArticle = this.adminService.AverageFollowsUpPerArticle();
		AverageFollowUpPerArticleOneWeek = this.adminService.AverageFollowUpPerArticleOneWeek();

		AverageFollowUpPerArticleTwoWeek = this.adminService.AverageFollowUpPerArticleTwoWeek();
		AverageChirpsPerUser = this.adminService.AverageChirpsPerUser();

		StandardDesviationChirpsPerUser = this.adminService.StandardDesviationChirpsPerUser();
		RatioUsersWithMoreAvgChirps = this.adminService.RatioUsersWithMoreAvgChirps();
		RatioPublicVersusPrivate = this.adminService.RatioPublicVersusPrivate();
		AverageArticlesPerPrivateNewspaper = this.adminService.AverageArticlesPerPrivateNewspaper();
		
		AverageArticlesPerPublicNewspaper= this.adminService.AverageArticlesPerPublicNewspaper();
		ratioPublicVersusPrivatePerPublisher= this.adminService.ratioPublicVersusPrivatePerPublisher();
		
		AverageRatioOfPrivateVersusPublicNewspapers = this.adminService.AverageRatioOfPrivateVersusPublicNewspapers();
		
		
		
		
		NewspapersWithMoreArticlesThanAverage = this.adminService.NewspapersWithMoreArticlesThanAverage();
		NewspapersWithLessArticlesThanAverage = this.adminService.NewspapersWithLessArticlesThanAverage();
		

		result = new ModelAndView("admin/dashboard");

		result.addObject("AverageNewspapersPerUser", AverageNewspapersPerUser);
		result.addObject("StandardDesviationNewspapersPerUser", StandardDesviationNewspapersPerUser);
		result.addObject("AverageArticlesPerUser", AverageArticlesPerUser);
		result.addObject("StandardDesviationArticlesPerUser", StandardDesviationArticlesPerUser);
		result.addObject("AverageArticlesPerNewspaper", AverageArticlesPerNewspaper);
		result.addObject("StandardDesviationArticlesPerNewspaper", StandardDesviationArticlesPerNewspaper);
		result.addObject("RatioUsersWithAtLeast1Newspaper", RatioUsersWithAtLeast1Newspaper);
		result.addObject("RatioUsersWithAtLeast1Article", RatioUsersWithAtLeast1Article);
		result.addObject("AverageFollowsUpPerArticle", AverageFollowsUpPerArticle);
		result.addObject("AverageFollowUpPerArticleOneWeek", AverageFollowUpPerArticleOneWeek);
		result.addObject("AverageFollowUpPerArticleTwoWeek", AverageFollowUpPerArticleTwoWeek);
		result.addObject("AverageChirpsPerUser", AverageChirpsPerUser);
		result.addObject("StandardDesviationChirpsPerUser", StandardDesviationChirpsPerUser);
		result.addObject("RatioUsersWithMoreAvgChirps", RatioUsersWithMoreAvgChirps);
		result.addObject("RatioPublicVersusPrivate", RatioPublicVersusPrivate);
		result.addObject("AverageArticlesPerPrivateNewspaper", AverageArticlesPerPrivateNewspaper);
		result.addObject("AverageArticlesPerPublicNewspaper", AverageArticlesPerPublicNewspaper);
		result.addObject("ratioPublicVersusPrivatePerPublisher", ratioPublicVersusPrivatePerPublisher);
		result.addObject("AverageRatioOfPrivateVersusPublicNewspapers",AverageRatioOfPrivateVersusPublicNewspapers);
		
		result.addObject("NewspapersWithMoreArticlesThanAverage",NewspapersWithMoreArticlesThanAverage);
		result.addObject("NewspapersWithLessArticlesThanAverage",NewspapersWithLessArticlesThanAverage);

		return result;
	}
}
