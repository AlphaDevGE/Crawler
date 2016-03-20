package web.crawler.db.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import web.crawler.constant.DBTable;

/**
 * 
 * DocWord model class will save the document details corresponding to the
 * String term that is being indexed in the Index object
 * 
 * @author AlphaDev, Mohammad Yazdani
 * @email m.yazdani2010@gmail.com
 * 
 */
@Document(collection = DBTable.WORD_DOC)
public class WordDoc {

	@Id
	private String id;

	@Indexed
	private String docHash;

	// this is document number that is been provided by Apache Lucene library.
	private int docNumber;

	private String term;
	private double tf;
	private double idf;
	private double tfIdf;
	private double score;
	// don't need now
	// private Doc doc;//the source Doc that is
	private boolean inTitle;
	private String docLocation;

	public WordDoc() {
		super();
	}

	public WordDoc(String docHash, String term, double tf, double idf,
			double tfIdf, double score, Doc doc, boolean inTitle) {
		super();
		this.docHash = docHash;
		this.term = term.toLowerCase();
		this.tf = tf;
		this.idf = idf;
		this.tfIdf = tfIdf;

		this.score = score;

		this.inTitle = inTitle;
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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isInTitle() {
		return inTitle;
	}

	public void setInTitle(boolean inTitle) {
		this.inTitle = inTitle;
	}

	public String getDocLocation() {
		return docLocation;
	}

	public void setDocLocation(String docLocation) {
		this.docLocation = docLocation;
	}

	public int getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(int docNumber) {
		this.docNumber = docNumber;
	}

	public String toString() {
		String str = String.format("'WordDoc':{\n" + "		'id': %s,\n"
				+ "		'docHash': '%s',\n" + "		'term': '%s',\n"
				+ "		'tf': '%s',\n" + "		'idf': '%s',\n" + "		'tfIdf': '%s',\n"
				+ "		'postitions': '%s',\n" + "		'score': '%s',\n"
				+ "		'doc': '%s',\n" + "		},\n", id, docHash, term, tf, idf,
				tfIdf, score);
		return str;
	}
}