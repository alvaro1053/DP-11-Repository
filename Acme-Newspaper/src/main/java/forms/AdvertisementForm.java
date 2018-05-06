package forms;

import java.util.Collection;


import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;


import domain.CreditCard;
import domain.DomainEntity;
import domain.Newspaper;

public class AdvertisementForm extends DomainEntity {

	private String 				title;
	private String				bannerURL;
	private String				targetPageURL;
	private CreditCard			creditCard;
	private Collection<Newspaper> newspapers;
	
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBannerURL() {
		return bannerURL;
	}
	public void setBannerURL(String bannerURL) {
		this.bannerURL = bannerURL;
	}
	

	public String getTargetPageURL() {
		return targetPageURL;
	}
	public void setTargetPageURL(String targetPageURL) {
		this.targetPageURL = targetPageURL;
	}
	

	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
	@NotEmpty
	public Collection<Newspaper> getNewspapers() {
		return newspapers;
	}
	public void setNewspapers(Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}
	
}
