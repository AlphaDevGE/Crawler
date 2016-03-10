package web.crawler.db.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import web.crawler.constant.DBTable;

@Document(collection = DBTable.INDEX)
public class Index {	
	@Id
	private String id;
	
	@Indexed
	private String term;
	
	private List<WordDoc> documents;
	
	public Index(){ super(); }

	public Index(String term, List<WordDoc> documents) {
		super();
		this.term = term;
		this.documents = documents;
	}
	
	public void addWordDoc(WordDoc wd){
		if(documents == null)
			documents = new ArrayList<WordDoc>();
		documents.add(wd);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<WordDoc> getDocuments() {
		return documents;
	}

	public void setDocuments(List<WordDoc> documents) {
		this.documents = documents;
	}
	
}