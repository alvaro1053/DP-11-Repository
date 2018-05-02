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
public class FolderServiceTest extends AbstractTest{

	@Autowired
	private FolderService folderService;
	
	@Autowired
	private ActorService actorService;
	
	
	@Test
	public void createAndDeleteFolder(){
		final Object testingData[][] = {
				
				//TEST POSITIVO
				//El user1 crea la carpeta Folder1 y la borra
				{"user1","Folder1","Folder1", null}, 
				//
				//==========================================================================//

				//TEST NEGATIVO
				//
				//El user1 crea una carpeta sin nombre
				{"user1","",null, IllegalArgumentException.class},
				//Un actor intenta borrar una carpeta del sistema 
				{"user1",null,"system", IllegalArgumentException.class}
		};
		
		for (int i = 0; i < testingData.length; i++){
			this.startTransaction();
			this.templateCreateAndDeleteFolder((String) testingData[i][0],(String) testingData[i][1],(String) testingData[i][2], (Class<?>) testingData[i][3]);
			this.rollbackTransaction();
		}
}


	private void templateCreateAndDeleteFolder(String username, String createFolderName,
			String deleteFolderName, Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			Folder result = new Folder();
			Actor principal = this.actorService.findByPrincipal();
			
			if(createFolderName != null){
			Folder folder = this.folderService.create();
			folder.setName(createFolderName);
			result = this.folderService.save(folder);
			}
			if (deleteFolderName != null){
			this.folderService.delete(result);
			}
			if(deleteFolderName == "system"){
				Folder system = folderService.findInBoxFolderActor(principal);
				this.folderService.delete(system);
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
		
	}
	

