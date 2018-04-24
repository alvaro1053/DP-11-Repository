package services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FollowUpRepository;
import domain.Article;
import domain.FollowUp;
import domain.User;

@Service
@Transactional
public class FollowUpService {

	//Managed Repository ----
	@Autowired
	private FollowUpRepository	followUpRepository;
	//Services
	@Autowired
	private UserService userService;

	//Constructors
	public FollowUpService() {
		super();
	}

	public FollowUp create() {
		FollowUp result;
		List<String> photosURL = new ArrayList<String>();
		User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);

		result = new FollowUp();
		result.setPhotosURL(photosURL);

		return result;
	}

	public Collection<FollowUp> findAll() {
		Collection<FollowUp> result;

		result = this.followUpRepository.findAll();

		return result;
	}

	public FollowUp save(final FollowUp followUp) {
		FollowUp result;
		Assert.isTrue(followUp.getId() == 0);
		User principal = userService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(followUp.getArticle().getIsDraft() == false || followUp.getArticle().getNewspaper().getPublicationDate() != null);
		
		result = this.followUpRepository.save(followUp);
		
		Article article = followUp.getArticle();
		Collection<FollowUp> articlesFollowUps = article.getFollowUps();
		articlesFollowUps.add(result);
		article.setFollowUps(articlesFollowUps);
		
		return result;
	}

	public FollowUp findOne(final int followUpId) {
		FollowUp result;

		result = this.followUpRepository.findOne(followUpId);
		Assert.notNull(result);

		return result;
	}

	public Collection<FollowUp> publishedFollowUps (int articleId){
		Collection<FollowUp> res = this.followUpRepository.publishedFollowUps(articleId);
		return res;
		
	}

}