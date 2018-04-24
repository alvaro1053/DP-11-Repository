
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "tabooWords")
})
public class Chirp extends DomainEntity {

	private Date	moment;
	private String	title;
	private String	description;
	private User	user;
	private Boolean	tabooWords;


	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	public Boolean getTabooWords() {
		return this.tabooWords;
	}
	public void setTabooWords(final Boolean tabooWords) {
		this.tabooWords = tabooWords;
	}

	//Relationship
	@ManyToOne(optional = true)
	public User getUser() {
		return this.user;
	}
	public void setUser(final User user) {
		this.user = user;
	}
}
