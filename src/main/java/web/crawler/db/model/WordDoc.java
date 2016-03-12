package web.crawler.db.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import web.crawler.constant.DBTable;
/**
 * 
 * DocWord model class will save the document details corresponding to the String term
 * that is being indexed in the Index object
 * 
 * @author AlphaDev, Mohammad Yazdani 
 * @email m.yazdani2010@gmail.com
 * 
 */
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
	
	private String term;
	private double tf;
	private double idf;
	private double tfIdf;
	private List<Integer> postitions;
	private double score;
	private Doc doc;//the source Doc that is 
	
	public WordDoc(){ super(); }
	
	public WordDoc(String docHash, String term, double tf, double idf, double tfIdf, List<Integer> postitions,
			double score, Doc doc) {
		super();
		this.docHash = docHash;
		this.term = term.toLowerCase();
		this.tf = tf;
		this.idf = idf;
		this.tfIdf = tfIdf;
		this.postitions = postitions;
		this.score = score;
		this.doc = doc;
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
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
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
	
	public String toString() {
		String str = String.format(
                "'WordDoc':{\n"
              + "		'id': %s,\n"
              + "		'docHash': '%s',\n"
              + "		'term': '%s',\n"
              + "		'tf': '%s',\n"
              + "		'idf': '%s',\n"
              + "		'tfIdf': '%s',\n"
              + "		'postitions': '%s',\n"
              + "		'score': '%s',\n"
              + "		'doc': '%s',\n"
              + "		},\n",
                id, docHash, term, tf, idf, tfIdf, postitions, score, doc
                );
		return str;
	}
	
}