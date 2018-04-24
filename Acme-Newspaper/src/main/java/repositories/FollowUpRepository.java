package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.FollowUp;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Integer> {

	@Query("select f from Article a join a.followUps f where f.publicationDate < CURRENT_TIMESTAMP and a.id=?1")
	Collection<FollowUp> publishedFollowUps(int articleId);
	
}