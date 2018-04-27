package forms;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import domain.DomainEntity;
import domain.Newspaper;

@Entity
@Access(AccessType.PROPERTY)
public class VolumeForm extends DomainEntity {

	private String	title;
	private String	description;
	private int	year;
	private Collection<Newspaper>	newspapers;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotNull
	@Range(min = 1900, max = 3000)
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	//Relationships

	@ManyToMany
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}
}