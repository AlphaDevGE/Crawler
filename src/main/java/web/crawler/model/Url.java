package web.crawler.model;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class Url {

	@Id
	private String id;
	
	private String url;
	private String hash;

	
	public Url() {}

	public Url(String url, String hash) {
		this.url = url;
		this.hash = hash;
	}
	
	public String toString() {
//		Calendar cal = Calendar.getInstance()
		String str = String.format(
                "'Web':{\n"
              + "		'id': %s,\n"
              + "		'url': '%s',\n"
              + "		'hash': '%s',\n"
              + "		},\n",
                id, url, hash
                );
		
		return str;
	}	
	
}