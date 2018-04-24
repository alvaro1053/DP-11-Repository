
package repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;
import domain.Newspaper;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	@Query("select a from Admin a where a.userAccount.id = ?1")
	Admin findByUserAccountId(int userAccountId);

	//7.1 The average and the standard deviation of newspapers created per user.
	@Query("select avg(u.newspapers.size) from User u")
	Double AverageNewspapersPerUser();
	@Query("select stddev(u.newspapers.size) from User u")
	Double StandardDesviationNewspapersPerUser();
	//7.2 The average and the standard deviation of articles written by writer.
	@Query("select avg(u.articles.size) from User u")
	Double AverageArticlesPerUser();
	@Query("select stddev(u.articles.size) from User u")
	Double StandardDesviationArticlesPerUser();
	//7.3 The average and the standard deviation of articles per newspaper.
	@Query("select avg(n.articles.size) from Newspaper n")
	Double AverageArticlesPerNewspaper();
	@Query("select stddev(n.articles.size) from Newspaper n")
	Double StandardDesviationArticlesPerNewspaper();
	//7.4 The newspapers that have at least 10% more articles than the average.
	@Query("select n.title from Newspaper n where n.articles.size >(select avg(n.articles.size) * 1.1 from Newspaper n)")
	Collection<Newspaper> NewspapersWithMoreArticlesThanAverage();
	//7.5 The newspapers that have at least 10% fewer articles than the average.
	@Query("select n.title from Newspaper n where n.articles.size <(select avg(n.articles.size) * 0.9 from Newspaper n)")
	Collection<Newspaper> NewspapersWithLessArticlesThanAverage();
	//7.6 The ratio of users who have ever created a newspaper.
	@Query("select (select count(u) from User u where (u.newspapers.size >=1))*1.0/count(u2) from User u2")
	Double RatioUsersWithAtLeast1Newspaper();
	//7.7 The ratio of users who have ever written an article.
	@Query("select (select count(u) from User u where (u.articles.size >=1))*1.0/count(u2) from User u2")
	Double RatioUsersWithAtLeast1Article();
	//6.1(B)The average number of follow-ups per article.
	@Query("select avg(a.followUps.size) from Article a")
	Double AverageFollowsUpPerArticle();
	//6.2 (B)The average number of follow-ups per article up to one week after the corresponding newspaper’s been published.
	@Query("select avg(a.followUps.size) from Newspaper n join n.articles a join a.followUps f where DATEDIFF(n.publicationDate, f.publicationDate) < 7")
	Double AverageFollowUpPerArticleOneWeek();
	//6.3 (B) The average number of follow-ups per article up to two weeks after the correspondingnewspaper’s been published.
	@Query("select avg(a.followUps.size) from Newspaper n join n.articles a join a.followUps f where DATEDIFF(n.publicationDate, f.publicationDate) < 14")
	Double AverageFollowUpPerArticleTwoWeek();
	//6.4 (B) The average and the standard deviation of the number of chirps per user.
	@Query("select avg(u.chirps.size) from User u")
	Double AverageChirpsPerUser();
	@Query("select stddev(u.chirps.size) from User u")
	Double StandardDesviationChirpsPerUser();
	//6.5 (B) The ratio of users who have posted above 75% the average number of chirps per user.
	@Query("select sum(case when(u.chirps.size>(select (avg(us.chirps.size)*0.75) from User us)) then 1.0 else 0.0 end)/count(u) from User u")
	Double RatioUsersWithMoreAvgChirps();
	//1.1 (A) The ratio of public versus private newspapers.
	@Query("select (select count(n1) from Newspaper n1 where n1.isPrivate = FALSE)/(count(n)*1.0) from Newspaper n where n.isPrivate = TRUE")
	Double RatioPublicVersusPrivate();
	//1.2 (A) The average number of articles per private newspapers.
	@Query("select avg(n.articles.size) from Newspaper n where n.isPrivate = TRUE")
	Double AverageArticlesPerPrivateNewspaper();
	//1.3 (A) The average number of articles per public newspapers.
	@Query("select avg(n.articles.size) from Newspaper n where n.isPrivate = FALSE")
	Double AverageArticlesPerPublicNewspaper();
	//1.4 (A) The ratio of subscribers per private newspaper versus the total number of customers.
	@Query("select count(s)/(select count(c) from Customer c) from Subscription s where s.newspaper.isPrivate = TRUE")
	Double ratioPublicVersusPrivatePerPublisher();	
	//1.5 (A) The average ratio of private versus public newspapers per publisher.
	@Query("select count(n)/(select count(n) from n) from User u join u.newspapers n where n.isPrivate = TRUE")
	Collection<Long> AverageRatioOfPrivateVersusPublicNewspapers();
}
