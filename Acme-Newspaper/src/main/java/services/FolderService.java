
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FolderRepository;
import domain.Actor;
import domain.Folder;
import domain.MailMessage;

@Service
@Transactional
public class FolderService {

	// Managed Repository
	@Autowired
	private FolderRepository	folderRepository;

	// Supporting services

	@Autowired
	private ActorService		actorService;


	// Constructors

	public FolderService() {
		super();
	}

	// Simple CRUD methods

	public Folder create() {
		Folder result;
		Actor principal;
		Collection<MailMessage> messages;
		Collection<Folder> childFolders;

		messages = new ArrayList<MailMessage>();
		childFolders = new ArrayList<Folder>();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Folder();
		Assert.notNull(result);

		result.setChildFolders(childFolders);
		result.setMessages(messages);

		return result;
	}

	public Folder save(final Folder folder) {
		Folder result;
		Actor principal;
		Collection<MailMessage> messages;
		Collection<Folder> childFolders;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		if (folder.getId() == 0) {
			
			Assert.isTrue(folder.getName() != "");
			folder.setIsSystem(false);
			messages = new ArrayList<MailMessage>();
			folder.setMessages(messages);
			childFolders = new ArrayList<Folder>();
			folder.setChildFolders(childFolders);

		} else {

			Assert.isTrue(!folder.getIsSystem());
			Assert.isTrue(principal.getFolders().contains(folder));
			childFolders = folder.getChildFolders();

			boolean containsSystemfolders = false;
			for (final Folder f : childFolders)
				if (f.getIsSystem()) {
					containsSystemfolders = true;
					break;
				}

			Assert.isTrue(!containsSystemfolders);

		}
		result = this.folderRepository.save(folder);
		Assert.notNull(result);

		if (!principal.getFolders().contains(result))
			principal.getFolders().add(result);

		return result;

	}
	/*
	 * public Folder saveSystemFolder(final Folder folder) {
	 * Administrator principal;
	 * Folder saved;
	 * 
	 * principal = this.administratorService.findByPrincipal();
	 * Assert.notNull(principal);
	 * 
	 * Assert.notNull(folder);
	 * Assert.isTrue(folder.getIsSystem());
	 * 
	 * saved = this.folderRepository.save(folder);
	 * Assert.notNull(saved);
	 * 
	 * return saved;
	 * 
	 * }
	 */
	public void delete(final Folder folder) {
		Actor principal;
		Collection<Folder> childFolders;

		Assert.isTrue(folder.getId() != 0);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(!folder.getIsSystem());
		Assert.isTrue(principal.getFolders().contains(folder));

		childFolders = folder.getChildFolders();

		this.folderRepository.delete(folder);

		for (final Folder f : childFolders)
			this.folderRepository.delete(f);

	}

	public List<Folder> createSystemFolders() {

		List<Folder> result;
		List<String> names;
		Collection<Folder> childFolders;
		Collection<MailMessage> messages;
		Folder saved;

		names = new ArrayList<String>();
		names.add("in box");
		names.add("out box");
		names.add("notification box");
		names.add("spam box");
		names.add("trash box");

		result = new ArrayList<Folder>();
		for (final String name : names) {
			final Folder folder = new Folder();
			folder.setName(name);
			folder.setIsSystem(true);
			folder.setParentFolder(null);
			childFolders = new ArrayList<Folder>();
			folder.setChildFolders(childFolders);
			messages = new ArrayList<MailMessage>();
			folder.setMessages(messages);
			saved = this.folderRepository.save(folder);
			Assert.notNull(saved);

			result.add(saved);

		}

		return result;

	}

	// Other business methods

	public Folder findNotificationFolderActor(final Actor a) {
		Folder result;
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);
		result = this.folderRepository.findNotificationFolderActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public Folder findInBoxFolderActor(final Actor a) {
		Actor principal;
		Folder result;

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.folderRepository.findInBoxFolderActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public Folder findOutBoxFolderActor(final Actor a) {
		Actor principal;
		Folder result;

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.folderRepository.findOutBoxFolderActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public Folder findSpamBoxFolderActor(final Actor a) {
		Actor principal;
		Folder result;
		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.folderRepository.findSpamFolderActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public Folder findTrashBoxFolderActor(final Actor a) {
		Actor principal;
		Folder result;
		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);
		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		result = this.folderRepository.findTrashFolderActorId(a.getId());
		Assert.notNull(result);
		return result;
	}

	public Collection<Folder> findAllWithoutParentFolderByPrincipal() {
		Collection<Folder> result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		result = this.folderRepository.findAllWithoutParentFolderByPrincipal(principal.getId());
		Assert.notNull(result);
		return result;
	}
	public Collection<Folder> findAllByPrincipal() {
		Collection<Folder> result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		result = this.folderRepository.findAllByPrincipal(principal.getId());
		Assert.notNull(result);
		return result;
	}
	public Folder findOne(final int folderId) {
		Folder result;
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		result = this.folderRepository.findOne(folderId);
		Assert.notNull(result);
		//		Assert.isTrue(result.getOwner().equals(principal));
		return result;
	}

}
