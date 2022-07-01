package com.udd.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
		@PostMapping(value="/search/term", consumes="application/json")
		public ResponseEntity<List<ResultDataApplication>> searchTermQuery(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.regular, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultDataApplication> results = resultRetriever.getResultsApplication(query, rh);
			//List<ResultDataApplication> results = resultRetriever.getResultsApplication(simpleQuery);
			return new ResponseEntity<List<ResultDataApplication>>(results, HttpStatus.OK);
		}
		
		@PostMapping(value="/search/fuzzy", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchFuzzy(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.fuzzy, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);			
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}
		
		@PostMapping(value="/search/prefix", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchPrefix(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.prefix, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);			
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}
		
		@PostMapping(value="/search/range", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchRange(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.range, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);			
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}
		
		@PostMapping(value="/search/phrase", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchPhrase(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.phrase, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);			
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}
		
		@PostMapping(value="/search/boolean", consumes="application/json")
		public ResponseEntity<List<ResultDataApplication>> searchBoolean(@RequestBody AdvancedQueryApplication advancedQuery) throws Exception {
			if(isFieldsEmpty(advancedQuery))
				return new ResponseEntity<List<ResultDataApplication>>((List<ResultDataApplication>) null, HttpStatus.OK);
			if(isQuerySimple(advancedQuery)){
				org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.regular, choseField(advancedQuery), choseValue(advancedQuery));
				List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
				rh.add(new RequiredHighlight(choseField(advancedQuery), choseValue(advancedQuery)));
				List<ResultDataApplication> results = resultRetriever.getResultsApplication(query, rh);
				return new ResponseEntity<List<ResultDataApplication>>(results, HttpStatus.OK);
			}else {

				BoolQueryBuilder builder = buildQuery(advancedQuery);

				List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();

				if (advancedQuery.getFirstnameField() != "" && advancedQuery.getFirstnameValue() != "")
					rh.add(new RequiredHighlight(advancedQuery.getFirstnameField(), advancedQuery.getFirstnameValue()));
				if (advancedQuery.getLastnameField() != "" && advancedQuery.getLastnameValue() != "")
					rh.add(new RequiredHighlight(advancedQuery.getLastnameField(), advancedQuery.getLastnameValue()));
				if (advancedQuery.getEducationField() != "" && advancedQuery.getEducationValue() != "")
					rh.add(new RequiredHighlight(advancedQuery.getEducationField(), advancedQuery.getEducationValue()));
				if (advancedQuery.getContentField() != "" && advancedQuery.getContentValue() != "")
					rh.add(new RequiredHighlight(advancedQuery.getContentField(), advancedQuery.getContentValue()));
				List<ResultDataApplication> results = resultRetriever.getResultsApplication(builder, rh);

				return new ResponseEntity<List<ResultDataApplication>>(results, HttpStatus.OK);
			}
		}

	private boolean isFieldsEmpty(AdvancedQueryApplication advancedQuery) {
			return advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() == "" &&
					advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
					advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "";
	}

	private BoolQueryBuilder buildQuery(AdvancedQueryApplication advancedQuery) throws ParseException {
			org.elasticsearch.index.query.QueryBuilder query1 = null;
			org.elasticsearch.index.query.QueryBuilder query2 = null;
			org.elasticsearch.index.query.QueryBuilder query3 = null;
			org.elasticsearch.index.query.QueryBuilder query4 = null;

			query1 = getQuery(advancedQuery.getFirstnameField(), advancedQuery.getFirstnameValue(), advancedQuery.getFirstnameIsPhrase());
			query2 = getQuery(advancedQuery.getLastnameField(), advancedQuery.getLastnameValue(), advancedQuery.getLastnameIsPhrase());
			query3 = getQuery(advancedQuery.getEducationField(), advancedQuery.getEducationValue(), advancedQuery.getEducationIsPhrase());
			query4 = getQuery(advancedQuery.getContentField(), advancedQuery.getContentValue(), advancedQuery.getContentIsPhrase());

			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			if (advancedQuery.getOperation().equalsIgnoreCase("AND")) {
				if(query1 != null)
					builder.must(query1);
				if (query2 != null)
					builder.must(query2);
				if (query3 != null)
					builder.must(query3);
				if (query4 != null)
					builder.must(query4);
			} else if (advancedQuery.getOperation().equalsIgnoreCase("OR")) {
				if(query1 != null)
					builder.should(query1);
				if (query2 != null)
					builder.should(query2);
				if (query3 != null)
					builder.should(query3);
				if (query4 != null)
					builder.should(query4);
			} else if (advancedQuery.getOperation().equalsIgnoreCase("NOT")) {
				if(query1 != null)
					builder.must(query1);
				if (query2 != null)
					builder.mustNot(query2);
				if (query3 != null)
					builder.mustNot(query3);
				if (query4 != null)
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
				query = QueryBuilder.buildQuery(SearchType.regular, field, value);
		}
		return query;
	}

	private boolean isQuerySimple(AdvancedQueryApplication advancedQuery) {
			if(advancedQuery.getFirstnameField() != "" && advancedQuery.getFirstnameValue() != "" && advancedQuery.getLastnameField() == "" &&
				advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
				advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
				return true;
			else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() != "" &&
					advancedQuery.getLastnameValue() != "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
					advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
				return true;
			else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() == "" &&
					advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() != "" && advancedQuery.getEducationValue() != "" &&
					advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
				return true;
			else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() == "" &&
					advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
					advancedQuery.getContentField() != "" && advancedQuery.getContentValue() != "")
				return true;
			else
				return false;
	}

	private String choseField(AdvancedQueryApplication advancedQuery){
		if(advancedQuery.getFirstnameField() != "" && advancedQuery.getFirstnameValue() != "" && advancedQuery.getLastnameField() == "" &&
				advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
				advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
			return advancedQuery.getFirstnameField();
		else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() != "" &&
				advancedQuery.getLastnameValue() != "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
				advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
			return advancedQuery.getLastnameField();
		else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() == "" &&
				advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() != "" && advancedQuery.getEducationValue() != "" &&
				advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
			return advancedQuery.getEducationField();
		else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() == "" &&
				advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
				advancedQuery.getContentField() != "" && advancedQuery.getContentValue() != "")
			return advancedQuery.getContentField();
		else
			return null;
	}

	private String choseValue(AdvancedQueryApplication advancedQuery){
		if(advancedQuery.getFirstnameField() != "" && advancedQuery.getFirstnameValue() != "" && advancedQuery.getLastnameField() == "" &&
				advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
				advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
			return advancedQuery.getFirstnameValue();
		else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() != "" &&
				advancedQuery.getLastnameValue() != "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
				advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
			return advancedQuery.getLastnameValue();
		else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() == "" &&
				advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() != "" && advancedQuery.getEducationValue() != "" &&
				advancedQuery.getContentField() == "" && advancedQuery.getContentValue() == "")
			return advancedQuery.getEducationValue();
		else if(advancedQuery.getFirstnameField() == "" && advancedQuery.getFirstnameValue() == "" && advancedQuery.getLastnameField() == "" &&
				advancedQuery.getLastnameValue() == "" && advancedQuery.getEducationField() == "" && advancedQuery.getEducationValue() == "" &&
				advancedQuery.getContentField() != "" && advancedQuery.getContentValue() != "")
			return advancedQuery.getContentValue();
		else
			return null;
	}

	@PostMapping(value="/search/queryParser", consumes="application/json")
		public ResponseEntity<List<ResultData>> search(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query=QueryBuilders.queryStringQuery(simpleQuery.getValue());			
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

	@PostMapping("/geoSearch")
	public ResponseEntity geoSearch(@RequestBody GeoSearchDTO geoSearchDTO) throws IOException {
		//logger.info("Geo search");


		//logger.info("Passed geo search validation");
		List<ResultDataApplication> searchResultDTOList = resultRetriever.geoSearch(geoSearchDTO);

		//logger.info("Successfully finished geo search");
		return new ResponseEntity(searchResultDTOList, HttpStatus.OK);


	}
	
		
	
}
