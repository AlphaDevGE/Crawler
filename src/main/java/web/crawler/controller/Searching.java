package web.crawler.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import web.crawler.db.dao.DocDao;
import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.WordDocDao;
import web.crawler.db.model.Doc;
import web.crawler.db.model.Index;
import web.crawler.db.model.WordDoc;

public class Searching {
	static Version version = Version.LUCENE_36;
	static String queryString = "";

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

	public static void main(String sr[]) throws IOException, ParseException {
		HashSet<String> result = searchIndexWithQueryParser("sachdev not Savin");
		for (String doc : result) {
			System.out.println(doc);
		}
	}

}
