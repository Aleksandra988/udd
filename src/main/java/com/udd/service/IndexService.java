package com.udd.service;

import com.udd.lucene.indexing.handlers.*;
import com.udd.lucene.model.IndexUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.udd.lucene.indexing.handlers.*;
import com.udd.repository.BookRepository;

import java.io.File;

@Service
public class IndexService {
    private final BookRepository bookRepository;

    @Autowired
    public IndexService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public boolean save(final IndexUnit indexUnit){
        IndexUnit unit = bookRepository.save(indexUnit);
        if(unit!=null)
            return true;
        else
            return false;
    }

    public int index(File file){
        DocumentHandler handler = null;
        String fileName = null;
        int retVal = 0;
        try {
            File[] files;
            if(file.isDirectory()){
                files = file.listFiles();
            }else{
                files = new File[1];
                files[0] = file;
            }
            for(File newFile : files){
                if(newFile.isFile()){
                    fileName = newFile.getName();
                    handler = getHandler(fileName);
                    if(handler == null){
                        System.out.println("Nije moguce indeksirati dokument sa nazivom: " + fileName);
                        continue;
                    }
                    if(save(handler.getIndexUnit(newFile)))
                        retVal++;
                } else if (newFile.isDirectory()){
                    retVal += index(newFile);
                }
            }
            System.out.println("indexing done");
        } catch (Exception e) {
            System.out.println("indexing NOT done");
        }
        return retVal;
    }

    public DocumentHandler getHandler(String fileName){
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
}
