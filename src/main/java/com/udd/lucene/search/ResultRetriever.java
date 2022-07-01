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
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Service
public class ResultRetriever {
	
	private ElasticsearchRestTemplate template;
	
	@Autowired
	public ResultRetriever(ElasticsearchRestTemplate template){
		this.template = template;
	}

	public List<ResultDataApplication> getResultsApplication(org.elasticsearch.index.query.QueryBuilder query,
																				  List<RequiredHighlight> requiredHighlights) {
		if (query == null) {
			return null;
		}

		List<ResultDataApplication> results = new ArrayList<ResultDataApplication>();

		HighlightBuilder highlightBuilder = new HighlightBuilder().highlighterType("plain").field("content").preTags("<b>").postTags("</b>");
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(query)
				.withHighlightBuilder(highlightBuilder)
				.build();

		SearchHits<Application> applications = template.search(searchQuery, Application.class, IndexCoordinates.of("index_application"));

		for (var ap : applications) {
			var unit = ap.getContent();
			String highlight = createHighlight(ap);
			Path p = Paths.get(unit.getFilename());
			String filename = p.getFileName().toString();
			results.add(new ResultDataApplication(unit.getFirstname(), unit.getLastname(), unit.getEducation(), filename, unit.getLocation().toString(),
					highlight));
		}


		return results;
	}

	private String createHighlight(SearchHit<Application> ap) {
		var unit = ap.getContent();
		String highlight = "";
		if (ap.getHighlightFields().isEmpty()) {
			highlight = unit.getContent().substring(0, 150) + "...";
		} else {
			highlight = "..." + ap.getHighlightFields().get("content").get(0) + "...";
		}
		return highlight;
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


		SearchHits<Application> searchHits =  this.template.search(searchQuery, Application.class, IndexCoordinates.of("index_application"));
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
			searchResultDTO.setLocation(unit.getLocation().toString());

			results.add(searchResultDTO);

		}
		return results;

	}

//	public void getStatistic(){
////		TermsAggregationBuilder aggregation = AggregationBuilders.terms("top_tags")
////				.field("city")
////				.size(1)
////				.minDocCount(1);
////				//.order(Terms.Order.count(false));
////		SearchSourceBuilder builder = new SearchSourceBuilder().aggregation(aggregation);
////
////		SearchRequest searchRequest = new SearchRequest().indices("blog").source(builder);
////		//SearchHits<Application> response = template.search(searchRequest, Application.class, IndexCoordinates.of("index_application"));
////		SearchResponse response = template.prepareSearch("test").setSearchType(SearchType.regular).addAggregation(aggregation).execute().actionGet();
////
////		Map<String, Aggregation> results = response.getAggregations().asMap();
////		StringTerms topTags = (StringTerms) results.get("top_tags");
////
////		List<String> keys = topTags.getBuckets()
////				.stream()
////				.map(b -> b.getKeyAsString())
////				.collect(toList());
////		//assertEquals(asList("elasticsearch", "spring data", "search engines", "tutorial"), keys);
//		RestHighLevelClient client = HighLevelClient.getInstance();
//		TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("top_cmd").
//				field("city").
//				size(3);
//				//aggregationBuilder.subAggregation(histogramAggregationBuilder);
//
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().aggregation(aggregationBuilder);
//		SearchRequest searchRequest = new SearchRequest("serverlog_20180710").source(searchSourceBuilder);
//
//		SearchResponse searchResponse = client.search(searchRequest);
//            ;
//	}
}
