package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {
	
	@Query("select n from Newspaper n where n.publicationDate < CURRENT_TIMESTAMP")
	Collection<Newspaper> publishedNewspapers();
	
	@Query("select n from Newspaper n where n.publicationDate > CURRENT_TIMESTAMP")
	Collection<Newspaper> notPublishedNewspapers();
	
	@Query("select n from Newspaper n where n.title LIKE %?1% or n.description LIKE %?1% and (n.publicationDate < CURRENT_TIMESTAMP)")
	Collection<Newspaper> findByFilterPublished(String filter);
	
	@Query("select n from Newspaper n where n.title LIKE %?1% or n.description LIKE %?1%")
	Collection<Newspaper> findByFilter(String filter);

	@Query("select n from Newspaper n where n.tabooWords = true")
	Collection<Newspaper> findNewspapersWithTabooWords();

	@Query("select distinct a.newspapers from Advertisement a where a.agent.id = ?1")
	Collection<Newspaper> findPlacedAdsByAgent(int agentId);
	
	@Query("select distinct n from Newspaper n left join n.adverts ad where ((n.adverts is empty) or (ad.agent.id != ?1)) and (n.isPrivate = false) and (current_timestamp() >= n.publicationDate)")
	Collection<Newspaper> findNotPlacedAdsByAgent(int agentId);
	
}