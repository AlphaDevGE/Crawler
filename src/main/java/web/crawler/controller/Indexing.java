package web.crawler.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.UrlDao;
import web.crawler.db.dao.WordDocDao;
import web.crawler.db.model.Index;
import web.crawler.db.model.WordDoc;

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
	
	 static void walk(String path,IndexWriter writer) throws IOException {
		 Version version = Version.LUCENE_36;
		 Analyzer an = new StandardAnalyzer(version);
	        File root = new File( path );
	        File[] list = root.listFiles();
	       
	        if (list == null) return;

	        for (File f:list ) {
	            if (f.isDirectory() ) {
	            	String absPath = f.getAbsolutePath();
	                walk(f.getAbsolutePath(),writer);
	            }
	            else {

	    			String filename=f.getName();
	    			System.out.println(filename);
	    			Document doc = new Document();
	    			Reader r = new FileReader(f);
	    			HTMLStripCharFilter filter = new HTMLStripCharFilter(
	    					CharReader.get(new FileReader(f)));
	    			TokenStream ts = an.tokenStream("html", filter);
	    			doc.add(new Field("html", ts, Field.TermVector.WITH_POSITIONS));
	    			doc.add(new Field("path", f.getAbsolutePath(), Field.Store.YES, Field.Index.ANALYZED)); 


	    			// Just checking what is in thee document
	    			writer.addDocument(doc);
	    		
	    		
	            	
	            }
	        }
	 }
	private WordDocDao worddocdao=new WordDocDao();
	public static void main(String sr[]) throws IOException {
		IndexDao indexDao=new IndexDao();
		Version version = Version.LUCENE_36;
		 Analyzer an = new StandardAnalyzer(version);
		
		 boolean create = true;

			Directory index = FSDirectory.open(new File("D:/webcrawler/Indexing"));
			IndexWriterConfig config = new IndexWriterConfig(version, an);
			if (create) {

				config.setOpenMode(OpenMode.CREATE);
			} else {

				config.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}
			IndexWriter writer = new IndexWriter(index, config);
			

		
		walk("D:/en",writer);
		writer.close();

		// Apply Stripper and StopWord Removal and Stemmer

		/*
		 * HTMLStripCharFilter filter = new HTMLStripCharFilter(
		 * CharReader.get(new FileReader(fList[0])));
		 */
		
		/*
		 * an.tokenStream(fieldName, reader) ts=new PorterStemFilter(filter);
		 */

		// Indexing code
		


		/*for (File f : fList) {
			String filename=f.getName();
			Document doc = new Document();
			Reader r = new FileReader(f);
			HTMLStripCharFilter filter = new HTMLStripCharFilter(
					CharReader.get(new FileReader(f)));
			TokenStream ts = an.tokenStream("html", filter);
			doc.add(new Field("html", ts, Field.TermVector.WITH_POSITIONS));

			// Just checking what is in thee document
			writer.addDocument(doc);

		}*/
		
		File indexDirectory = new File("D:/webcrawler/Indexing");

		// Reader
		IndexReader reader = IndexReader.open(FSDirectory.open(indexDirectory));
		System.out.println("No of document" + reader.numDocs());
		System.out.println("Enumeration of all terms in the index by reader.terms()");
		TermEnum allTerms = reader.terms();
		DefaultSimilarity simi = new DefaultSimilarity();

		while (allTerms.next()) {
			List<WordDoc> wordDoc=new ArrayList<WordDoc>();
			String term = (allTerms.term().text());
			int documentFrequency = reader.docFreq(allTerms.term());
			TermPositions data = reader.termPositions(allTerms.term());
			HashMap<String, Double> tfidf = new HashMap<String, Double>();
			String output = term + " ==> ";
			output += ("This term is appearing in " + documentFrequency + " documents");
			String apnd = "< ";
			while (data.next()) {
				WordDoc wd=new WordDoc();
				int documentNo = (data.doc());
				Document doc=reader.document(documentNo);
				String documentLocation=doc.get("path");
				wd.setDocHash(documentLocation);
				int termsInDoc = getNoOfTermsInDoc(reader, documentNo);
				int appearanceTime=data.freq();
				wd.setApearanceTime(appearanceTime);
				double tf = 1 + Math.log10(data.freq());
				wd.setTf(tf);
				double idf = simi.idf(documentFrequency, reader.numDocs());
				wd.setIdf(idf);
				double tfidfscore = tf * idf;
				wd.setTfIdf(tfidfscore);
				tfidf.put("tfidf", tfidfscore);
				
				List<Integer> positionsList=new ArrayList<Integer>();
				for (int i = 0; i < data.freq(); i++) {
					positionsList.add(data.nextPosition());
				}
				wd.setPostitions(positionsList);
				/* System.out.println(tfidf); */
				apnd += documentNo + " " + tf ;
				wordDoc.add(wd);
				
				
			}
			Index indx=new Index(term,wordDoc);
			indexDao.saveIndex(indx);
			System.out.println(output);
			System.out.println(apnd);

		}

	}

}
