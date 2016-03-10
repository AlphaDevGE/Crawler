package web.crawler.db.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import web.crawler.constant.DBTable;

@Document(collection = DBTable.WORD_DOC)
public class WordDoc {
			
//	@Indexed
//	private String ic;
	
/*	doc_hash: 
	page_ranking: 
	TF:
	IDF:
	TF-IDF:
	apearanceTime: 
	positions: [3, 20, 100, ...]
	ranks: [ contecnt: No, 
			 title: No, 
			 meta: No],
	

	meta_data: [ no_of_appearance: No,
				 .... ],

	//maybe we can have no of appearance based on content, meta, header, title
	no_of_appearance: [ content: no, header: no, title: no, meta: no],
	appearance_positions[ content: [1, 10, 20, ...],  
						  header: [11, 110, 120, ...], 
						  title: [21, 210, 220, ...], 
						  meta: [31, 310, 320, ...] 
						],

	score: //calculating ranks, TF, IDF, TF-IDF and may be more
*/	
	@Id
	private String id;
	
	@Indexed
	private String docHash;
	private double pageRanking;
	private double tf;
	private double idf;
	private double tfIdf;
	private int apearanceTime;
	private List<Integer> postitions;
	private double score;
	private Doc doc;
	
	public WordDoc(){ super(); }
	
	public WordDoc(String docHash, double pageRanking, double tf, double idf, double tfIdf, int apearanceTime,
			List<Integer> postitions, double score) {
		super();
		this.docHash = docHash;
		this.pageRanking = pageRanking;
		this.tf = tf;
		this.idf = idf;
		this.tfIdf = tfIdf;
		this.apearanceTime = apearanceTime;
		this.postitions = postitions;
		this.score = score;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDocHash() {
		return docHash;
	}
	public void setDocHash(String docHash) {
		this.docHash = docHash;
	}
	public double getPageRanking() {
		return pageRanking;
	}
	public void setPageRanking(double pageRanking) {
		this.pageRanking = pageRanking;
	}
	public double getTf() {
		return tf;
	}
	public void setTf(double tf) {
		this.tf = tf;
	}
	public double getIdf() {
		return idf;
	}
	public void setIdf(double idf) {
		this.idf = idf;
	}
	public double getTfIdf() {
		return tfIdf;
	}
	public void setTfIdf(double tfIdf) {
		this.tfIdf = tfIdf;
	}
	public int getApearanceTime() {
		return apearanceTime;
	}
	public void setApearanceTime(int apearanceTime) {
		this.apearanceTime = apearanceTime;
	}
	public List<Integer> getPostitions() {
		return postitions;
	}
	public void setPostitions(List<Integer> postitions) {
		this.postitions = postitions;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

	public Doc getDoc() {
		return doc;
	}

	public void setDoc(Doc doc) {
		this.doc = doc;
	}
	
	
}