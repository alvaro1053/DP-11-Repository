package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("select a from User u join u.newspapers n join n.articles a where n.publicationDate < CURRENT_TIMESTAMP and u.id=?1")
	Collection<Article> articlesPublishedByUser(int userId);
	
	@Query("select a from User u join u.newspapers n join n.articles a where n.publicationDate > CURRENT_TIMESTAMP and u.id=?1")
	Collection<Article> articlesNotPublishedByUser(int userId);
	
	@Query("select a from Article a where a.newspaper.publicationDate < CURRENT_TIMESTAMP")
	Collection<Article> articlesPublished();
	
	@Query("select a from Article a where (a.title like CONCAT('%',?1,'%') or a.summary like CONCAT('%',?1,'%') or a.body like CONCAT('%',?1,'%')) and (a.newspaper.publicationDate < CURRENT_TIMESTAMP)")
	Collection<Article> findByFilter(String filter);
	
	@Query("select a from Article a where (a.title like CONCAT('%',?1,'%') or a.summary like CONCAT('%',?1,'%') or a.body like CONCAT('%',?1,'%')) and (a.user.id =?2)")
	Collection<Article> findByFilterByUser(String filter, int userId);
	
	@Query("select a from Article a where a.tabooWords = true")
	Collection<Article> findArticlesWithTabooWords();
	
	@Query("select a from Article a where a.newspaper.id = ?1")
	Collection<Article> articlesOfNewspaper(int newspaperId);
	
}