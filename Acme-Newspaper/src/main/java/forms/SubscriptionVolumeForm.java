package forms;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.*;



@Entity
@Access(AccessType.PROPERTY)
public class SubscriptionVolumeForm extends DomainEntity {

	private CreditCard					creditCard;
	private Volume						volume;
	
	

	@Valid
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public Volume getVolume() {
		return volume;
	}
	public void setVolume(Volume volume) {
		this.volume = volume;
	}
	

}
