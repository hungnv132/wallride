package org.wallride.core.repository;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallride.core.domain.Article;
import org.wallride.core.service.ArticleSearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<Article> search(ArticleSearchRequest request, Pageable pageable) {
		FullTextEntityManager fullTextEntityManager =  Search.getFullTextEntityManager(entityManager);
		QueryBuilder qb = fullTextEntityManager.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(Article.class)
				.get();
		
		@SuppressWarnings("rawtypes")
		BooleanJunction<BooleanJunction> junction = qb.bool();
		junction.must(qb.all().createQuery());

		junction.must(qb.keyword().onField("drafted").ignoreAnalyzer().matching("_null_").createQuery());

		if (StringUtils.hasText(request.getKeyword())) {
			Analyzer analyzer = fullTextEntityManager.getSearchFactory().getAnalyzer("synonyms");
			String[] fields = new String[] {
					"title", "body",
					"categories.name", "tags.name",
			};
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_36, fields, analyzer);
			parser.setDefaultOperator(Operator.AND);
			Query query = null;
			try {
				query = parser.parse(request.getKeyword());
			}
			catch (ParseException e1) {
				try {
					query = parser.parse(QueryParser.escape(request.getKeyword()));
				}
				catch (ParseException e2) {
					throw new RuntimeException(e2);
				}
			}
			junction.must(query);
		}
		if (request.getStatus() != null) {
			junction.must(qb.keyword().onField("status").matching(request.getStatus()).createQuery());
		}
		if (StringUtils.hasText(request.getLanguage())) {
			junction.must(qb.keyword().onField("language").matching(request.getLanguage()).createQuery());
		}

		if (request.getDateFrom() != null) {
			junction.must(qb.range().onField("date").above(request.getDateFrom()).createQuery());
		}
		if (request.getDateTo() != null) {
			junction.must(qb.range().onField("date").below(request.getDateTo()).createQuery());
		}

		if (!CollectionUtils.isEmpty(request.getCategoryIds())) {
			for (long categoryId : request.getCategoryIds()) {
				junction.must(qb.keyword().onField("categories.id").matching(categoryId).createQuery());
			}
		}
		if (!CollectionUtils.isEmpty(request.getCategoryCodes())) {
			for (String categoryCode : request.getCategoryCodes()) {
				junction.must(qb.keyword().onField("categories.code").matching(categoryCode).createQuery());
			}
		}

		if (!CollectionUtils.isEmpty(request.getTagIds())) {
			for (long tagId : request.getTagIds()) {
				junction.must(qb.keyword().onField("tags.id").matching(tagId).createQuery());
			}
		}
		if (!CollectionUtils.isEmpty(request.getTagNames())) {
			for (String tagName : request.getTagNames()) {
				junction.must(qb.keyword().onField("tags.name").matching(tagName).createQuery());
			}
		}

		if (request.getAuthorId() != null) {
			junction.must(qb.keyword().onField("author.id").matching(request.getAuthorId()).createQuery());
		}

		Query searchQuery = junction.createQuery();
		
		Session session = (Session) entityManager.getDelegate();
		Criteria criteria = session.createCriteria(Article.class)
				.setFetchMode("cover", FetchMode.JOIN)
				.setFetchMode("user", FetchMode.JOIN)
				.setFetchMode("categories", FetchMode.JOIN);
//				.setFetchMode("tags", FetchMode.JOIN);

		Sort sort = new Sort(
				new SortField("date", SortField.STRING, true),
				new SortField("id", SortField.LONG, true));

		FullTextQuery persistenceQuery = fullTextEntityManager
				.createFullTextQuery(searchQuery, Article.class)
				.setCriteriaQuery(criteria)
				.setSort(sort);
		persistenceQuery.setFirstResult(pageable.getOffset());
		persistenceQuery.setMaxResults(pageable.getPageSize());

		int resultSize = persistenceQuery.getResultSize();

		@SuppressWarnings("unchecked")
		List<Article> results = persistenceQuery.getResultList();
		return new PageImpl<>(results, pageable, resultSize);
	}
}
