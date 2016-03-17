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
 * Doc model class will save the web page that is being crawled and extracted
 * After completion crawling and extraction the incomingDocStr and pageRanking must be calculated
 * 
 * @author AlphaDev, Mohammad Yazdani 
 * @email m.yazdani2010@gmail.com
 * 
 */

@Document(collection = DBTable.DOCS)
public class Docs {	
	@Id
	private String id;
	
	private List<Doc> docs;
	
	public Docs(){super();}

	public Docs( List<Doc> docs) {
		super();
		this.docs = docs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Doc> getDocs() {
		return docs;
	}

	public void setDocs(List<Doc> docs) {
		this.docs = docs;
	}
	
	

}