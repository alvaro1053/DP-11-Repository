package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import domain.Actor;
import domain.Admin;
import domain.Folder;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed Repository
	@Autowired
	private MessageRepository		messageRepository;

	// Supporting services

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private AdminService	adminService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private FolderService			folderService;


	// Constructors

	public MessageService() {
		super();
	}

	// Simple CRUD methods
	public Message create() {
		Message result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Message();
		result.setSender(principal);
		result.setFolder(this.folderService.findOutBoxFolderActor(principal));
		result.setMoment(new Date(System.currentTimeMillis() - 1));

		return result;
	}

	// An actor who is authenticated must be able to exchange messages with other actors
	public Message save(final Message message) {
		Message result;
		Actor principal;
		Date moment;
		boolean isSpam;
		Collection<String> spamWords;
		Folder folder;
		Message copy;

		Assert.notNull(message);

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);

		moment = new Date(System.currentTimeMillis() - 1);
		isSpam = false;

		spamWords = this.customisationService.findCustomisation().getTabooWords();
		for (final String spam : spamWords)
			if (message.getSubject().toLowerCase().contains(spam.toLowerCase())) {
				isSpam = true;
				break;
			} else if (message.getBody().toLowerCase().contains(spam.toLowerCase())) {
				isSpam = true;
				break;
			}

		message.setMoment(moment);
		message.setIsSpam(isSpam);
		message.setSender(principal);
		Assert.isTrue(message.getRecipient().getId() != principal.getId());
		Assert.isTrue(message.getSender().getId() == principal.getId());

		folder = this.folderService.findInBoxFolderActor(message.getRecipient());
		Assert.notNull(folder);

		message.setFolder(folder);

		if (isSpam) {
			folder = this.folderService.findSpamBoxFolderActor(message.getRecipient());
			Assert.notNull(folder);
			message.setFolder(folder);
		}

		result = this.messageRepository.save(message);
		Assert.notNull(result);

		copy = new Message();
		copy.setSubject(message.getSubject());
		copy.setBody(message.getBody());
		copy.setMoment(message.getMoment());
		copy.setIsSpam(message.getIsSpam());
		copy.setPriority(message.getPriority());
		copy.setRecipient(message.getRecipient());
		copy.setSender(message.getSender());
		copy.setFolder(this.folderService.findOutBoxFolderActor(principal));
		this.messageRepository.save(copy);

		/*
		 * Collection<Message> sentMessagesUpdated;
		 * Collection<Message> receivedMessagesUpdated;
		 * 
		 * receivedMessagesUpdated = copy.getRecipient().getReceivedMessages();
		 * sentMessagesUpdated = principal.getSentMessages();
		 * 
		 * 
		 * receivedMessagesUpdated.remove(m);
		 * sentMessagesUpdated.remove(m);
		 * copy.getRecipient().setReceivedMessages(receivedMessagesUpdated);
		 * principal.setSentMessages(sentMessagesUpdated);
		 */

		return result;

	}

	public void delete(final Message message) {
		Actor principal;
		Folder folder;
		Folder trashBox;

		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(principal.getReceivedMessages().contains(message) || principal.getSentMessages().contains(message));

		folder = message.getFolder();

		if (folder.getIsSystem() && folder.getName().equals("trash box"))
			this.messageRepository.delete(message);
		else {
			trashBox = this.folderService.findTrashBoxFolderActor(principal);
			this.move(message, trashBox);

		}

	}

	// Other business methods

	public void move(final Message message, final Folder destination) {

		Actor principal;
		Folder origin;
		Collection<Message> updatedOriginFolder;
		Collection<Message> updatedDestinationFolder;
		Message m;

		Assert.notNull(message);
		Assert.notNull(destination);

		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(destination.getId() != 0);

		origin = message.getFolder();

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(principal.getReceivedMessages().contains(message) || principal.getSentMessages().contains(message));
		Assert.isTrue(principal.getFolders().contains(origin));
		Assert.isTrue(principal.getFolders().contains(destination));

		message.setFolder(destination);
		m = this.messageRepository.save(message);

		updatedOriginFolder = origin.getMessages();
		updatedDestinationFolder = destination.getMessages();

		updatedOriginFolder.remove(m);
		updatedDestinationFolder.add(m);

		origin.setMessages(updatedOriginFolder);
		destination.setMessages(updatedDestinationFolder);

	}

	public void broadcast(final Message m) {
		Admin principle;
		String subject, body, priority;
		Collection<Actor> actors;
		boolean isSpam;
		Date currentMoment;
		Message outboxMessage;

		Assert.notNull(m);

		principle = this.adminService.findByPrincipal();
		Assert.notNull(principle);

		subject = m.getSubject();
		body = m.getBody();
		priority = m.getPriority();
		isSpam = false;

		currentMoment = new Date(System.currentTimeMillis() - 1);

		actors = this.actorService.findAll();
		for (final Actor actor : actors)
			if (!(actor instanceof Admin || actor.getName().equals("DELETED/BORRADO"))) {
				final Message message = new Message();
				message.setSubject(subject);
				message.setBody(body);
				message.setPriority(priority);
				message.setSender(principle);
				message.setRecipient(actor);
				message.setIsSpam(isSpam);
				message.setMoment(currentMoment);
				message.setFolder(this.folderService.findNotificationFolderActor(actor));
				this.messageRepository.save(message);
			}

		//mensaje guardado en el out box del admin que realizó el broadcast:
		outboxMessage = new Message();
		outboxMessage.setSubject(subject);
		outboxMessage.setBody(body);
		outboxMessage.setPriority(priority);
		outboxMessage.setSender(principle);
		outboxMessage.setIsSpam(isSpam);
		outboxMessage.setMoment(currentMoment);
		outboxMessage.setFolder(this.folderService.findOutBoxFolderActor(principle));
		//El receptor del mensaje es el mismo que lo envía, así podemos distinguir los mensajes normales que envía un admin
		// de los mensajes broadcast que envía ese admin
		outboxMessage.setRecipient(principle);
		this.messageRepository.save(outboxMessage);
	}

	public Message findOne(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);

		return result;
	}

	public Message createAndSaveNotificationStatusApplicationChanged(final Actor actor, final String body, final Date moment) {
		Message result;

		result = new Message();
		result.setSender(actor);
		result.setRecipient(actor);
		result.setPriority("NORMAL");
		result.setMoment(moment);
		result.setIsSpam(false);
		result.setFolder(this.folderService.findNotificationFolderActor(actor));
		result.setBody(body);
		result.setSubject("New status update");

		this.messageRepository.save(result);

		return result;
	}
	
	public void flush(){
		this.messageRepository.flush();
	}
}