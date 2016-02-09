package web.crawler.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Web {

	@Id
	private String id;
	
	private String url;
	private String hash;
	private String content;
	private String header;
	private String visitedTime;
	
	public Web() {}

	public Web(String url, String hash, String content, String header) {
		this.url = url;
		this.hash = hash;
		this.content = content;
		this.header = header;
	}

	public Web(String url, String hash, String content, String header, String visitedTime) {
		this.url = url;
		this.hash = hash;
		this.content = content;
		this.header = header;
		this.visitedTime = visitedTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getVisitedTime() {
		return visitedTime;
	}

	public void setVisitedTime(String visitedTime) {
		this.visitedTime = visitedTime;
	}
	
	public String toString() {
		String str = String.format("to String()"
//                "'File':{\n"
//              + "		'Id': %s,\n"
//              + "		'Name': '%s',\n"
//              + "		'Type': '%s',\n"
//              + "		'Extension': '%s',\n"
//              + "		'Size': '%s',\n"
//              + "		'Owner': '%s',\n"
//              + "		'Date': '%s',\n"
//              + "		'Path': '%s',\n"
//              + "		},\n",
//                id, name, type, extension, size, owner, date, path
                );
		
		return str;
	}	
	
}
