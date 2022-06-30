package com.udd.lucene.search;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.udd.lucene.model.*;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.udd.lucene.indexing.handlers.DocumentHandler;
import com.udd.lucene.indexing.handlers.PDFHandler;
import com.udd.lucene.indexing.handlers.TextDocHandler;
import com.udd.lucene.indexing.handlers.Word2007Handler;
import com.udd.lucene.indexing.handlers.WordHandler;

@Service
public class ResultRetriever {
	
	private ElasticsearchRestTemplate template;
	
	@Autowired
	public ResultRetriever(ElasticsearchRestTemplate template){
		this.template = template;
	}

	public List<ResultData> getResults(org.elasticsearch.index.query.QueryBuilder query,
			List<RequiredHighlight> requiredHighlights) {
		if (query == null) {
			return null;
		}
			
		List<ResultData> results = new ArrayList<ResultData>();

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(query)
				.build();
       
        var indexUnits = template.search(searchQuery, IndexUnit.class, IndexCoordinates.of("nameofindex"));

        for (var indexUnit : indexUnits) {
			var iu = indexUnit.getContent();
        	results.add(new ResultData(iu.getTitle(), iu.getKeywords(), iu.getFilename(), ""));
		}
        
		
		return results;
	}

	private Map<String, Float> createMap(){
		Map<String, Float> map = new HashMap<>();
		map.put("firstName", 1.0f);
		map.put("lastName", 1.0f);
		map.put("education",1.0f);
		map.put("content",1.0f);

		return map;
	}

	public List<ResultDataApplication> getResultsApplication(/*SimpleQuery query*/org.elasticsearch.index.query.QueryBuilder query,
																				  List<RequiredHighlight> requiredHighlights) {
		if (query == null) {
			return null;
		}

		List<ResultDataApplication> results = new ArrayList<ResultDataApplication>();
//
//		MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery(query.getField(), query.getValue());
//		//Map<String, Float> map = createMap();
//		//queryBuilder.must(QueryBuilders.multiMatchQuery(query.getValue()).fields(map));
//		HighlightBuilder highlightBuilder = new HighlightBuilder().highlighterType("plain").field("cvContent").preTags("<b>").postTags("</b>");
//		//queryBuilder.must(QueryBuilders.matchQuery(query.getField(),query.getValue()));
//		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
//		/*new NativeSearchQueryBuilder()
//				.withQuery(queryBuilder)
//				.withHighlightBuilder(highlightBuilder)
//				.build();*/
//		//logger.info("Basic search - search query");

//		SearchHits<Application> applications =  this.template.search(searchQuery, Application.class, IndexCoordinates.of("application"));
		//logger.info("Basic search - search hits");
		HighlightBuilder highlightBuilder = new HighlightBuilder().highlighterType("plain").field("content").preTags("<b>").postTags("</b>");
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(query)
				.withHighlightBuilder(highlightBuilder)
				.build();

		SearchHits<Application> applications = template.search(searchQuery, Application.class, IndexCoordinates.of("application"));

		for (var ap : applications) {
			var unit = ap.getContent();
			String highlight = "";
			if (ap.getHighlightFields().isEmpty()) {
				highlight = unit.getContent().substring(0, 150) + "...";
			} else {
				highlight = "..." + ap.getHighlightFields().get("content").get(0) + "...";
			}
			Path p = Paths.get(unit.getFilename());
			String filename = p.getFileName().toString();
			results.add(new ResultDataApplication(unit.getFirstname(), unit.getLastname(), unit.getEducation(), filename, unit.getLocation().toString(), highlight));
		}


		return results;
	}

	private HighlightBuilder highlightBuilder(SimpleQuery query){
		//logger.info("Advanced search - highlight builder");

		HighlightBuilder highlightBuilder = new HighlightBuilder();
		if(query.getField().equals("all")){
			//logger.info("Highlight builder for ALL ");
			return  highlightBuilder.field("firstName").field("lastName").field("education");
		}else {
			//logger.info("Highlight builder for specific field: " + query.getField());
			return highlightBuilder.field(query.getField());
		}
	}
	
	protected DocumentHandler getHandler(String fileName){
		if(fileName.endsWith(".pdf")){
			return new PDFHandler();
		}else if(fileName.endsWith(".txt")){
			return new TextDocHandler();
		}else if(fileName.endsWith(".doc")){
			return new WordHandler();
		}else if(fileName.endsWith(".docx")){
			return new Word2007Handler();
		}else{
			return null;
		}
	}

	public List<ResultDataApplication> geoSearch(GeoSearchDTO geoSearchDTO) throws IOException {

		JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("925c9f60dd9b42088219fe26476d8746");
		String query = geoSearchDTO.getCity() + ", " + geoSearchDTO.getCountry();
		JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
		JOpenCageResponse response = jOpenCageGeocoder.forward(request);
		JOpenCageLatLng locationLatLng = response.getFirstPosition();

		HighlightBuilder highlightBuilder = new HighlightBuilder().highlighterType("plain").field("content").preTags("<b>").postTags("</b>");
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.geoDistanceQuery("location")
						.point(locationLatLng.getLat(),locationLatLng.getLng())
						.distance(geoSearchDTO.getDistance()))
				.withHighlightBuilder(highlightBuilder)
				.build();
		System.out.println("Geo search - query");


		SearchHits<Application> searchHits =  this.template.search(searchQuery, Application.class, IndexCoordinates.of("application"));
		//logger.info("Geo search - search hits");

		List<ResultDataApplication> results = new ArrayList<>();

		for(SearchHit<Application> ap : searchHits) {
			var unit = ap.getContent();
			String highlight = "";
			if (ap.getHighlightFields().isEmpty()) {
				highlight = unit.getContent().substring(0, 150) + "...";
			} else {
				highlight = "..." + ap.getHighlightFields().get("content").get(0) + "...";
			}
			Path p = Paths.get(unit.getFilename());
			String filename = p.getFileName().toString();
			ResultDataApplication searchResultDTO = new ResultDataApplication();
			searchResultDTO.setFirstName(unit.getFirstname());
			searchResultDTO.setLastName(unit.getLastname());
			searchResultDTO.setEducation(unit.getEducation());
			searchResultDTO.setFilename(filename);
			searchResultDTO.setHighlight(highlight);
			//searchResultDTO.setContent(hit.getContent().getContent());
			//searchResultDTO.setId(hit.getContent().getId());
			searchResultDTO.setLocation(unit.getLocation().toString());

			//logger.info("Geo search - found hit with id - " + hit.getContent().getId());
			results.add(searchResultDTO);

		}

		//logger.info("Geo search - found search hits - " + results.size());
		return results;

	}
}
