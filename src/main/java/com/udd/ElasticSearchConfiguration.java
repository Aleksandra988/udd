package com.udd;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.node.Node;
//import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "rs.ac.uns.ftn.informatika.udd.vezbe04.repository")
@ComponentScan(basePackages = {"rs.ac.uns.ftn.informatika.udd.vezbe04"})
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {
	@Value("${elasticsearch.url}")
	public String elasticsearchUrl;

	@Bean
	@Override
	public RestHighLevelClient elasticsearchClient() {
		final ClientConfiguration config = ClientConfiguration.builder()
				.connectedTo(elasticsearchUrl)
				.build();

		return RestClients.create(config).rest();
	}

	@Bean
	public ElasticsearchRestTemplate elasticsearchRestTemplate() {
		return new ElasticsearchRestTemplate(elasticsearchClient());
	}


//		@Value("${elasticsearch.host}")
//	    private String EsHost;
//
//	    @Value("${elasticsearch.port}")
//	    private int EsPort;
//
//	    @Value("${elasticsearch.clustername}")
//	    private String EsClusterName;
//
//		public Client nodeClient() {
//			Settings settings = Settings.builder()
//					.put("path.home", "data")
//					.build();
//
//			final Node node = new NodeBuilder()
//					.settings(settings)
//					.local(true)
//			        .build().start();
//
//			return node.client();
//		}
//
//	    @Bean
//	    public ElasticsearchOperations elasticsearchTemplate() {
//	        return new ElasticsearchTemplate(nodeClient());
//	    }


}
