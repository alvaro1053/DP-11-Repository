package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Advertisement extends DomainEntity {

	private String 				title;
	private String				bannerURL;
	private String				targetPageURL;
	private CreditCard			creditCard;
	private Agent				agent;
	private Collection<Newspaper>	newspapers;
	private Boolean				tabooWords;

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	@URL
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getBannerURL() {
		return this.bannerURL;
	}
	public void setBannerURL(final String linkBanner) {
		this.bannerURL = linkBanner;
	}
	@NotBlank
	@URL
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTargetPageURL() {
		return this.targetPageURL;
	}
	public void setTargetPageURL(final String targetPageURL) {
		this.targetPageURL = targetPageURL;
	}

	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}
	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
	public Boolean getTabooWords() {
		return tabooWords;
	}
	public void setTabooWords(Boolean tabooWords) {
		this.tabooWords = tabooWords;
	}
	
	// Relationships

	@NotNull
	@Valid
	@ManyToOne(optional = true)
	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	
	@ManyToMany
	@NotNull
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}
	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

}