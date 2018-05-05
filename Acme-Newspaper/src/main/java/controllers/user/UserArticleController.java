
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;
import domain.User;
import forms.ArticleForm;

@Controller
@RequestMapping("/article/user")
public class UserArticleController extends AbstractController {

	//Autowired
	@Autowired
	ArticleService		articleService;

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	UserService			userService;


	//Constructor
	public UserArticleController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String filter) {
		ModelAndView result;
		Collection<Article> articles;
		final User principal = this.userService.findByPrincipal();
		final String uri = "/user";
		
		articles = this.articleService.findByFilter(filter);
		result = new ModelAndView("article/list");
		
		result.addObject("uri", uri);
		result.addObject("articles", articles);
		result.addObject("principal", principal);

		return result;

	}

	//create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ArticleForm articleForm;
		Collection<Newspaper> newspapers;

		articleForm = this.articleService.createForm();
		newspapers = this.newspaperService.notPublishedNewspapers();

		result = this.createEditModelAndView(articleForm);
		result.addObject("newspapers", newspapers);

		return result;
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article;
		final User principal = this.userService.findByPrincipal();
		final String uri = "/user";

		article = this.articleService.findOne(articleId);

		
		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("uri", uri);
		result.addObject("principal", principal);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int articleId, final RedirectAttributes redir) {
		ModelAndView result;
		Article article;
		ArticleForm articleForm;
		final User principal = this.userService.findByPrincipal();
		try {
			article = this.articleService.findOne(articleId);
			Assert.isTrue(article.getUser().equals(principal));
			articleForm = this.articleService.reconstructForm(article);
			result = this.createEditModelAndView(articleForm);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/article/list.do");
			redir.addFlashAttribute("message", "article.permision");

		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ArticleForm articleForm, final BindingResult binding) {
		ModelAndView result;
		Article article;
		User principal;

		principal = this.userService.findByPrincipal();
		article = this.articleService.reconstruct(articleForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(articleForm);
		else
			try {
				this.articleService.save(article);
				result = new ModelAndView("redirect:/article/list.do");
			} catch (final Throwable oops) {
				articleForm.setIsDraft(true);
				result = this.createEditModelAndView(articleForm, "article.commit.error");
			}

		result.addObject("principal", principal);
		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final ArticleForm articleForm) {
		ModelAndView result;

		result = this.createEditModelAndView(articleForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ArticleForm articleForm, final String message) {
		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.notPublishedNewspapers();

		result = new ModelAndView("article/edit");
		result.addObject("articleForm", articleForm);
		result.addObject("message", message);
		result.addObject("newspapers", newspapers);

		return result;
	}

}
