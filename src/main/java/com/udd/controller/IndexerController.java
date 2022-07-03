package com.udd.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.ResourceBundle;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.udd.lucene.model.Application;
import com.udd.lucene.model.UploadModelApplication;
import com.udd.service.IndexApplicationService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.udd.lucene.indexing.Indexer;

@RestController
public class IndexerController {

		private static String DATA_DIR_PATH;
	
		static {
			ResourceBundle rb=ResourceBundle.getBundle("application");
			DATA_DIR_PATH=rb.getString("dataDir");
		}
		
		@Autowired
		private Indexer indexer;
//	@Autowired
//	private IndexService indexService;

	@Autowired
	private IndexApplicationService indexApplicationService;

//		@GetMapping("/reindex")
//		public ResponseEntity<String> index() throws IOException {
//			File dataDir = getResourceFilePath(DATA_DIR_PATH);
//			long start = new Date().getTime();
//			int numIndexed = indexService.index(dataDir);
//			long end = new Date().getTime();
//			String text = "Indexing " + numIndexed + " files took "
//					+ (end - start) + " milliseconds";
//			return new ResponseEntity<String>(text, HttpStatus.OK);
//		}
		
		private File getResourceFilePath(String path) {
		    URL url = this.getClass().getClassLoader().getResource(path);
		    File file = null;
		    try {
		        file = new File(url.toURI());
		    } catch (URISyntaxException e) {
		        file = new File(url.getPath());
		    }   
		    return file;
		}
		


	    @PostMapping("/index/add")
	    public ResponseEntity<String> multiUploadFileModel(@ModelAttribute UploadModelApplication model) {

	        try {
	        	indexUploadedFile(model);

	        } catch (IOException e) {
	            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity<String>("Successfully uploaded!", HttpStatus.OK);
	    }
		    
		    
	    //save file
	    private String saveUploadedFile(MultipartFile file) throws IOException {
	    	String retVal = null;
            if (! file.isEmpty()) {
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(getResourceFilePath(DATA_DIR_PATH).getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.write(path, bytes);
	            retVal = path.toString();
            }
            return retVal;
	    }
	    
	    private void indexUploadedFile(UploadModelApplication model) throws IOException{
	    	
	    	for (MultipartFile file : model.getFiles()) {

	            if (file.isEmpty()) {
	                continue;
	            }
	            String fileName = saveUploadedFile(file);
	            if(fileName != null){
					Application application = indexer.getHandler(fileName).getApplication(new File(fileName));
					application.setFirstname(model.getFirstname());
					application.setLastname(model.getLastname());
					application.setEducation(model.getEducation());
					application.setCity(model.getCity());
					application.setTimestamp(LocalDateTime.now().getHourOfDay());
					System.out.println(LocalDateTime.now().getHourOfDay());
					application.setLocation(getLocation(model.getCity(), model.getCountry()));
					indexApplicationService.save(application);
	            }
	    	}
	    }

	public GeoPoint getLocation(String city, String country){;
		JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("925c9f60dd9b42088219fe26476d8746");
		String query = city + ", " + country;
		JOpenCageForwardRequest request = new JOpenCageForwardRequest(query);
		JOpenCageResponse response = jOpenCageGeocoder.forward(request);
		JOpenCageLatLng locationLatLng = response.getFirstPosition();

		GeoPoint location = new GeoPoint(locationLatLng.getLat(), locationLatLng.getLng());

		System.out.println("LOCATION");
		System.out.println(location);
		return location;
	}

	
}
