package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import domain.Actor;
import domain.Folder;
import domain.Message;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class MessageServiceTest extends AbstractTest {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FolderService folderService;
	
	@Test
	public void writeMessageAndMove(){
		final Object testingData[][] = {

				//TEST POSITIVO
				//El user1 envía un mensaje al user2 y él lo mueve a una carpeta de su pripiedad
				{"user1","Hola", "esto es el test1","user2","folder13", null}, 
				//
				//==========================================================================//

				//TEST NEGATIVO
				//
				//El user1 se envía un mensaje así mismo
				{"customer1", "Hola", "esto es el test2","customer1","folder13", IllegalArgumentException.class},
				//Un actor sin loguear intenta enviar un mensaje
				{null,"Hola","esto es el test3", "user2","folder13", IllegalArgumentException.class},
				//El user1 envía un mensaje al user2 y él lo mueve a una carpeta que no es de su propiedad (Folder12 es del user1)
				{"user1","Hola","esto es el test3", "user2","folder12", IllegalArgumentException.class}
				
		};
		
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			this.templateWriteMessageAndMove((String) testingData[i][0],(String) testingData[i][1],(String) testingData[i][2], super.getEntityId((String) testingData[i][3]), super.getEntityId((String) testingData[i][4]),(Class<?>) testingData[i][5]);
			this.rollbackTransaction();
		}
}

	private void templateWriteMessageAndMove(String username, String messageSubject, String messageBody,
			int recipentId, int folderId, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			
			super.authenticate(username);
			
			Actor recipent = this.actorService.findOne(recipentId);
			Message message = this.messageService.create();
			message.setRecipient(recipent);
			message.setSubject(messageSubject);
			message.setBody(messageBody);
			message.setPriority("NORMAL");
			
			Message result = this.messageService.save(message);
			this.messageService.flush();
			super.unauthenticate();
			
			//Move message
			
			super.authenticate(recipent.getUserAccount().getUsername());
			Folder folder = this.folderService.findOne(folderId);
			this.messageService.move(result, folder);
			super.unauthenticate();
			
			 

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	
	
	@Test
	public void writeBroadcastMessage(){
		final Object testingData[][] = {

				//TEST POSITIVO
				//El admin envía un mensaje a todos los usuarios
				{"admin","Hola", "esto es el test1", null}, 
				//
				//==========================================================================//

				//TEST NEGATIVO
				//
				//El user1 intenta enviar un mensaje que llegue a todos los usuarios
				{"user1", "Hola", "esto es el test2", IllegalArgumentException.class},
				//El customer1 intenta enviar un mensaje que llegue a todos los usuarios
				{"customer1","Hola","esto es el test3", IllegalArgumentException.class}
		};
		
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			this.templateWriteBroadcastMessage((String) testingData[i][0],(String) testingData[i][1],(String) testingData[i][2], (Class<?>) testingData[i][3]);
			this.rollbackTransaction();
		}
}

	private void templateWriteBroadcastMessage(String username, String messageSubject, String messageBody, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			
			this.actorService.findByPrincipal();
			Message message = this.messageService.create();
			message.setSubject(messageSubject);
			message.setBody(messageBody);
			
			this.messageService.broadcast(message);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	
	
	
	}


