package web.crawler.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import web.crawler.constant.Value;
import web.crawler.db.dao.DocDao;
import web.crawler.db.model.Doc;

public class PageRanking {

	public static void main(String[] args) throws InterruptedException {

		// calculate ranking

		// Ranking (1-d)/N + d ( PR(A) / C(A) )
		// d= constant to manage the weight of the ranking function initially
		// can be ).85
		// N = total number of document
		// PR(A) = page ranking which is initially 1/N
		// C(A) = No. of outgoing links

		System.out.println("Pge Ranking in process...");
		List<Doc> docs = new ArrayList<Doc>();
		DocDao docDao = new DocDao();
		docs = docDao.getAllDocs(); // get all Docs from DB
		
		double n = docs.size();
		double initialRank = ((double) 1) / n;
		List<Double> ranks = new ArrayList<Double>();
		ranks.add(initialRank);
		// make all the pages rank 1/n at initial stage
		// for(Doc doc: docs)
		// {
		// doc.setPageRankings(ranks);
		// docDao.saveDoc(doc);
		// }

		Map<String, Doc> docMap = new HashMap<String, Doc>();
		for (Doc doc : docs) {
			List<Double> eachDocRank=new ArrayList<Double>();
			eachDocRank.add(initialRank);
			doc.setPageRankings(eachDocRank);
			docMap.put(doc.getUrl(), doc);
		}
		System.out.println("docMap size: " + docMap.size());

		// do next 10 iteration and save ranking of each page
		// stop saving at iteration if the value is equal to previous iteration

		Set<String> urlKeys = docMap.keySet();
		System.out.println("Key Map Size is:" + urlKeys.size());
		
		//DB shoulb be clear here
		//List to add doc objects should also be here
		
			
			
			for (int i = 1; i <= 3; i++) {
			// Map<String, Doc> docMapTemp = new HashMap<String, Doc>();
				for (String url : urlKeys) {
			
				// If already redundant ranking value found in more than 1
				// iteration
				// if(doc.getRankingIterationTimae() >
				// doc.getPageRankings().size())
				// {
				// System.out.println("Already repeated ranking found for Doc: "
				// + doc.getUrl());
				// continue;
				// }
				Doc doc = docMap.get(url);
				double lastRank = doc.getPageRankings().get(
						doc.getPageRankings().size() - 1);
				double sum = 0;
				if (doc.getIncomingDocsStr() != null) {
					if (doc.getIncomingDocsStr().size() != 0) {
						// System.out.println("No incoming for Doc: " +
						// doc.getUrl() + " found.");
						for (String incomingUrl : doc.getIncomingDocsStr()) {
							Doc incomingDoc = docMap.get(incomingUrl);
							sum = sum
									+ incomingDoc.getPageRankings().get((incomingDoc.getPageRankings().size()) - 1)
									/ incomingDoc.getOutgoingDocsStr().size();
							 System.out.println("Sum for Doc: " + doc.getUrl()
							 + " = " + sum);
						}
					}
				} else
					System.out.println("Incoming of Doc: " + doc.getUrl()
							+ "is NULL !!!");

				double newRank = ((((double) 1) - Value.LAMBDA / ((double) 5)) + (Value.LAMBDA * sum))
						* Value.SCALER;
				if (lastRank != newRank) {
					System.out.println("Iteration: " + i + " page ranking "+
					 newRank +" for Doc: " + doc.getUrl() + "added." );
					List<Double> pageRankList= doc.getPageRankings();
					System.out.println("Page Rank List size for doc: "+doc.getUrl()+" is:"+pageRankList.size());
					
					pageRankList.add(newRank);
					doc.setPageRankings(pageRankList);
				}
				// else
				// System.out.println("Iteration: " + i + " Doc: " +
				// doc.getUrl() + " old and New Page ranking are same:  "+
				// lastRank +" = " + newRank);

				doc.setRankingIterationTimae(i);
				
				docMap.put(url, doc);
				// docDao.saveDoc(doc);
			}

			// get the doc map iterate througn each entry and put it in the list
			// and later save it in the database
			
			
		}
			docDao.dropCollection();
		
		List<Doc> docsForDb = new ArrayList<Doc>();
		Set<String> mapIterator = docMap.keySet();
		for (String key : mapIterator) {
			docsForDb.add(docMap.get(key));
			if (docsForDb.size() == 1000) {
				docDao.saveDocsList(docsForDb);
				docsForDb.clear();
			}
		}
		if (docsForDb.size() > 0) {
			docDao.saveDocsList(docsForDb);
		}

		// int i = 1;
		// for(String url: docMap.keySet())
		// if(docMap.get(url).getPageRankings().size()>1)
		// System.out.println(i++ + ": url: " + url + "Pagerank: " +
		// docMap.get(url).getPageRankings().get(docMap.get(url).getPageRankings().size()-1));
		//
		// //display some sample page ranking
		// docs = docDao.getAllDocs();
		// System.out.println(" ****************** Sample Page Ranking *********************** ");
		// for(Doc doc : docs)
		// {
		// if(doc.getIncomingDocsStr() == null)
		// System.out.println("INCOMING LINK IS NULL !!!");
		// else if(doc.getIncomingDocsStr().size() > 0)
		// System.out.println("Doc: " +doc.getUrl()+ " Rankins: " +
		// doc.getPageRankings());
		// }/

	}
}