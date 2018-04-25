
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "publicationDate, isPrivate, title, description")
})
public class Newspaper extends DomainEntity {

	private String						title;
	private Date						publicationDate;
	private String						description;
	private String						pictureURL;
	private Boolean						isPrivate;
	private Collection<Article>			articles;
	private User						user;
	private Collection<Subscription>	subscriptions;
	private Boolean						tabooWords;
	
	private Volume					volumen;
	private Collection<Adversiment> adverts;


	@NotBlank
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getPublicationDate() {
		return this.publicationDate;
	}
	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getPictureURL() {
		return this.pictureURL;
	}
	public void setPictureURL(final String pictureURL) {
		this.pictureURL = pictureURL;
	}

	@NotNull
	public Boolean getIsPrivate() {
		return this.isPrivate;
	}
	public void setIsPrivate(final Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Boolean getTabooWords() {
		return this.tabooWords;
	}
	public void setTabooWords(final Boolean tabooWords) {
		this.tabooWords = tabooWords;
	}

	//Relationships
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Article> getArticles() {
		return this.articles;
	}
	public void setArticles(final Collection<Article> articles) {
		this.articles = articles;
	}

	@ManyToOne(optional = true)
	public User getUser() {
		return this.user;
	}
	public void setUser(final User user) {
		this.user = user;
	}

	@OneToMany(mappedBy = "newspaper")
	public Collection<Subscription> getSubscriptions() {
		return this.subscriptions;
	}
	public void setSubscriptions(final Collection<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	@Valid
	@ManyToMany
	public Volume getVolumen() {
		return volumen;
	}
	public void setVolumen(Volume volumen) {
		this.volumen = volumen;
	}
	@ManyToMany
	public Collection<Adversiment> getAdverts() {
		return adverts;
	}
	public void setAdverts(Collection<Adversiment> adverts) {
		this.adverts = adverts;
	}
	
	
}
