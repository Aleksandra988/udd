package com.udd.lucene.indexing.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.udd.lucene.model.Application;
import com.udd.lucene.model.IndexUnit;
import org.apache.lucene.document.DateTools;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class TextDocHandler extends DocumentHandler {

    public Application getApplication(File file) {
        Application retVal = new Application();
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(
                    fis, "UTF8"));

            String firstLine = reader.readLine(); // u prvoj liniji svake
            // tekstualne datoteke se
            // nalazi naslov rada

            //retVal.setTitle(firstLine);

            /*
             * add other custom metadata
             */

            String secondLine = reader.readLine();
            //retVal.setKeywords(secondLine);

            String fullText = "";
            while (true) {
                secondLine = reader.readLine();
                if (secondLine == null) {
                    break;
                }
                fullText += " " + secondLine;
            }
            retVal.setContent(fullText);

           // retVal.setFilename(file.getCanonicalPath());

            String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
            //retVal.setFiledate(modificationDate);

            return retVal;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Datoteka ne postoji");
        } catch (IOException e) {
            throw new IllegalArgumentException("Greska: Datoteka nije u redu");
        } finally {
            if(reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                }
        }
    }

		@Override
	public IndexUnit getIndexUnit(File file) {
		IndexUnit retVal = new IndexUnit();
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, "UTF8"));

			String firstLine = reader.readLine(); // u prvoj liniji svake
													// tekstualne datoteke se
													// nalazi naslov rada

			retVal.setTitle(firstLine);

			/*
			 * add other custom metadata
			 */

			String secondLine = reader.readLine();
			retVal.setKeywords(secondLine);

			String fullText = "";
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText += " " + secondLine;
			}
			retVal.setText(fullText);

			retVal.setFilename(file.getCanonicalPath());

			String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
			retVal.setFiledate(modificationDate);

			return retVal;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
	}

	@Override
	public String getText(File file) {
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, "UTF8"));
			String secondLine;
			String fullText = "";
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText += " " + secondLine;
			}
			return fullText;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
	}

}
