package org.wallride.web.support;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;
import org.springframework.web.util.UriComponentsBuilder;
import org.wallride.core.domain.Article;
import org.wallride.core.domain.Blog;
import org.wallride.core.domain.Setting;
import org.wallride.core.service.BlogService;
import org.wallride.core.support.PostUtils;
import org.wallride.core.support.Settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class RssFeedView extends AbstractRssFeedView {
	
	private BlogService blogService;

	public void setBlogService(BlogService blogService) {
		this.blogService = blogService;
	}

	@Override
	protected void buildFeedMetadata(
			Map<String, Object> model, 
			Channel feed, 
			HttpServletRequest request) {
		Blog blog = blogService.readBlogById(Blog.DEFAULT_ID);
		String language = LocaleContextHolder.getLocale().getLanguage();

		feed.setTitle(blog.getTitle(language));
		feed.setDescription(blog.getTitle(language));

		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		feed.setLink(builder.buildAndExpand().toUriString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<Item> buildFeedItems(
			Map<String, Object> model,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		Set<Article> articles = (Set<Article>)model.get("articles");
		List<Item> items = new ArrayList<>(articles.size());
		for (Article article : articles) {
			Item item = new Item();
			item.setTitle(article.getTitle());
			item.setPubDate(article.getDate().toDate());
			Description description = new Description();
			description.setType("text/html");
			description.setValue(article.getBody());
			item.setDescription(description);
			item.setLink(link(article));
			items.add(item);
		}
		return items;
	}

	private String link(Article article) {
		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		Map<String, Object> params = new HashMap<>();

		Blog blog = blogService.readBlogById(Blog.DEFAULT_ID);
		if (blog.getLanguages().size() > 1) {
			builder.path("/{language}");
			params.put("language", LocaleContextHolder.getLocale().getLanguage());
		}
		builder.path("/{year}/{month}/{day}/{code}");
		params.put("year", String.format("%04d", article.getDate().getYear()));
		params.put("month", String.format("%02d", article.getDate().getMonthOfYear()));
		params.put("day", String.format("%02d", article.getDate().getDayOfMonth()));
		params.put("code", article.getCode());
		return builder.buildAndExpand(params).encode().toUriString();
	}
}