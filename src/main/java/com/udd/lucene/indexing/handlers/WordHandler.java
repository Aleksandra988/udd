package com.udd.lucene.indexing.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import com.udd.lucene.model.Application;
import com.udd.lucene.model.IndexUnit;
import org.apache.lucene.document.DateTools;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class WordHandler extends DocumentHandler {

	public IndexUnit getIndexUnit(File file) {
		IndexUnit retVal = new IndexUnit();
		InputStream is;

		try {
			is = new FileInputStream(file);
			// pomocu WordExtractor objekta izvuci tekst
			WordExtractor we = new WordExtractor(is);
			String text = we.getText();
			retVal.setText(text);

			// pomocu SummaryInformation objekta izvuci ostale metapodatke
			SummaryInformation si = we.getSummaryInformation();
			String title = si.getTitle();
			retVal.setTitle(title);

			String keywords = si.getKeywords();
			retVal.setKeywords(keywords);

			retVal.setFilename(file.getCanonicalPath());

			String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
			retVal.setFiledate(modificationDate);

			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Dokument ne postoji");
		} catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}

		return retVal;
	}

    public Application getApplication(File file) {
        Application retVal = new Application();
        InputStream is;

        try {
            is = new FileInputStream(file);
            // pomocu WordExtractor objekta izvuci tekst
            WordExtractor we = new WordExtractor(is);
            String text = we.getText();
            retVal.setContent(text);

            // pomocu SummaryInformation objekta izvuci ostale metapodatke
            SummaryInformation si = we.getSummaryInformation();
            String title = si.getTitle();
            //retVal.setTitle(title);

            String keywords = si.getKeywords();
          //  retVal.setKeywords(keywords);

           // retVal.setFilename(file.getCanonicalPath());

            String modificationDate=DateTools.dateToString(new Date(file.lastModified()),DateTools.Resolution.DAY);
           // retVal.setFiledate(modificationDate);

            we.close();
        } catch (FileNotFoundException e1) {
            System.out.println("Dokument ne postoji");
        } catch (Exception e) {
            System.out.println("Problem pri parsiranju doc fajla");
        }

        return retVal;
    }

	@Override
	public String getText(File file) {
		String text = null;
		try {
			WordExtractor we = new WordExtractor(new FileInputStream(file));
			text = we.getText();
			we.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Dokument ne postoji");
		} catch (Exception e) {
			System.out.println("Problem pri parsiranju doc fajla");
		}
		return text;
	}

}
