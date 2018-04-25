package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.PROPERTY)
public class Agent extends Actor {
	
	private Collection<Advertisement>	advertisements;


	@OneToMany(mappedBy = "agent")
	public Collection<Advertisement> getAdvertisements() {
		return this.advertisements;
	}

	public void setAdvertisements(Collection<Advertisement> advertisements) {
		this.advertisements = advertisements;

	}

}