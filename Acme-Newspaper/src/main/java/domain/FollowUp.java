
package domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "publicationDate")
})
public class FollowUp extends DomainEntity {

	private String			title;
	private Date			publicationDate;
	private String			summary;
	private String			text;
	private List<String>	photosURL;

	private Article			article;


	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	@NotBlank
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Future
	public Date getPublicationDate() {
		return this.publicationDate;
	}
	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getSummary() {
		return this.summary;
	}
	public void setSummary(final String summary) {
		this.summary = summary;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getText() {
		return this.text;
	}
	public void setText(final String text) {
		this.text = text;
	}

	@ElementCollection
	public Collection<String> getPhotosURL() {
		return this.photosURL;
	}
	public void setPhotosURL(final List<String> photosURL) {
		this.photosURL = photosURL;
	}

	//Relationship
	@ManyToOne(optional = true)
	public Article getArticle() {
		return this.article;
	}
	public void setArticle(final Article article) {
		this.article = article;
	}

}
