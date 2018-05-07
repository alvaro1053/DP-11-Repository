
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.MailMessage;

@Repository
public interface MessageRepository extends JpaRepository<MailMessage, Integer> {

}
