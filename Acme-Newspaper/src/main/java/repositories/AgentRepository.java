package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {

	@Query("select u from UserAccount u where u.id = ?1")
	public Agent findByUserAccount(int userAccountId);
	
}
