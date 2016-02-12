package web.crawler.db.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "users")
public class Url {
		
	@Id
	private String id;
	
//	@Indexed
//	private String ic;
	
	private String url;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date visitedDate;
	private String hash;
	private String location;
	private String metadata;
	private String header;
	private String title;
	private String outgoingUrls;
	private String parent;
	
	public Url(){}
	
	public Url(String url, Date visitedDate, String hash, String location) {
		this.url = url;
		this.visitedDate = visitedDate;
		this.hash = hash;
		this.location = location;
		this.metadata = "null";
		this.header = "null";
		this.title = "null";
		this.outgoingUrls = "null";
		this.parent = "null";
	}
	public Url(String url, Date visitedDate, String hash, String location, String metadata, String header, String title,
			String outgoingUrls, String parent) {
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

	public String getLoation() {
		return location;
	}

	public void setLoation(String location) {
		this.location = location;
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

	public String getOutgoingUrls() {
		return outgoingUrls;
	}

	public void setOutgoingUrls(String outgoingUrls) {
		this.outgoingUrls = outgoingUrls;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
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