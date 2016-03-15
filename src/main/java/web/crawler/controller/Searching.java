package web.crawler.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import web.crawler.db.dao.DocDao;
import web.crawler.db.dao.IndexDao;
import web.crawler.db.model.Doc;
import web.crawler.db.model.Index;
import web.crawler.db.model.WordDoc;

public class Searching {
	static Version version = Version.LUCENE_36;
	static String queryString = "";
	static IndexDao indexDao=new IndexDao();
	static HashSet<String> searchIndexWithQueryParser(String query)
			throws ParseException, IOException {
		// if length of query is greater than 2
		if (query.length() > 1) {
			String[] queryTerms = query.split(" ");
			for (String q : queryTerms) {
				if (q.equalsIgnoreCase("and") || q.equalsIgnoreCase("or")
						|| q.equalsIgnoreCase("not")) {
					queryString += " " + q.toUpperCase();
				} else {
					queryString += " " + q;
				}
			}
		}
		DocDao docDao = new DocDao();
		System.out.println("Query String is" + queryString);
		File indexDirectory = new File("D:/webcrawler/Indexing");
		IndexReader reader = IndexReader.open(FSDirectory.open(indexDirectory));
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		Analyzer an = new StandardAnalyzer(version);

		// Search within title
		QueryParser titleParser = new QueryParser(version, "title", an);
		Query q = titleParser.parse(queryString);
		System.out.println("parsed query is"+q.toString());
		TopDocs titleDocs = indexSearcher.search(q, 20);
		System.out.println("Total Hits in Title :" + titleDocs.totalHits);
		ScoreDoc[] scoreDocArray = titleDocs.scoreDocs;
		HashSet<String> results = new HashSet<String>();
		// for all the documents get the score and multiply it by some
		// coefficient to give it more importance
		for (ScoreDoc scoredoc : scoreDocArray) {
			// Retrieve the matched document and increase the rank of the
			// documents by some coefficient
			Document doc = indexSearcher.doc(scoredoc.doc);
			String path = doc.get("path");
			
			results.add(doc.get("path"));
			Doc currentDoc = docDao.getDocByPath(path);
			// use doc dao to get this document
			List<Double> pageRankList = currentDoc.getPageRankings();
			int size = pageRankList.size();
			double scoreToChange = pageRankList.get(size - 1);
			scoreToChange += 0.10;
			currentDoc.setPageRankings(pageRankList);
			docDao.saveDoc(currentDoc);

			// retrieve the doc and increase the ranking and save it in the
			// database

		}

		// search within content
		QueryParser contentParser = new QueryParser(version, "html", an);
		Query qString=contentParser.parse(queryString);
		System.out.println(qString.toString());
		TopDocs contentDocs = indexSearcher.search(qString, 20);
		
		 System.out.println("Total Hits in Contents: "+contentDocs.totalHits);
		ScoreDoc[] scoreDocArray2 = contentDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocArray2) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			results.add(doc.get("path"));
		}
		return results;
	}

	public static void displayQuery(Query query) {
		System.out.println("Query: " + query.toString());
	}
	//for the search queries get all the word docs
	//compare it with the docs of results return by lucene and rmeove those which are not return by lucene
	//sort the results based on score and display results
	
	public static List<WordDoc> getWordDocsByTerm(String term){
		
			Index index=indexDao.getIndexByTerm(term);
			List<WordDoc> wdList = index.getDocuments();
			System.out.println("list containss "+wdList.size()+" items for :"+term);
			return wdList;
	} 
	
	public static List<WordDoc> orQuery(String term1,String term2){
		//for or query take the lists and merge the two list(create a set)
		
		List<WordDoc> term1List=getWordDocsByTerm(term1);
		List<WordDoc> term2List=getWordDocsByTerm(term2);
		term1List.addAll(term2List);
		return term1List;
	}
	
	
	public static List<String> andQuery(String term1,String term2){
		//for or query take the lists and merge the two list(create a set)
		System.out.println("And Operation Received :"+term1+" and "+term2);
		List<WordDoc> term1List=getWordDocsByTerm(term1);
		List<WordDoc> term2List=getWordDocsByTerm(term2);
		List<String> path1=new ArrayList<String>();
		List<String> path2=new ArrayList<String>();
		for(WordDoc wd:term1List){
			path1.add(wd.getDocHash());
		}
		for(WordDoc wd:term2List){
			path2.add(wd.getDocHash());
		}
		term1List.retainAll(term2List);
		System.out.println("After retaining the list size is"+term1List.size());
		path1.retainAll(path2);
		return path1;
	}
	
	public static List<String> doQuery(String query){
		//search for And and get the term before and after the AND and call the andQuery
		System.out.println("My Query IS:"+query);
		String[] terms=query.split(" ");
		if(terms.length>1){
			for(int i=0;i<terms.length;i++){
				if(terms[i].equalsIgnoreCase("and")){
					List<String> results=andQuery(terms[i-1],terms[i+1]);
					return results;
					
				}
				if(terms[i].equalsIgnoreCase("or")){
					List<String> results=andQuery(terms[i-1],terms[i+1]);
					return results;
				}
			}
		}else{
			if(query.length()==1){
				System.out.println("yes its 1 Term query");
				Index index=indexDao.getIndexByTerm(query);
				List<WordDoc> wdList=index.getDocuments();
				for(WordDoc wd:wdList){
					System.out.println("tfidf found is:"+wd.getTfIdf());
				}
			}
		}
		return null;
	}


	public static void main(String sr[]) throws IOException, ParseException {
		/*HashSet<String> result = searchIndexWithQueryParser("savin");
		for (String doc : result) {
			System.out.println(doc);
		}*/
		
		doQuery("sachin");
		
/*		for(String wd:results){
			System.out.println(wd);
		}
*/	}

}