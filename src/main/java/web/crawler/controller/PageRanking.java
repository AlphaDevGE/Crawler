package web.crawler.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import web.crawler.constant.Value;
import web.crawler.db.dao.DocDao;
import web.crawler.db.model.Address;
import web.crawler.db.model.Doc;

public class PageRanking {

	public static void main(String[] args) {

		//calculate ranking
		
		// Ranking    (1-d)/N + d ( PR(A) / C(A) ) 
		// d= constant to manage the weight of the ranking function initially can be ).85
		//N = total number of document
		// PR(A) = page ranking which is initially 1/N
		// C(A) = No. of outgoing links 
	
	
//			N     = number of incoming links to B
//			PR(A) = PageRank of incoming link A
//			C(A)  = number of outgoing links from page A
		
		System.out.println("Pge Ranking in process...");
		List<Doc> docs = new ArrayList<Doc>();
		DocDao docDao = new DocDao();
		docs = docDao.getAllDocs(); //get all Docs from DB	
		
		double n = docs.size();
		double initialRank = ((double)1) / n;
		List<Double> ranks = new ArrayList<Double>();
		ranks.add(initialRank);
		//make all the pages rank 1/n at initial stage
		for(Doc doc: docs)
		{
			doc.setPageRankings(ranks);
			docDao.saveDoc(doc);
		}
		
		docs = docDao.getAllDocs();
		
		//do next 10 iteration and save ranking of each page
		//stop saving at iteration if the value is equal to previous iteration
		for(int i=1 ; i<=6; i++)
		{
			for(Doc doc: docs)
			{
				//If already redundant ranking value found in more than 1 iteration
//				if(doc.getRankingIterationTimae() > doc.getPageRankings().size()) 
//				{
//					System.out.println("Already repeated ranking found for Doc: " + doc.getUrl());
//					continue;
//				}
					
				double lastRank = doc.getPageRankings().get( doc.getPageRankings().size()-1 );
				double sum = 0;
				if(doc.getIncomingDocsStr() != null )
				{
					if(doc.getIncomingDocsStr().size() != 0)
					{
						System.out.println("No incoming for Doc: " + doc.getUrl() + " found.");
						for(String incomingUrl: doc.getIncomingDocsStr())
						{
							Doc incomingDoc = docDao.getDocByUrl(incomingUrl);
							sum = sum + incomingDoc.getPageRankings()
								.get( incomingDoc.getPageRankings().size()-1 )
								/ incomingDoc.getOutgoingDocsStr().size();
							System.out.println("Sum for Doc: " + doc.getUrl() + " = " + sum);
						}
					}
				}
				else
					System.out.println("Incoming of Doc: " + doc.getUrl() + "is NULL !!!");
				
				double newRank = (( ((double)1) - Value.LAMBDA / ((double)5) ) + ( Value.LAMBDA * sum)) * Value.SCALER;
				if(lastRank != newRank)
				{
					System.out.println("Iteration: " + i + " page ranking "+ newRank +" for Doc: " + doc.getUrl() + "added." );
					doc.getPageRankings().add(newRank);
				}
				else
					System.out.println("Iteration: " + i + " Doc: " + doc.getUrl() +  " old and New Page ranking are same:  "+ lastRank +" = " + newRank);
				
				doc.setRankingIterationTimae(i);
				docDao.saveDoc(doc);
			}
		}	
		
		//display some sample page ranking
		docs = docDao.getAllDocs();
		System.out.println(" ****************** Sample Page Ranking *********************** ");
		for(Doc doc : docs)
		{
			if(doc.getIncomingDocsStr() == null)
				System.out.println("INCOMING LINK IS NULL !!!");
			else if(doc.getIncomingDocsStr().size() > 0)
				System.out.println("Doc: " +doc.getUrl()+ " Rankins: " + doc.getPageRankings());
		}
		
	}
}