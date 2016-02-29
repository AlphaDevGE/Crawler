package web.crawler.controller;



import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Version;

public class Indexing {
// Read the files from the storage
//remove all tags
// remove stop words
	
		public static void main(String sr[]) throws IOException  {
	File directory = new File("D:/webcrawler/separateFiles");
	File[] fList = directory.listFiles();
	Version version = Version.LUCENE_36 ;
	HTMLStripCharFilter filter=new HTMLStripCharFilter(CharReader.get(new FileReader(fList[0])));
	Analyzer an=new StandardAnalyzer(version);
	TokenStream ts=an.tokenStream(null, filter);
	ts=new PorterStemFilter(ts);
	CharTermAttribute termAttribute = ts.getAttribute(CharTermAttribute.class);
	
	
	
	
	while(ts.incrementToken()){
		
	System.out.println(termAttribute.toString());	
	}

	
	    
	

		}	
	
	
}	
	
	
	
	

		
		
	
	
	
	
	

	

	






