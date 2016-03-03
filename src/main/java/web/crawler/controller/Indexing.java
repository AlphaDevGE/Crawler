package web.crawler.controller;



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IndexableBinaryStringTools;
import org.apache.lucene.util.Version;


public class Indexing {
// Read the files from the storage
//remove all tags
// remove stop words
	
	public static void main(String sr[]) throws IOException  {
		File directory = new File("D:/webcrawler/separateFiles");
		File[] fList = directory.listFiles();
		Version version = Version.LUCENE_36 ;
	
	//Apply Stripper and StopWord Removal and Stemmer
	
		
		HTMLStripCharFilter filter=new HTMLStripCharFilter(CharReader.get(new FileReader(fList[0])));
		Analyzer an=new StandardAnalyzer(version);
		TokenStream ts=an.tokenStream(null,filter );
	//	ts=new PorterStemFilter(ts);
		
	
	
	//
	// Indexing code 
	boolean create=true;
		
	Directory index = FSDirectory.open(new File("D:/webcrawler/Indexing"));
	IndexWriterConfig config = new IndexWriterConfig(version, an);
	if (create) {
	       // Create a new index in the directory, removing any
	        // previously indexed documents:
	        config.setOpenMode(OpenMode.CREATE);
	     } else {
	        // Add new documents to an existing index:
	        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
	      }
	IndexWriter writer = new IndexWriter(index, config);
	
	for(File f:fList){
	Document doc=new Document();
	Reader r=new FileReader(f);
	doc.add(new Field("content",r,Field.TermVector.WITH_POSITIONS ));
	writer.addDocument(doc);
	}
	writer.close();
	File indexDirectory = new File("D:/webcrawler/Indexing");
	IndexReader reader = IndexReader.open(FSDirectory.open(indexDirectory));
	System.out.println("No of document"+reader.numDocs());
	System.out.println("Enumeration of all terms in the index by reader.terms()");
	TermEnum allTerms=reader.terms();
	
	while(allTerms.next()){
		String term=(allTerms.term().text());
		int documentFrequency=reader.docFreq(allTerms.term());
		TermPositions data=reader.termPositions(allTerms.term());
		String output=term +" ==> ";
		String apnd="< ";
		while(data.next()){
			int documentNo=(data.doc());
			int termFrequency=(data.freq());
			
			String positions = "<";
			for(int i=0;i<data.freq();i++){
			positions+=" "+(data.nextPosition());
			}
			positions+=">";
			
			apnd+=documentNo+" "+termFrequency+" "+positions;
		}
		System.out.println(output);
		System.out.println(apnd);
		
	
	
	}
	
	/*TermFreqVector vtr=reader.getTermFreqVector(0, "content");

	TermPositionVector tpvector = (TermPositionVector)vtr;
	
	String[] terms=tpvector.getTerms();
	int tf[]=tpvector.getTermFrequencies();
	for(int i=0;i<terms.length;i++){
	
	
	int[] pos=tpvector.getTermPositions(i);
	String str = " ";
	for(int posi: pos){
		str+=posi+" ";
	}
	System.out.println(terms[i]+"   Frequency: "+tf[i]+"Pos: "+str);
	}*/
	}
	
	    }
	
	
	
	
	
	
	

		
		
	
	
	
	
	

	

	






