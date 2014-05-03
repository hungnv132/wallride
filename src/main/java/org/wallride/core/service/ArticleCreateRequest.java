package org.wallride.core.service;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArticleCreateRequest implements Serializable {

	private String code;
	private String coverId;
	private String title;
	private List<String> bodies = new ArrayList<>();
	private Long authorId;
	private LocalDateTime date;
	private Set<Long> categoryIds = new HashSet<>();
	private Set<Long> relatedArticleIds = new HashSet<>();
	private String tags;
	private String metaKeywords;
	private String metaDescription;
	private String language;

	public String getCode() {
		return code;
	}

	public String getCoverId() {
		return coverId;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getBodies() {
		return bodies;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Set<Long> getCategoryIds() {
		return categoryIds;
	}

	public String getTags() {
		return tags;
	}

	public Set<Long> getRelatedArticleIds() {
		return relatedArticleIds;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public String getLanguage() {
		return language;
	}

	public static class Builder  {

		private String code;
		private String coverId;
		private String title;
		private List<String> bodies;
		private Long authorId;
		private LocalDateTime date;
		private Set<Long> categoryIds = new HashSet<>();
		private Set<Long> relatedArticleIds = new HashSet<>();
		private String tags;
		private String metaKeywords;
		private String metaDescription;
		private String language;

		public Builder() {
		}

		public Builder code(String code) {
			this.code = code;
			return this;
		}

		public Builder coverId(String coverId) {
			this.coverId = coverId;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder bodies(List<String> bodies) {
			this.bodies = bodies;
			return this;
		}

		public Builder authorId(Long authorId) {
			this.authorId = authorId;
			return this;
		}

		public Builder date(LocalDateTime date) {
			this.date = date;
			return this;
		}

		public Builder categoryIds(Set<Long> categoryIds) {
			this.categoryIds = categoryIds;
			return this;
		}

		public Builder relatedArticleIds(Set<Long> relatedArticleIds) {
			this.relatedArticleIds = relatedArticleIds;
			return this;
		}

		public Builder tags(String tags) {
			this.tags = tags;
			return this;
		}

		public Builder metaKeywords(String metaKeywords) {
			this.metaKeywords = metaKeywords;
			return this;
		}

		public Builder metaDescription(String metaDescription) {
			this.metaDescription = metaDescription;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public ArticleCreateRequest build() {
			ArticleCreateRequest request = new ArticleCreateRequest();
			request.code = code;
			request.coverId = coverId;
			request.title = title;
			request.bodies = bodies;
			request.authorId = authorId;
			request.date = date;
			request.categoryIds = categoryIds;
			request.relatedArticleIds = relatedArticleIds;
			request.tags = tags;
			request.metaKeywords = metaKeywords;
			request.metaDescription = metaDescription;
			request.language = language;
			return request;
		}
	}
}
