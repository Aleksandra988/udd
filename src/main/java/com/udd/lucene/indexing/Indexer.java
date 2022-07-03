package com.udd.lucene.indexing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import com.udd.lucene.indexing.handlers.DocumentHandler;
import com.udd.lucene.indexing.handlers.PDFHandler;
//import com.udd.lucene.indexing.handlers.TextDocHandler;
//import com.udd.lucene.indexing.handlers.Word2007Handler;
//import com.udd.lucene.indexing.handlers.WordHandler;

@Service
public class Indexer {



	private ElasticsearchRestTemplate template;

	@Autowired
	public Indexer(ElasticsearchRestTemplate elasticsearchTemplate) {
		this.template = elasticsearchTemplate;
	}


//	public boolean delete(String filename){
//		String retVal = template.delete(filename, IndexUnit.class);
//		if(filename.equals(retVal))
//			return true;
//		else
//			return false;
//
//	}
//
//	public boolean add(IndexUnit unit){
//		IndexQuery indexQuery = new IndexQueryBuilder()
//				.withObject(unit)
//				.withId(unit.getFilename())
//				.build();
//
//		String documentId = template.index(indexQuery, IndexCoordinates.of("nameofindex"));
//		if(documentId.equals(unit.getFilename()))
//			return true;
//		else
//			return false;
//	}
//
//	/**
//	 *
//	 * @param file Direktorijum u kojem se nalaze dokumenti koje treba indeksirati
//	 */
//	public int index(File file){
//		DocumentHandler handler = null;
//		String fileName = null;
//		List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
//		int retVal = 0;
//		try {
//			File[] files;
//			if(file.isDirectory()){
//				files = file.listFiles();
//			}else{
//				files = new File[1];
//				files[0] = file;
//			}
//			for(File newFile : files){
//				if(newFile.isFile()){
//					fileName = newFile.getName();
//					handler = getHandler(fileName);
//					if(handler == null){
//						System.out.println("Nije moguce indeksirati dokument sa nazivom: " + fileName);
//						continue;
//					}
//					indexQueries.add(new IndexQueryBuilder()
//							.withObject(handler.getIndexUnit(newFile))
//							.withId(handler.getIndexUnit(newFile).getFilename())
//							.build());
//				} else if (newFile.isDirectory()){
//					retVal += index(newFile);
//				}
//			}
////			BulkOptions.BulkOptionsBuilder defaultOptions = new BulkOptions.BulkOptionsBuilder;
////			template.bulkIndex(indexQueries);
//			System.out.println("indexing done");
//			retVal += indexQueries.size();
//		} catch (Exception e) {
//			System.out.println("indexing NOT done");
//		}
//		return retVal;
//	}


	public DocumentHandler getHandler(String fileName){
		if(fileName.endsWith(".pdf")){
			return new PDFHandler();
//		}else if(fileName.endsWith(".txt")){
//			return new TextDocHandler();
//		}else if(fileName.endsWith(".doc")){
//			return new WordHandler();
//		}else if(fileName.endsWith(".docx")){
//			return new Word2007Handler();
		}else{
			return null;
		}
	}

}