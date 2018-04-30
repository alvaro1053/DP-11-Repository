
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

	private String					title;
	private Date					moment;
	private String					summary;
	private String					body;
	private List<String>			photosURL;
	private Boolean					isDraft;	
	private Newspaper 				newspaper;
	
	
	
	@NotBlank
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}
	
	@NotBlank
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@NotBlank
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@ElementCollection
	public List<String> getPhotosURL() {
		return photosURL;
	}
	public void setPhotosURL(List<String> photosURL) {
		this.photosURL = photosURL;
	}
	
	@NotNull
	public Boolean getIsDraft() {
		return isDraft;
	}
	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}
	
	


//Relationships	
	
	@ManyToOne(optional = true)
	public Newspaper getNewspaper() {
		return newspaper;
	}
	public void setNewspaper(Newspaper newspaper) {
		this.newspaper = newspaper;
	}
	

}
