package web.crawler.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexing {
	// Read the files from the storage
	// remove all tags
	// remove stop words

	static int getNoOfTermsInDoc(IndexReader reader, int docno)
			throws IOException {
		TermFreqVector[] tfv = reader.getTermFreqVectors(docno);
		String[] arr = tfv[0].getTerms();

		return arr.length;
	}

	public static void main(String sr[]) throws IOException {
		File directory = new File("D:/webcrawler/separateFiles");
		File[] fList = directory.listFiles();
		Version version = Version.LUCENE_36;

		// Apply Stripper and StopWord Removal and Stemmer

		/*
		 * HTMLStripCharFilter filter = new HTMLStripCharFilter(
		 * CharReader.get(new FileReader(fList[0])));
		 */
		Analyzer an = new StandardAnalyzer(version);
		/*
		 * an.tokenStream(fieldName, reader) ts=new PorterStemFilter(filter);
		 */

		// Indexing code
		boolean create = true;

		Directory index = FSDirectory.open(new File("D:/webcrawler/Indexing"));
		IndexWriterConfig config = new IndexWriterConfig(version, an);
		if (create) {

			config.setOpenMode(OpenMode.CREATE);
		} else {

			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		IndexWriter writer = new IndexWriter(index, config);

		for (File f : fList) {
			Document doc = new Document();
			Reader r = new FileReader(f);
			HTMLStripCharFilter filter = new HTMLStripCharFilter(
					CharReader.get(new FileReader(f)));
			TokenStream ts = an.tokenStream("html", filter);
			doc.add(new Field("html", ts, Field.TermVector.WITH_POSITIONS));

			// Just checking what is in thee document
			writer.addDocument(doc);

		}
		writer.close();
		File indexDirectory = new File("D:/webcrawler/Indexing");

		// Reader
		IndexReader reader = IndexReader.open(FSDirectory.open(indexDirectory));
		System.out.println("No of document" + reader.numDocs());
		System.out
				.println("Enumeration of all terms in the index by reader.terms()");
		TermEnum allTerms = reader.terms();
		DefaultSimilarity simi = new DefaultSimilarity();

		while (allTerms.next()) {
			String term = (allTerms.term().text());
			int documentFrequency = reader.docFreq(allTerms.term());
			TermPositions data = reader.termPositions(allTerms.term());
			HashMap<String, Double> tfidf = new HashMap<String, Double>();
			String output = term + " ==> ";
			output += ("This term is appearing in " + documentFrequency + " documents");
			String apnd = "< ";
			while (data.next()) {
				int documentNo = (data.doc());
				int termsInDoc = getNoOfTermsInDoc(reader, documentNo);
				double tf = 1 + Math.log10(data.freq());
				double idf = simi.idf(documentFrequency, reader.numDocs());
				double tfidfscore = tf * idf;
				tfidf.put("tfidf", tfidfscore);

				String positions = "<Positions:";
				for (int i = 0; i < data.freq(); i++) {
					positions += " " + (data.nextPosition());
				}
				positions += ">";
				/* System.out.println(tfidf); */
				apnd += documentNo + " " + tf + " " + positions;

			}
			System.out.println(output);
			System.out.println(apnd);

		}

	}

}
