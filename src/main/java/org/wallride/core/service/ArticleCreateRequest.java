package org.wallride.core.service;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ArticleCreateRequest implements Serializable {

	private String code;
	private String coverId;
	private String title;
	private String body;
	private Long authorId;
	private LocalDateTime date;
	private Set<Long> categoryIds = new HashSet<>();
	private String tags;
	private Set<Long> relatedPostIds = new HashSet<>();
	private String seoTitle;
	private String seoDescription;
	private String seoKeywords;
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

	public String getBody() {
		return body;
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

	public Set<Long> getRelatedPostIds() {
		return relatedPostIds;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public String getLanguage() {
		return language;
	}

	public static class Builder  {

		private String code;
		private String coverId;
		private String title;
		private String body;
		private Long authorId;
		private LocalDateTime date;
		private Set<Long> categoryIds = new HashSet<>();
		private String tags;
		private Set<Long> relatedPostIds = new HashSet<>();
		private String seoTitle;
		private String seoDescription;
		private String seoKeywords;
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

		public Builder body(String body) {
			this.body = body;
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

		public Builder tags(String tags) {
			this.tags = tags;
			return this;
		}

		public Builder relatedPostIds(Set<Long> relatedPostIds) {
			this.relatedPostIds = relatedPostIds;
			return this;
		}

		public Builder seoTitle(String seoTitle) {
			this.seoTitle = seoTitle;
			return this;
		}

		public Builder seoDescription(String seoDescription) {
			this.seoDescription = seoDescription;
			return this;
		}

		public Builder seoKeywords(String seoKeywords) {
			this.seoKeywords = seoKeywords;
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
			request.body = body;
			request.authorId = authorId;
			request.date = date;
			request.categoryIds = categoryIds;
			request.tags = tags;
			request.relatedPostIds = relatedPostIds;
			request.seoTitle = seoTitle;
			request.seoDescription = seoDescription;
			request.seoKeywords = seoKeywords;
			request.language = language;
			return request;
		}
	}
}
