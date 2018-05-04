
package repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

	@Query("select s from Subscription s where s.customer.id =?1 and s.newspaper.id=?2")
	Subscription findByCustomerAndNewspaper(int customerId, int newspaperId);

}
