package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Folder extends DomainEntity {

	private String				name;
	private boolean				isSystem;
	private Collection<Message>	messages;
	private Folder				parentFolder;
	private Collection<Folder>	childFolders;
	


	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	public boolean getIsSystem() {
		return this.isSystem;
	}
	public void setIsSystem(final boolean isSystem) {
		this.isSystem = isSystem;
	}

	@NotNull
	@OneToMany(mappedBy = "folder")
	public Collection<Message> getMessages() {
		return this.messages;
	}
	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

	@Valid
	@ManyToOne(optional = true)
	public Folder getParentFolder() {
		return this.parentFolder;
	}
	public void setParentFolder(final Folder parentFolder) {
		this.parentFolder = parentFolder;
	}

	@NotNull
	@OneToMany(mappedBy = "parentFolder")
	public Collection<Folder> getChildFolders() {
		return this.childFolders;
	}
	public void setChildFolders(final Collection<Folder> childFolders) {
		this.childFolders = childFolders;
	}



}
