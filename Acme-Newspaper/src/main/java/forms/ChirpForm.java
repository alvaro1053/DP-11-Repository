
package forms;


import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import domain.DomainEntity;

public class ChirpForm extends DomainEntity {

	private Date		moment;
	private String		title;
	private String		description;


	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}
	
	@NotBlank
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
