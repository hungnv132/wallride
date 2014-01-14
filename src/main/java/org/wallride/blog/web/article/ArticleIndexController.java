package org.wallride.blog.web.article;

import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;
import org.wallride.blog.service.ArticleService;
import org.wallride.core.domain.Article;
import org.wallride.core.domain.Category;
import org.wallride.core.domain.CategoryTree;
import org.wallride.core.domain.Setting;
import org.wallride.core.service.CategoryTreeService;
import org.wallride.core.service.SettingService;
import org.wallride.core.support.Paginator;
import org.wallride.core.web.DomainObjectSearchCondition;
import org.wallride.core.web.HttpNotFoundException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ArticleIndexController {

//	/article/[:yyyy]/[:mm]/[:dd]/[:code]
//	/categories/[:code]/[:code]/[:code]/[:code]/
//	/tag/[:code]/

	@Inject
	private ArticleService articleService;

	@Inject
	private CategoryTreeService categoryTreeService;

	@Inject
	private SettingService settingService;

	@RequestMapping("/{language}/{year:[0-9]{4}}")
	public String year(
			@PathVariable String language,
			@PathVariable int year,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) String token,
			HttpSession session,
			Model model) {
		DomainObjectSearchCondition<ArticleSearchForm> condition = DomainObjectSearchCondition.resolve(session, ArticleSearchForm.class, token);
		if (condition == null) {
			ArticleSearchForm form = new ArticleSearchForm() {};
			form.setLanguage(language);
			form.setDateFrom(new LocalDateTime(year, 1, 1, 0, 0, 0));
			form.setDateTo(new LocalDateTime(year, 12, 31, 0, 0, 0));
			Paginator<Long> paginator = articleService.readArticles(form);
			condition = new DomainObjectSearchCondition<ArticleSearchForm>(session, form, paginator);
		}
		if (page != null && condition.getPaginator().hasElement()) {
			condition.getPaginator().setNumber(page);
		}

		List<Article> articles = articleService.readArticles(condition.getPaginator());
		model.addAttribute("articles", articles);
		model.addAttribute("paginator", condition.getPaginator());
		model.addAttribute("token", condition.getToken());
		return "/article/index";
	}

	@RequestMapping("/{language}/{year:[0-9]{4}}/{month:[0-9]{2}}")
	public String month(
			@PathVariable String language,
			@PathVariable int year,
			@PathVariable int month,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) String token,
			HttpSession session,
			Model model) {
		DomainObjectSearchCondition<ArticleSearchForm> condition = DomainObjectSearchCondition.resolve(session, ArticleSearchForm.class, token);
		if (condition == null) {
			ArticleSearchForm form = new ArticleSearchForm() {};
			form.setLanguage(language);
			LocalDateTime date = new LocalDateTime(year, month, 1, 0, 0, 0);
			form.setDateFrom(new LocalDateTime(year, month, 1, 0, 0, 0));
			form.setDateTo(new LocalDateTime(year, month, date.dayOfMonth().getMaximumValue(), 23, 59, 59));
			Paginator<Long> paginator = articleService.readArticles(form);
			condition = new DomainObjectSearchCondition<ArticleSearchForm>(session, form, paginator);
		}
		if (page != null && condition.getPaginator().hasElement()) {
			condition.getPaginator().setNumber(page);
		}

		List<Article> articles = articleService.readArticles(condition.getPaginator());
		model.addAttribute("articles", articles);
		model.addAttribute("paginator", condition.getPaginator());
		model.addAttribute("token", condition.getToken());
		return "/article/index";
	}

	@RequestMapping("/{language}/{year:[0-9]{4}}/{month:[0-9]{2}}/{day:[0-9]{2}}")
	public String day(
			@PathVariable String language,
			@PathVariable int year,
			@PathVariable int month,
			@PathVariable int day,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) String token,
			HttpSession session,
			Model model) {
		DomainObjectSearchCondition<ArticleSearchForm> condition = DomainObjectSearchCondition.resolve(session, ArticleSearchForm.class, token);
		if (condition == null) {
			ArticleSearchForm form = new ArticleSearchForm() {};
			form.setLanguage(language);
			form.setDateFrom(new LocalDateTime(year, month, day, 0, 0, 0));
			form.setDateTo(new LocalDateTime(year, month, day, 23, 59, 59));
			Paginator<Long> paginator = articleService.readArticles(form);
			condition = new DomainObjectSearchCondition<ArticleSearchForm>(session, form, paginator);
		}
		if (page != null && condition.getPaginator().hasElement()) {
			condition.getPaginator().setNumber(page);
		}

		List<Article> articles = articleService.readArticles(condition.getPaginator());
		model.addAttribute("articles", articles);
		model.addAttribute("paginator", condition.getPaginator());
		model.addAttribute("token", condition.getToken());
		return "/article/index";
	}

	@RequestMapping("/{language}/category/**")
	public String category(
			@PathVariable String language,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) String token,
			@RequestParam(required=false) Integer year,
			HttpServletRequest request,
			HttpSession session,
			Model model) {
		String path = extractPathFromPattern(request);
		String[] codes = path.split("/");
		String lastCode = codes[codes.length - 1];

		CategoryTree categoryTree = categoryTreeService.readCategoryTree(language);
		Category category = categoryTree.getCategoryByCode(lastCode);
		if (category == null) {
			String defaultLanguage = settingService.readSettingAsString(Setting.Key.DEFAULT_LANGUAGE);
			categoryTree = categoryTreeService.readCategoryTree(defaultLanguage);
			category = categoryTree.getCategoryByCode(lastCode);
			if (category == null) {
				throw new HttpNotFoundException();
			}
		}

		DomainObjectSearchCondition<ArticleSearchForm> condition = DomainObjectSearchCondition.resolve(session, ArticleSearchForm.class, token);
		if (condition == null) {
			ArticleSearchForm form = new ArticleSearchForm() {};
			form.setLanguage(language);
			form.getCategoryIds().add(category.getId());
			if (year != null) {
				LocalDateTime dateFrom = new LocalDateTime(year, 1, 1, 0, 0);
				LocalDateTime dateTo = new LocalDateTime(year, 12, 31, 0, 0);
				form.setDateFrom(dateFrom);
				form.setDateTo(dateTo);
			}
			Paginator<Long> paginator = articleService.readArticles(form);
			condition = new DomainObjectSearchCondition<ArticleSearchForm>(session, form, paginator);
		}
		if (page != null && condition.getPaginator().hasElement()) {
			condition.getPaginator().setNumber(page);
		}

		List<Article> articles = articleService.readArticles(condition.getPaginator());

		List<Map<String, Long>> articleCountsByYear =  articleService.readArticleCountsByYear(lastCode, language);

		model.addAttribute("code", lastCode);
		model.addAttribute("category", category);
		model.addAttribute("articles", articles);
		model.addAttribute("articleCountsByYear", articleCountsByYear);
		model.addAttribute("paginator", condition.getPaginator());
		model.addAttribute("token", condition.getToken());
		return "/article/index";
	}

	private String extractPathFromPattern(final HttpServletRequest request){
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

		AntPathMatcher apm = new AntPathMatcher();
		String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

		return finalPath;
	}
}
