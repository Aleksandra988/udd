package com.udd.repository;

import com.udd.lucene.model.IndexUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<IndexUnit, String> {

	List<IndexUnit> findByTitle(String title);

	IndexUnit findByFilename(String filename);
}
