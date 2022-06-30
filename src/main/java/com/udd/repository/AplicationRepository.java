package com.udd.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.udd.lucene.model.Application;

public interface AplicationRepository extends ElasticsearchRepository<Application, String> {
}
