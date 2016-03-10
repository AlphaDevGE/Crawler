package web.crawler.db.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import web.crawler.constant.DBTable;

@Document(collection = DBTable.DOC)
public class Doc {
		
	
	/****
	 * 	To do: make the toString()
	 * 	populate the following while doing the page ranking:
	 * 	private Set<Doc> outgoingDocs;
	 *	private Set<Doc> incomingDocs;
	 *	private Doc parentDoc;
	 *
	 ******/
	
	@Id
	private String id;
	
//	@Indexed
//	private String ic;
	
	private String url;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date visitedDate;
	
	@Indexed
	private String hash;
	private String location;
	private String metadata;
	private String header;
	private String title;
//	private String outgoingUrls;
	private Set<String> outgoingDocsStr;
	private Set<Doc> outgoingDocs;
	private Set<Doc> incomingDocs;
	private String parentStr;
	private Doc parentDoc;
	
	public Doc(){}

	public Doc(String url, Date visitedDate, String hash, String location, String metadata, String header, String title,
			Set<String> outgoingDocsStr, Set<Doc> outgoingDocs, Set<Doc> incomingDocs, String parentStr,
			Doc parentDoc) {
		super();
		this.url = url;
		this.visitedDate = visitedDate;
		this.hash = hash;
		this.location = location;
		this.metadata = metadata;
		this.header = header;
		this.title = title;
		this.outgoingDocsStr = outgoingDocsStr;
		this.outgoingDocs = outgoingDocs;
		this.incomingDocs = incomingDocs;
		this.parentStr = parentStr;
		this.parentDoc = parentDoc;
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

	public Set<String> getOutgoingDocsStr() {
		return outgoingDocsStr;
	}

	public void setOutgoingDocsStr(Set<String> outgoingDocsStr) {
		this.outgoingDocsStr = outgoingDocsStr;
	}

	public Set<Doc> getOutgoingDocs() {
		return outgoingDocs;
	}

	public void setOutgoingDocs(Set<Doc> outgoingDocs) {
		this.outgoingDocs = outgoingDocs;
	}

	public Set<Doc> getIncomingDocs() {
		return incomingDocs;
	}

	public void setIncomingDocs(Set<Doc> incomingDocs) {
		this.incomingDocs = incomingDocs;
	}

	public String getParentStr() {
		return parentStr;
	}

	public void setParentStr(String parentStr) {
		this.parentStr = parentStr;
	}

	public Doc getParentDoc() {
		return parentDoc;
	}

	public void setParentDoc(Doc parentDoc) {
		this.parentDoc = parentDoc;
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
                id, url, visitedDate, hash, location, title, header, metadata
                );
		return str;
	}
	
}