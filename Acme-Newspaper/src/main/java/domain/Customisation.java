package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Customisation extends DomainEntity {
	
	private List<String>	tabooWords;


	
	@NotNull
	@ElementCollection
	public List<String> getTabooWords() {
		return tabooWords;
	}

	public void setTabooWords(List<String> tabooWords) {
		this.tabooWords = tabooWords;
	}

	
}
