package com.udd.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.udd.lucene.model.*;
import com.udd.lucene.search.QueryBuilder;
import com.udd.lucene.search.ResultRetriever;
import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

		@Autowired
		private ResultRetriever resultRetriever;
		
		@PostMapping(value="/search/boolean", consumes="application/json")
		public ResponseEntity<List<ResultDataApplication>> searchBoolean(@RequestBody AdvancedQueryApplication advancedQuery) throws Exception {
			if(isFieldsEmpty(advancedQuery))
				return new ResponseEntity<List<ResultDataApplication>>((List<ResultDataApplication>) null, HttpStatus.OK);
			SimpleQuery simpleQuery = isQuerySimple(advancedQuery);
			if(simpleQuery != null){

				org.elasticsearch.index.query.QueryBuilder query= QueryBuilders.matchQuery(simpleQuery.getField(),
						simpleQuery.getValue().toLowerCase(Locale.ROOT));
				List<ResultDataApplication> results = resultRetriever.getResultsApplication(query);
				return new ResponseEntity<List<ResultDataApplication>>(results, HttpStatus.OK);
			}else {

				BoolQueryBuilder builder = buildQuery(advancedQuery);
				List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
				List<ResultDataApplication> results = resultRetriever.getResultsApplication(builder);

				return new ResponseEntity<List<ResultDataApplication>>(results, HttpStatus.OK);
			}
		}

	private boolean isFieldsEmpty(AdvancedQueryApplication advancedQuery) {
			return advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameValue() == ""
					&& advancedQuery.getEducationValue() == "" && advancedQuery.getContentValue() == "";
	}

	private BoolQueryBuilder buildQuery(AdvancedQueryApplication advancedQuery) throws ParseException {
			org.elasticsearch.index.query.QueryBuilder query1 = null;
			org.elasticsearch.index.query.QueryBuilder query2 = null;
			org.elasticsearch.index.query.QueryBuilder query3 = null;
			org.elasticsearch.index.query.QueryBuilder query4 = null;

			query1 = getQuery(advancedQuery.getFirstnameField(), advancedQuery.getFirstnameValue().toLowerCase(Locale.ROOT), advancedQuery.getFirstnameIsPhrase());
			query2 = getQuery(advancedQuery.getLastnameField(), advancedQuery.getLastnameValue().toLowerCase(Locale.ROOT), advancedQuery.getLastnameIsPhrase());
			query3 = getQuery(advancedQuery.getEducationField(), advancedQuery.getEducationValue().toLowerCase(Locale.ROOT), advancedQuery.getEducationIsPhrase());
			query4 = getQuery(advancedQuery.getContentField(), advancedQuery.getContentValue().toLowerCase(Locale.ROOT), advancedQuery.getContentIsPhrase());

			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			if(query1 != null) {
				if (advancedQuery.getFirstnameOperation().equalsIgnoreCase("AND"))
					builder.must(query1);
				else if (advancedQuery.getFirstnameOperation().equalsIgnoreCase("OR"))
					builder.should(query1);
				else if(advancedQuery.getFirstnameOperation().equalsIgnoreCase("NOT"))
					builder.mustNot(query1);
			}
			if(query2 != null) {
				if (advancedQuery.getLastnameOperation().equalsIgnoreCase("AND"))
					builder.must(query2);
				else if (advancedQuery.getLastnameOperation().equalsIgnoreCase("OR"))
					builder.should(query2);
				else if(advancedQuery.getLastnameOperation().equalsIgnoreCase("NOT"))
					builder.mustNot(query2);
			}
			if(query3 != null) {
				if (advancedQuery.getEducationOperation().equalsIgnoreCase("AND"))
					builder.must(query3);
				else if (advancedQuery.getEducationOperation().equalsIgnoreCase("OR"))
					builder.should(query3);
				else if(advancedQuery.getEducationOperation().equalsIgnoreCase("NOT"))
					builder.mustNot(query3);
			}
			if(query4 != null) {
				if (advancedQuery.getContentOperation().equalsIgnoreCase("AND"))
					builder.must(query4);
				else if (advancedQuery.getContentOperation().equalsIgnoreCase("OR"))
					builder.should(query4);
				else if(advancedQuery.getContentOperation().equalsIgnoreCase("NOT"))
					builder.mustNot(query4);
			}
			return builder;
		}



	private org.elasticsearch.index.query.QueryBuilder getQuery(String field, String value, boolean isPhrase) throws ParseException {
		org.elasticsearch.index.query.QueryBuilder query = null;
		if (field != "" && value != "") {
			if(isPhrase)
				query = QueryBuilders.matchPhraseQuery(field, value);
			else
				query = QueryBuilders.matchQuery(field, value);
		}
		return query;
	}

	private SimpleQuery isQuerySimple(AdvancedQueryApplication advancedQuery) {
			if(advancedQuery.getFirstnameValue() != "" && advancedQuery.getLastnameValue() == ""
					&& advancedQuery.getEducationValue() == "" && advancedQuery.getContentValue() == "")
				return new SimpleQuery(advancedQuery.getFirstnameField(), advancedQuery.getFirstnameValue(), true);
			else if(advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameValue() != ""
					&& advancedQuery.getEducationValue() == "" && advancedQuery.getContentValue() == "")
				return new SimpleQuery(advancedQuery.getLastnameField(), advancedQuery.getLastnameValue(), true);
			else if(advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameValue() == ""
					&& advancedQuery.getEducationValue() != "" && advancedQuery.getContentValue() == "")
				return new SimpleQuery(advancedQuery.getEducationField(), advancedQuery.getEducationValue(), true);
			else if(advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameValue() == ""
					&& advancedQuery.getEducationValue() == "" && advancedQuery.getContentValue() != "")
				return new SimpleQuery(advancedQuery.getContentField(), advancedQuery.getContentValue(), true);
			else
				return null;
	}

	@PostMapping("/geoSearch")
	public ResponseEntity geoSearch(@RequestBody GeoSearchDTO geoSearchDTO) throws IOException {

		List<ResultDataApplication> searchResultDTOList = resultRetriever.geoSearch(geoSearchDTO);
		return new ResponseEntity(searchResultDTOList, HttpStatus.OK);
	}
	
		
	
}
