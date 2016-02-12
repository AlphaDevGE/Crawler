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
	
	public Url(){}
	
	public Url(String url, Date visitedDate, String hash, String location) {
		super();
		this.url = url;
		this.visitedDate = visitedDate;
		this.hash = hash;
		this.location = location;
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
	
	public String toString() {
		String str = String.format(
                "'Url':{\n"
              + "		'id': %s,\n"
              + "		'url': '%s',\n"
              + "		'visitedDate': '%s',\n"
              + "		'hash': '%s',\n"
              + "		'location': '%s',\n"
              + "		},\n",
                id, url, visitedDate, hash, location
                );
		return str;
	}	
	
}