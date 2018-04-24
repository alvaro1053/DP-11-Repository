
package repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from User u join u.follows f join f.chirps c where u.id = ?1")
	public Collection<Chirp> findByUserFollowers(int userId);
	
	@Query("select c from Chirp c where c.tabooWords = true")
	public Collection<Chirp> findChirpsWithTabooWords();
}
