package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;


@Entity
@Access(AccessType.PROPERTY)
public class Volume extends DomainEntity {

	private String	title;
	private String	description;
	private Integer	year;
	private User					user;
	private Collection<Newspaper>	newspapers;
	private Collection<Customer>	customersSubscribed;

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotNull
	@Range(min = 1900, max = 3000)
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}



	//Relationships
	@NotNull
	@ManyToOne(optional = true)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@ManyToMany
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}
	@ManyToMany
	public Collection<Customer> getCustomersSubscribed() {
		return customersSubscribed;
	}

	public void setCustomersSubscribed(Collection<Customer> customersSubscribed) {
		this.customersSubscribed = customersSubscribed;
	}
	
	
}