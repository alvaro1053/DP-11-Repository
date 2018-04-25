package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Access(AccessType.PROPERTY)
public class Agent extends Actor {
	
	private Collection<Adversiment>	adverts;


	@NotEmpty
	@OneToMany(mappedBy = "agent")
	public Collection<Adversiment> getAdversiments() {
		return this.adverts;
	}

	public void setAdversiments(final Collection<Adversiment> sponsorships) {
		this.adverts = sponsorships;
	}

}