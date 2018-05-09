
package forms;





import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;



import domain.DomainEntity;

public class EditActorForm extends DomainEntity {

	private String		name;
	private String		surname;
	private String		email;
	private String		phone;
	private String		address;






	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}


	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String emails) {
		this.email = emails;
	}

	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	@Pattern(regexp="\\+?([0-9]+)?")
	public String getPhone() {
		return this.phone;
	}
	
	
	public void setPhone(final String phones) {
		this.phone = phones;
	}

	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String addresses) {
		this.address = addresses;
	}


}
