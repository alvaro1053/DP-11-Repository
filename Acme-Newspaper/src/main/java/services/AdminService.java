
package services;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdminRepository;
import security.LoginService;
import security.UserAccount;
import domain.Admin;
import domain.Newspaper;

@Service
@Transactional
public class AdminService {

	// Managed Repository
	@Autowired
	private AdminRepository	adminRepository;


	// Supporting services

	// Constructors

	public AdminService() {
		super();
	}

	// Simple CRUD methods
	public Admin create() {
		Admin principal;
		Admin result;
		principal = this.findByPrincipal();
		Assert.notNull(principal);
		result = new Admin();
		return result;
	}

	public Admin save(final Admin Admin) {
		Admin saved;
		Assert.notNull(Admin);

		if (Admin.getId() == 0) {
			Admin principal;
			principal = this.findByPrincipal();
			Assert.notNull(principal);
			final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			Admin.getUserAccount().setPassword(passwordEncoder.encodePassword(Admin.getUserAccount().getPassword(), null));
		} else {
			Admin principal;
			principal = this.findByPrincipal();
			Assert.notNull(principal);

		}

		saved = this.adminRepository.save(Admin);

		return saved;
	}

	public Admin findOne(final int AdminId) {
		Admin result;
		result = this.adminRepository.findOne(AdminId);
		return result;
	}

	public Admin findByPrincipal() {
		Admin result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;

	}
	public Admin findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Admin result;
		result = this.adminRepository.findByUserAccountId(userAccount.getId());
		return result;
	}
	
	
	//queries
	
	//7.1
	public Double AverageNewspapersPerUser(){
		Double result;
		result = this.adminRepository.AverageNewspapersPerUser();
		return result;
	}
	
	public Double StandardDesviationNewspapersPerUser(){
		Double result;
		result = this.adminRepository.StandardDesviationNewspapersPerUser();
		return result;
	}
	//7.2
	public Double AverageArticlesPerUser(){
		Double result;
		result = this.adminRepository.AverageArticlesPerUser();
		return result;
	}
	
	public Double StandardDesviationArticlesPerUser(){
		Double result;
		result = this.adminRepository.StandardDesviationArticlesPerUser();
		return result;
	}
	//7.3
	public Double AverageArticlesPerNewspaper(){
		Double result;
		result = this.adminRepository.AverageArticlesPerNewspaper();
		return result;
	}
	
	public Double StandardDesviationArticlesPerNewspaper(){
		Double result;
		result = this.adminRepository.StandardDesviationArticlesPerNewspaper();
		return result;
	}
	//7.4
	public Collection<Newspaper> NewspapersWithMoreArticlesThanAverage(){
		Collection<Newspaper> result;
		result = this.adminRepository.NewspapersWithMoreArticlesThanAverage();
		return result;
	}
	//7.5
	public Collection<Newspaper> NewspapersWithLessArticlesThanAverage(){
		Collection<Newspaper> result;
		result = this.adminRepository.NewspapersWithLessArticlesThanAverage();
		return result;
	}
	//7.6
	public Double RatioUsersWithAtLeast1Newspaper(){
		Double result;
		result = this.adminRepository.RatioUsersWithAtLeast1Newspaper();
		return result;
	}
	//7.7
	public Double RatioUsersWithAtLeast1Article(){
		Double result;
		result = this.adminRepository.RatioUsersWithAtLeast1Article();
		return result;
	}
	//6.1
	public Double AverageFollowsUpPerArticle(){
		Double result;
		result = this.adminRepository.AverageFollowsUpPerArticle();
		return result;
	}
	//6.2
	public Double AverageFollowUpPerArticleOneWeek(){
		Double result;
		result = this.adminRepository.AverageFollowUpPerArticleOneWeek();
		return result;
	}
	//6.3
	public Double AverageFollowUpPerArticleTwoWeek(){
		Double result;
		result = this.adminRepository.AverageFollowUpPerArticleTwoWeek();
		return result;
	}
	//6.4
	public Double AverageChirpsPerUser(){
		Double result;
		result = this.adminRepository.AverageChirpsPerUser();
		return result;
	}
	
	public Double StandardDesviationChirpsPerUser(){
		Double result;
		result = this.adminRepository.StandardDesviationChirpsPerUser();
		return result;
	}
	//6.5
	public Double RatioUsersWithMoreAvgChirps(){
		Double result;
		result = this.adminRepository.RatioUsersWithMoreAvgChirps();
		return result;
	}
	//1.1
	public Double RatioPublicVersusPrivate(){
		Double result;
		result = this.adminRepository.RatioPublicVersusPrivate();
		return result;
	}
	//1.2
	public Double AverageArticlesPerPrivateNewspaper(){
		Double result;
		result = this.adminRepository.AverageArticlesPerPrivateNewspaper();
		return result;
	}
	//1.3
	public Double AverageArticlesPerPublicNewspaper(){
		Double result;
		result = this.adminRepository.AverageArticlesPerPublicNewspaper();
		return result;
	}
	//1.4
	public Double ratioPublicVersusPrivatePerPublisher(){
		Double result;
		result = this.adminRepository.ratioPublicVersusPrivatePerPublisher();
		return result;
	}
	//1.5
	public Double AverageRatioOfPrivateVersusPublicNewspapers(){
		Admin principal = this.findByPrincipal();
		Assert.notNull(principal);
		Double res = 0.;
		Double sumas = 0.;
		Collection<Double> ratios = this.adminRepository.AverageRatioOfPrivateVersusPublicNewspapers();
		for (Double d : ratios){
			sumas +=d;
		}
		res = (sumas/ratios.size());
		return res;
	}
	
	//5.3
	public Double ratioNewspaperOneAdvertisementOrAny(){
		Double result;
		Admin principal = this.findByPrincipal();
		Assert.notNull(principal);
		
		result = this.adminRepository.RatioNewspaperOneAdvertisementVersusAny();
		
		if(result == null){
			result = 0.0;
		}
		
		return result;
	}
	
	//5.3
	public Double ratioAdvertisementTabooWords(){
		Double result;
		Admin principal = this.findByPrincipal();
		Assert.notNull(principal);
		
		result = this.adminRepository.RatioAdvertisementTabooWords();
		
		if(result == null){
			result = 0.0;
		}
		
		return result;
	}
	
	//11.1
	public Double AverageNewspapersPerVolume(){
		Double result;
		Admin principal = this.findByPrincipal();
		Assert.notNull(principal);
		
		result = this.adminRepository.AverageNewspapersPerVolume();
			
		return result;
	}
	//11.2
	public Double ratioSubscriptionsVolumesVersusNewspapers(){
		Double result;
		Admin principal = this.findByPrincipal();
		Assert.notNull(principal);
		
		result = this.adminRepository.ratioSubscriptionsVolumesVersusNewspapers();
			
		return result;
	}

}
