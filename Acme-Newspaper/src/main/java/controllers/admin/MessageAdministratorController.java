
package controllers.admin;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/message/admin")
public class MessageAdministratorController extends AbstractController {

	// Services

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;


	// Constructors

	public MessageAdministratorController() {
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
	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "save")
	public ModelAndView broadcast(@Valid final Message mensaje, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(mensaje);
		else
			try {
				this.messageService.broadcast(mensaje);
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

		moment = mensaje.getMoment();
		folder = mensaje.getFolder();
		sender = mensaje.getSender();

		result = new ModelAndView("message/edit");
		result.addObject("moment", moment);
		result.addObject("folder", folder);
		result.addObject("sender", sender);
		result.addObject("mensaje", mensaje);
		//En la siguiente línea estamos indicando que el receptor del mensaje es el mismo administrador que lo está enviando,
		// esto es incorrecto, pero hará que el pase el @valid del método broadcast,
		// el servicio correspondiente se encargará de entregar el mensaje a todos los actores del sistema
		result.addObject("recipient", this.actorService.findByPrincipal());
		result.addObject("broadcast", true);
		result.addObject("permission", true);
		result.addObject("requestURI", "message/admin/broadcast.do");

		result.addObject("message", messageCode);

		return result;

	}
}
