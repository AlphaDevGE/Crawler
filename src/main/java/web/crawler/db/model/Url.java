package web.crawler.db.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import web.crawler.constant.DBTable;
/**
 * 
 * @author AlphaDev
 * 
 * Url model class has been replaced by Doc.
 *
 */
@Document(collection = DBTable.URL)
public class Url {
		
	@Id
	private String id;
	private String url;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date visitedDate;
	private String hash;
	private String location;
	private String metadata;
	private String header;
	private String title;
	private Set<String> outgoingUrls;
	private String parent;
	private String content;
	
	public Url(){}
	
	public Url(String url, Date visitedDate, String hash, String location) {
		this.url = url;
		this.visitedDate = visitedDate;
		this.hash = hash;
		this.location = location;
		this.metadata = "null";
		this.header = "null";
		this.title = "null";
		this.outgoingUrls = null;
		this.parent = "null";
	}
	public Url(String url, Date visitedDate, String hash, String location, String metadata, String header, String title,
			Set<String> outgoingUrls, String parent, String content) {
		super();
		this.url = url;
		this.visitedDate = visitedDate;
		this.hash = hash;
		this.location = location;
		this.metadata = metadata;
		this.header = header;
		this.title = title;
		this.outgoingUrls = outgoingUrls;
		this.parent = parent;
		this.content = content;
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

	public Date getVisitedDate() {
		return visitedDate;
	}

	public void setVisitedDate(Date visitedDate) {
		this.visitedDate = visitedDate;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getOutgoingUrls() {
		return outgoingUrls;
	}

	public void setOutgoingUrls(Set<String> outgoingUrls) {
		this.outgoingUrls = outgoingUrls;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {
		String str = String.format(
                "'Url':{\n"
              + "		'id': %s,\n"
              + "		'url': '%s',\n"
              + "		'visitedDate': '%s',\n"
              + "		'hash': '%s',\n"
              + "		'location': '%s',\n"
              + "		'title': '%s',\n"
              + "		'parent': '%s',\n"
              + "		'outgoingUrls': '%s',\n"
              + "		'header': '%s',\n"
              + "		'metadata': '%s',\n"
              + "		},\n",
                id, url, visitedDate, hash, location, title, parent, outgoingUrls, header, metadata
                );
		return str;
	}
}