package web.crawler.controller;

import java.awt.TextField;
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
import org.apache.lucene.document.Field.Store;
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

import web.crawler.constant.Value;
import web.crawler.db.controller.UrlAppDao;
import web.crawler.db.dao.DocDao;
import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.UrlDao;
import web.crawler.db.dao.WordDocDao;
import web.crawler.db.model.Doc;
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
		DocDao docDao=new DocDao();
		 Version version = Version.LUCENE_36;
		 Analyzer an = new StandardAnalyzer(version);
	        File root = new File( path );
	        File[] list = root.listFiles();
	       
	        if (list == null) return;

	        for (File f:list ) {
	            if (f.isDirectory() ) {
	            	String absPath = f.getAbsolutePath();
	                walk(absPath,writer);
	            }
	            else {

	    			String filename=f.getName();
	    			/*System.out.println(filename);*/
	    			Document doc = new Document();
	    			Reader r = new FileReader(f);
	    			HTMLStripCharFilter filter = new HTMLStripCharFilter(
	    					CharReader.get(new FileReader(f)));
	    			TokenStream ts = an.tokenStream("html", filter);
	    			doc.add(new Field("html", ts, Field.TermVector.WITH_POSITIONS));
	    			//read from the doc dao to get the title of the page
	    			Doc currentDoc=docDao.getDocByUrl(filename);
	    			String title=currentDoc.getTitle();
	    			doc.add(new Field("path", f.getAbsolutePath(), Field.Store.YES, Field.Index.NO)); 
	    			doc.add(new Field("title",title,Store.YES,Field.Index.NO));

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
		WordDocDao wdDao = new WordDocDao();
		
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

		
		File indexDirectory = new File("D:/webcrawler/Indexing");

		// Reader
		IndexReader reader = IndexReader.open(FSDirectory.open(indexDirectory));
		/*System.out.println("No of document" + reader.numDocs());
		System.out.println("Enumeration of all terms in the index by reader.terms()");*/
		TermEnum allTerms = reader.terms();
		DefaultSimilarity simi = new DefaultSimilarity();
		
		DocDao docDao = new DocDao();
		int count=1;
			while (allTerms.next()) {
			
			List<WordDoc> wordDoc=new ArrayList<WordDoc>();
			String term = (allTerms.term().text());
			
		
			int documentFrequency = reader.docFreq(allTerms.term());
			TermPositions data = reader.termPositions(allTerms.term());
			
//			HashMap<String, Double> tfidf = new HashMap<String, Double>();
			String output = term + " ==> ";
			output += ("This term is appearing in " + documentFrequency + " documents");
			
			outer: while (data.next()) {
				//get the word doc for this term and check if wd already exxists
				
				WordDoc wd=new WordDoc();
				int documentNo = (data.doc());
				Document doc=reader.document(documentNo);
				String documentLocation=doc.get("path");
				String title=doc.get("title");
				//get the relevant Doc object from DB
				Doc thisDoc = docDao.getDocByPath(documentLocation);
				if(thisDoc == null)
					System.out.println("Doc Not found in DB !!!!!!!!! ");
				//wd.setDoc(thisDoc);
				wd.setDocLocation(documentLocation);
				List<Double> pageRankingsList=thisDoc.getPageRankings();
				double latestScore=pageRankingsList.get(pageRankingsList.size()-1);
				if(title.contains(term)){
					wd.setInTitle(true);
				}
				Index indx=indexDao.getIndexByTerm(term);
				List<WordDoc> documents = null;
				if(indx!=null)
				documents=indx.getDocuments();
				if(documents!=null)
				for(WordDoc wdd:documents){
					if(wdd.getDocHash().equals(documentLocation)){
						continue outer;
					}
				}
				wd.setDocHash(documentLocation);
				int termsInDoc = getNoOfTermsInDoc(reader, documentNo);
				int appearanceTime=data.freq();
				
				//System.out.println("***********************Total no of Terms in document "+documentLocation+" is "+termsInDoc);
				
				
				double tf = (data.freq());
				double normalizedTf=tf/termsInDoc;
				wd.setTf(normalizedTf);
				double idf = simi.idf(documentFrequency, reader.numDocs());
				wd.setIdf(idf);
				double tfidfscore = normalizedTf* idf;
				wd.setTfIdf(tfidfscore);
				double score= (tfidfscore * Value.TF_IDF_WEIGHT) + (latestScore * Value.LINK_ANALYSIS_WEIGHT);
				wd.setScore(score*Value.SCORE_SCALER);
				System.out.println("Scores:"+score);
//				tfidf.put("tfidf", tfidfscore);
				
				List<Integer> positionsList=new ArrayList<Integer>();
				for (int i = 0; i < data.freq(); i++) {
					positionsList.add(data.nextPosition());
				}
				wd.setPostitions(positionsList);
				wd.setTerm(term);
				wdDao.saveWordDoc(wd);
				
				wordDoc.add(wd);
//				wordDoc.add(wdDao.getWordDocByDocHash(wd.getDocHash()));
			}
			Index indx=new Index(term, wordDoc);
			indexDao.saveIndex(indx);
			
			/*System.out.println(output);
			System.out.println(apnd);
*/			count++;
		}

	}

}

