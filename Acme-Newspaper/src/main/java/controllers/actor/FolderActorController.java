package controllers.actor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import controllers.AbstractController;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/folder/actor")
public class FolderActorController extends AbstractController {

	// Services

	@Autowired
	private FolderService	folderService;

	@Autowired
	private ActorService	actorService;


	// Constructors

	public FolderActorController() {
		super();
	}

	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Folder> folders;

		folders = this.folderService.findAllWithoutParentFolderByPrincipal();

		result = new ModelAndView("folder/list");
		result.addObject("folders", folders);

		return result;

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = {
		"folderId"
	})
	public ModelAndView list(@RequestParam final int folderId) {
		final ModelAndView result;
		Folder currentFolder;
		Collection<Message> messages;
		final Collection<Folder> folders;

		currentFolder = this.folderService.findOne(folderId);

		folders = currentFolder.getChildFolders();
		messages = currentFolder.getMessages();

		result = new ModelAndView("folder/list");
		result.addObject("folders", folders);
		result.addObject("currentFolder", currentFolder);
		result.addObject("messages", messages);

		return result;

	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Folder folder;

		folder = this.folderService.create();

		result = this.createEditModelAndView(folder);

		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET, params = "currentFolder")
	public ModelAndView create(@RequestParam final int currentFolder) {
		final ModelAndView result;
		Folder folder;

		folder = this.folderService.create();

		folder.setParentFolder(this.folderService.findOne(currentFolder));

		result = this.createEditModelAndView(folder);

		return result;

	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;

		folder = this.folderService.findOne(folderId);
		Assert.notNull(folder);
		result = this.createEditModelAndView(folder);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Folder folder, final BindingResult binding) {
		ModelAndView result;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		
		for (Folder fold : principal.getFolders()){
			if(fold.getName().equals(folder.getName())){
				binding.rejectValue("name", "folder.name.exists");
				result = this.createEditModelAndView(folder);
				return result;
			}
		}
		
		if (binding.hasErrors())
			result = this.createEditModelAndView(folder);
		else
			try {
				this.folderService.save(folder);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(folder, "folder.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Folder folder, final BindingResult binding) {
		ModelAndView result;

		try {
			this.folderService.delete(folder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(folder, "folder.commit.error");
		}

		return result;
	}

	//Ancillary methods
	protected ModelAndView createEditModelAndView(final Folder folder) {
		ModelAndView result;

		result = this.createEditModelAndView(folder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Folder folder, final String messageCode) {
		ModelAndView result;
		Collection<Message> messages;
		Folder parentFolder;
		Collection<Folder> childFolders;
		String name;
		Actor principal;
		boolean permission = false;

		principal = this.actorService.findByPrincipal();

		if (folder.getId() == 0)
			permission = true;
		else
			for (final Folder fol : principal.getFolders())
				if (fol.getId() == folder.getId()) {
					permission = true;
					break;
				}

		messages = folder.getMessages();

		childFolders = folder.getChildFolders();

		if (folder.getParentFolder() == null)
			parentFolder = null;
		else
			parentFolder = folder.getParentFolder();

		if (folder.getName() == null)
			name = null;
		else
			name = folder.getName();

		result = new ModelAndView("folder/edit");
		result.addObject("folder", folder);
		result.addObject("name", name);
		result.addObject("messages", messages);
		result.addObject("parentFolder", parentFolder);
		result.addObject("childFolders", childFolders);
		result.addObject("permission", permission);

		result.addObject("message", messageCode);

		return result;

	}
}
