
package forms;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import domain.DomainEntity;
import domain.Newspaper;

@Entity
@Access(AccessType.PROPERTY)
public class ArticleForm extends DomainEntity {

	private String			title;
	private Date			moment;
	private String			summary;
	private String			body;
	private List<String>	photosURL;
	private Boolean			isDraft;
	private Newspaper		newspaper;


	@NotBlank
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getSummary() {
		return this.summary;
	}
	public void setSummary(final String summary) {
		this.summary = summary;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}
	public void setBody(final String body) {
		this.body = body;
	}

	@ElementCollection
	@NotNull
	public List<String> getPhotosURL() {
		return this.photosURL;
	}
	public void setPhotosURL(final List<String> photosURL) {
		this.photosURL = photosURL;
	}

	@NotNull
	public Boolean getIsDraft() {
		return this.isDraft;
	}
	public void setIsDraft(final Boolean isDraft) {
		this.isDraft = isDraft;
	}

	//Relationships	

	@ManyToOne(optional = true)
	public Newspaper getNewspaper() {
		return this.newspaper;
	}
	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

}
