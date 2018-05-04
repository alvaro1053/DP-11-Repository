
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;


@Entity
@Access(AccessType.PROPERTY)
public class Customer extends Actor {

	private Collection<Volume> volumesSubscribed;
	private Collection<Subscription> subscriptions;

	
	
	@OneToMany(mappedBy="customer")
	public Collection<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Collection<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	@ManyToMany
	public Collection<Volume> getVolumesSubscribed() {
		return volumesSubscribed;
	}

	public void setVolumesSubscribed(Collection<Volume> volumesSubscribed) {
		this.volumesSubscribed = volumesSubscribed;
	}


}
