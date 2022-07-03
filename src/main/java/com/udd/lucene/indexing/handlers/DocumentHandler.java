package com.udd.lucene.indexing.handlers;

import java.io.File;

import com.udd.lucene.model.Application;

public abstract class DocumentHandler {
	/**
	 * Od prosledjene datoteke se konstruise Lucene Document
	 * 
	 * @param file
	 *            datoteka u kojoj se nalaze informacije
	 * @return Lucene Document
	 */
	//public abstract IndexUnit getIndexUnit(File file);
	public abstract Application getApplication(File file);
	public abstract String getText(File file);

}
