package controllers.actor;

import java.util.Collection;
import java.util.Date;

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
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/message/actor")
public class MessageActorController extends AbstractController {

	// Services

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private FolderService	folderService;


	// Constructors

	public MessageActorController() {
		super();
	}

	// Listing

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Message mensaje;

		mensaje = this.messageService.create();

		result = this.createEditModelAndView(mensaje);

		return result;

	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {
		ModelAndView result;
		Message message;

		message = this.messageService.findOne(messageId);
		Assert.notNull(message);
		result = this.createEditModelAndView(message);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Message mensaje, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(mensaje);
		else
			try {
				this.messageService.save(mensaje);
				result = new ModelAndView("redirect:/folder/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(mensaje, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "move")
	public ModelAndView move(@Valid final Message mensaje, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(mensaje);
		else
			try {
				this.messageService.move(mensaje, mensaje.getFolder());
				result = new ModelAndView("redirect:/folder/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(mensaje, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Message mensaje, final BindingResult binding) {
		ModelAndView result;

		try {
			this.messageService.delete(mensaje);
			result = new ModelAndView("redirect:/folder/actor/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(mensaje, "message.commit.error");
		}

		return result;
	}

	//Ancillary methods
	protected ModelAndView createEditModelAndView(final Message mensaje) {
		ModelAndView result;

		result = this.createEditModelAndView(mensaje, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message mensaje, final String messageCode) {
		final ModelAndView result;
		Date moment;
		Folder folder;
		Actor sender;
		Collection<Actor> recipients;
		Collection<Folder> folders;
		boolean permission = false;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		if (mensaje.getId() == 0)
			permission = true;
		else {
			for (final Message mens : principal.getReceivedMessages())
				if (mens.getId() == mensaje.getId()) {
					permission = true;
					break;
				}
			if (!permission)
				for (final Message mens : principal.getSentMessages())
					if (mens.getId() == mensaje.getId()) {
						permission = true;
						break;
					}
		}

		moment = mensaje.getMoment();
		folder = mensaje.getFolder();
		sender = mensaje.getSender();
		recipients = this.actorService.findAllMinusPrincipal();
		folders = this.folderService.findAllByPrincipal();

		result = new ModelAndView("message/edit");
		result.addObject("moment", moment);
		result.addObject("folder", folder);
		result.addObject("sender", sender);
		result.addObject("mensaje", mensaje);
		result.addObject("recipients", recipients);
		result.addObject("folders", folders);
		result.addObject("requestURI", "message/actor/edit.do");
		result.addObject("broadcast", false);
		result.addObject("permission", permission);

		result.addObject("message", messageCode);

		return result;

	}
}
