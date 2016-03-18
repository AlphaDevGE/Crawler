package web.crawler.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

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

import web.crawler.constant.Value;
import web.crawler.db.dao.DocDao;
import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.WordDocDao;
import web.crawler.db.model.Doc;
import web.crawler.db.model.Index;
import web.crawler.db.model.ResultBean;
import web.crawler.db.model.WordDoc;

public class Searching {
	static Version version = Version.LUCENE_36;
	
	static IndexDao indexDao = new IndexDao();

	public static List<ResultBean> searchIndexWithQueryParser(String query) throws ParseException, IOException {
		// if length of query is greater than 2
		String queryString = "";
		List<ResultBean> finalresults = new ArrayList<ResultBean>();
		{
			String[] queryTerms = query.split(" ");
			for (String q : queryTerms) {
				if (q.equalsIgnoreCase("and") || q.equalsIgnoreCase("or") || q.equalsIgnoreCase("not")) {
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
		System.out.println("parsed query is" + q.toString());
		TopDocs titleDocs = indexSearcher.search(q, 20);
		System.out.println("Total Hits in Title :" + titleDocs.totalHits);
		ScoreDoc[] scoreDocArray = titleDocs.scoreDocs;
		HashSet<String> results = new HashSet<String>();
		// for all the documents get the score and multiply it by some
		// coefficient to give it more importance

		for (ScoreDoc scoredoc : scoreDocArray) {
			// Retrieve the matched document and increase the rank of the
			// documents by some coefficient
			float score=scoredoc.score;
			System.out.println("score is: "+score);
			int document=scoredoc.doc;
			ResultBean rs = new ResultBean();
			Document doc = indexSearcher.doc(scoredoc.doc);
			String path = doc.get("path");
			rs.setDescription(doc.get("title"));
			rs.setLocation(path);
			List<Double> pageR=docDao.getDocByPath(path).getPageRankings();
			
			rs.setPageRanking(pageR.get(pageR.size()-1));
			rs.setTdIdf(score);
			double finalscore=(score* Value.TF_IDF_WEIGHT) + (pageR.get(pageR.size() - 1)* Value.LINK_ANALYSIS_WEIGHT);
			List<Double> pr = docDao.getDocByPath(path).getPageRankings();
			rs.setPageRanking(pr.get(pr.size() - 1));
			rs.setScore(finalscore);
			System.out.println("Path is :" + path);
			results.add(doc.get("path"));
			Doc currentDoc = docDao.getDocByPath(path);
			// use doc dao to get this document
			List<Double> pageRankList = currentDoc.getPageRankings();
			if (pageRankList == null)
				continue;
			int size = pageRankList.size();
			double scoreToChange = pageRankList.get(size - 1);
			System.out.println("Previous Score: " + scoreToChange);
			
			scoreToChange += 0.10;
			System.out.println("score after change: " + scoreToChange);
			currentDoc.setPageRankings(pageRankList);
			
			docDao.saveDoc(currentDoc);

			// retrieve the doc and increase the ranking and save it in the
			// database
			finalresults.add(rs);
		}

		// search within content
		QueryParser contentParser = new QueryParser(version, "html", an);
		Query qString = contentParser.parse(queryString);
		System.out.println(qString.toString());
		TopDocs contentDocs = indexSearcher.search(qString, 20);

		System.out.println("Total Hits in Contents: " + contentDocs.totalHits);
		ScoreDoc[] scoreDocArray2 = contentDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocArray2) {
			double score=scoreDoc.score;
			ResultBean rs = new ResultBean();
			Document doc = indexSearcher.doc(scoreDoc.doc);
			results.add(doc.get("path"));
			rs.setDescription(doc.get("title"));
			rs.setLocation(doc.get("path"));
			List<Double> pr = docDao.getDocByPath(doc.get("path")).getPageRankings();
			rs.setTdIdf(score);
			rs.setPageRanking(pr.get(pr.size() - 1));
			double finalscore=(score* Value.TF_IDF_WEIGHT) + (pr.get(pr.size() - 1)* Value.LINK_ANALYSIS_WEIGHT);
			rs.setScore(finalscore);
			finalresults.add(rs);
			
		}
		return finalresults;
	}

	public static void displayQuery(Query query) {
		System.out.println("Query: " + query.toString());
	}
	 //for the search queries get all the word docs
	// compare it with the docs of results return by lucene and rmeove those
	// which are not return by lucene
	// sort the results based on score and display results

	
	/* public static List<WordDoc> getWordDocsByTerm(String term){
	  
	  Index index=indexDao.getIndexByTerm(term); List<WordDoc> wdList =
	  index.getDocuments(); System.out.println("list containss "+wdList.size()+
	 " items for :"+term); return wdList; }
	  
	  public static List<WordDoc> orQuery(String term1,String term2){ //for or
	 // query take the lists and merge the two list(create a set)
	  
	  List<WordDoc> term1List=getWordDocsByTerm(term1); List<WordDoc>
	  term2List=getWordDocsByTerm(term2); term1List.addAll(term2List); return
	  term1List; }
	  
	  
	  public static List<String> andQuery(String term1,String term2){ //for or
	  //query take the lists and merge the two list(create a set)
	  System.out.println("And Operation Received :"+term1+" and "+term2);
	  List<WordDoc> term1List=getWordDocsByTerm(term1); List<WordDoc>
	  term2List=getWordDocsByTerm(term2); List<String> path1=new
	  ArrayList<String>(); List<String> path2=new ArrayList<String>();
	  for(WordDoc wd:term1List){ path1.add(wd.getDocHash()); } for(WordDoc
	  wd:term2List){ path2.add(wd.getDocHash()); }
	  term1List.retainAll(term2List); System.out.println(
	  "After retaining the list size is"+term1List.size());
	  path1.retainAll(path2); return path1; }
	  
	  public static List<String> doQuery(String query){ //search for And and
	  //get the term before and after the AND and call the andQuery
	  System.out.println("My Query IS:"+query); String[] terms=query.split(" "
	  ); if(terms.length>1){ for(int i=0;i<terms.length;i++){
	  if(terms[i].equalsIgnoreCase("and")){ List<String>
	  results=andQuery(terms[i-1],terms[i+1]); return results;
	  
	  } if(terms[i].equalsIgnoreCase("or")){ List<String>
	  results=andQuery(terms[i-1],terms[i+1]); return results; } } }else{
	  if(query.length()==1){ System.out.println("yes its 1 Term query"); Index
	  index=indexDao.getIndexByTerm(query); List<WordDoc>
	  wdList=index.getDocuments(); for(WordDoc wd:wdList){ System.out.println(
	  "tfidf found is:"+wd.getTfIdf()); } } } return null; }
	 */

	public static List<ResultBean> singleTermSearch(String term) {
		WordDocDao wddao = new WordDocDao();
		DocDao dd = new DocDao();
		Index index = indexDao.getIndexByTerm(term);
		List<WordDoc> wordDocList = index.getDocuments();
		List<ResultBean> results = new ArrayList<ResultBean>();
		TreeMap<Double, String> treemap = new TreeMap<Double, String>();
		
		for (WordDoc wd : wordDocList) {
			//require tfidf and latest score from dao
			//get path first
			
			Doc dbDocument=dd.getDocByPath(wd.getDocHash());
			double tfidf=wd.getTfIdf();
			
			List<Double> allRanks=dbDocument.getPageRankings();
			double pageRank=allRanks.get(allRanks.size()-1);
			double score=(tfidf* Value.TF_IDF_WEIGHT) + (pageRank* Value.LINK_ANALYSIS_WEIGHT);
			wd.setScore(score);
			
			treemap.put(score, wd.getDocHash());
		}
		TreeMap<Double, String> newMap = new TreeMap(treemap.descendingMap());
		List<String> resultsOfPath = new ArrayList<String>();
		

		for (Map.Entry<Double, String> entry : newMap.entrySet()) {
			ResultBean rs = new ResultBean();
			double key = entry.getKey();
			String value = entry.getValue();
			
			for (WordDoc wd : wordDocList) {
				if (wd.getDocHash().equals(value)) {
					rs.setTdIdf(wd.getTfIdf());

				}
			}
			Doc doc = dd.getDocByPath(value);
			List<Double> pr = doc.getPageRankings();
			rs.setDescription(doc.getTitle());
			rs.setScore(key);
			rs.setLocation(value);
			rs.setPageRanking(pr.get(pr.size() - 1));
			
			resultsOfPath.add(value);
			System.out.println(key + " => " + value);
			results.add(rs);

		}
		

		
		

		return results;
	}

	/*public static List<ResultBean> singleTermSearch2(String term) {
		WordDocDao wddao = new WordDocDao();
		DocDao docDao = new DocDao();
		Index index = indexDao.getIndexByTerm(term);
		List<WordDoc> wordDocList = index.getDocuments();
		
		System.out.println("wordDocList size: " + wordDocList.size());
		
		List<ResultBean> results = new ArrayList<ResultBean>();
		
		for(WordDoc w : wordDocList)
		{
			Doc dbDoc = docDao.getDocByPath(w.getDocLocation());
			if(dbDoc == null)
				System.out.println("Doc path not found in Doc DB !!!!!! ");
			
			ResultBean rb = new ResultBean();
			rb.setDescription(dbDoc.getTitle());
			rb.setLocation(dbDoc.getLocation());
			rb.setPageRanking( dbDoc.getPageRankings()
					.get( dbDoc.getPageRankings().size() - 1 ));
			rb.setTdIdf(w.getTfIdf());
			
			double score= (rb.getTdIdf() * Value.TF_IDF_WEIGHT) + (rb.getPageRanking() * Value.LINK_ANALYSIS_WEIGHT);
			rb.setScore(score);
			
			results.add(rb);
		}
		
		//sort the results here
		List<ResultBean> resultsSorted = new ArrayList<ResultBean>();
		
		for(int i=0; i<results.size()-1 ; i++ )
		{
			
			
			
			
			if(resultsSorted.size() == 20)
				break;
		}
		

		return resultsSorted;
	}
*/
	
	
	public static void main(String sr[]) throws IOException, ParseException {
		StringTokenizer st = new StringTokenizer("donalds");
		List<ResultBean> results = null;
		if (st.countTokens() > 1) {
			searchIndexWithQueryParser("mc donalds");
		} else
			results = singleTermSearch("b");
			System.out.println("RESULTS SIZE :"+results.size());

	}

}
