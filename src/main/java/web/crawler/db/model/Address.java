package web.crawler.db.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import web.crawler.constant.DBTable;

/**
 * 
 * Address represents the detail of a Doc object 
 * 
 * @author AlphaDev, Mohammad Yazdani 
 * @email m.yazdani2010@gmail.com
 * 
 */
@Document(collection = DBTable.ADDRESS)
public class Address {	
	@Id
	private String id;
	
	private String url;
	private String path;
	
	public Address(){super();}
	
	public Address(String url, String path) {
		super();
		this.url = url;
		this.path = path;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String toString() {
		String str = String.format(
                "'address':{\n"
              + "		'id': %s,\n"
              + "		'url': '%s',\n"
              + "		'path': '%s',\n"
              + "		},\n",
                id, url,path
                );
		return str;
	}
}