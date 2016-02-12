package web.crawler.model;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class Web {

	@Id
	private String id;
	
	private String url;
	private String hash;
	private String content;
	private String header;
	private String path;
	private String docId;
	private String metadata;
	private String title;
	private String outgoingUrl;
	private String location;
	private String parentUrl;
	private String contentType;
	
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date visitedTime;
	
	public Web() {}

	public Web(String url, String hash, String content, String header) {
		this.url = url;
		this.hash = hash;
		this.content = content;
		this.header = header;
	}

	public Web(String url, String hash, String content, String header, Date visitedTime) {
		this.url = url;
		this.hash = hash;
		this.content = content;
		this.header = header;
		this.visitedTime = visitedTime;
	}
	
	
	public Web(String url, String hash, String content, String header, String path, String docId, String metadata,
			String title, String outgoingUrl, String location, String parentUrl, String contentType, Date visitedTime) {
		super();
		this.url = url;
		this.hash = hash;
		this.content = content;
		this.header = header;
		this.path = path;
		this.docId = docId;
		this.metadata = metadata;
		this.title = title;
		this.outgoingUrl = outgoingUrl;
		this.location = location;
		this.parentUrl = parentUrl;
		this.contentType = contentType;
		this.visitedTime = visitedTime;
	}

	public String toString() {
//		Calendar cal = Calendar.getInstance()
		String str = String.format(
                "'Web':{\n"
              + "		'id': %s,\n"
              + "		'url': '%s',\n"
              + "		'hash': '%s',\n"
              + "		'visitedTime': '%s',\n"
              + "		'header': '%s',\n"
              + "		'content': '%s',\n"
              + "		},\n",
                id, url, hash, visitedTime, header, content
                );
		System.out.println("date: " + visitedTime);
		
		return str;
	}	
	
}
