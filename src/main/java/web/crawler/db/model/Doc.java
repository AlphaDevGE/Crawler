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

@Document(collection = DBTable.DOC)
public class Doc {	
	@Id
	private String id;
	
	private String url;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date visitedDate;
	
	@Indexed
	private String hash;
	private String location;
	private String metadata;
	private String header;
	private String title;
	private String path;
	private Set<String> outgoingDocsStr;
	private Set<String> incomingDocsStr;
	private Set<Address> outgoingAddresses;
//	private Set<Address> incomingAddresses;
	private String parentStr;
	private List<Double> pageRankings;
	private int rankingIterationTime;
	
	public Doc(){}

	public Doc(String url, Date visitedDate, String hash, String location, String metadata, String header, String title, String path,
			Set<String> outgoingDocsStr, String parentStr) {
		super();
		this.url = url;
		this.visitedDate = visitedDate;
		this.hash = hash;
		this.location = location;
		this.path = path;
		this.metadata = metadata;
		this.header = header;
		this.title = title;
		this.outgoingDocsStr = outgoingDocsStr;
		this.incomingDocsStr =  new HashSet<String>();
		this.outgoingAddresses =  new HashSet<Address>();
//		this.incomingAddresses =  new HashSet<Address>();
		this.parentStr = parentStr;
		this.rankingIterationTime = 0;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public Set<String> getIncomingDocsStr() {
		return incomingDocsStr;
	}

	public void setIncomingDocsStr(Set<String> incomingDocsStr) {
		this.incomingDocsStr = incomingDocsStr;
	}

	public Set<Address> getOutgoingAddresses() {
		return outgoingAddresses;
	}

	public void setOutgoingAddresses(Set<Address> outgoingAddresses) {
		this.outgoingAddresses = outgoingAddresses;
	}

//	public Set<Address> getIncomingAddresses() {
//		return incomingAddresses;
//	}
//
//	public void setIncomingAddresses(Set<Address> incomingAddresses) {
//		this.incomingAddresses = incomingAddresses;
//	}

	public String getParentStr() {
		return parentStr;
	}

	public void setParentStr(String parentStr) {
		this.parentStr = parentStr;
	}

	public List<Double> getPageRankings() {
		return pageRankings;
	}

	public void setPageRankings(List<Double> pageRankings) {
		this.pageRankings = pageRankings;
	}

	public int getRankingIterationTimae() {
		return rankingIterationTime;
	}

	public void setRankingIterationTimae(int rankingIterationTime) {
		this.rankingIterationTime = rankingIterationTime;
	}

	public String toString() {
		String str = String.format(
                "'Doc':{\n"
              + "		'id': %s,\n"
              + "		'url': '%s',\n"
              + "		'visitedDate': '%s',\n"
              + "		'hash': '%s',\n"
              + "		'location': '%s',\n"
              + "		'outgoingDocsStr': '%s',\n"
              + "		'incomingDocsStr': '%s',\n"
              + "		'pageRankings': '%s',\n"
              + "		'rankingIterationTimae': '%s',\n"
              + "		'title': '%s',\n"
              + "		'parent': '%s',\n"
              + "		'outgoingUrls': '%s',\n"
              + "		'header': '%s',\n"
              + "		'metadata': '%s',\n"
              + "		},\n",
                id, url, visitedDate, hash, location,outgoingDocsStr, 
                incomingDocsStr, pageRankings, rankingIterationTime, title, parentStr, header, metadata
                );
		return str;
	}
	
}