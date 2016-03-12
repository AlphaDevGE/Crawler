package web.crawler.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import web.crawler.db.dao.DocDao;
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
		
		double d = 0.85;
		int n = docs.size();
		double rank = 1/n;
		 //for test
		for(Doc doc: docs)
		{
//			doc.getPageRankings().add(rank);

			double testRank = Math.random();
			System.out.println(testRank); 
			if(doc.getPageRankings() == null)
				doc.setPageRankings(new ArrayList<Double>());
			doc.getPageRankings().add(testRank);
			//50 is the maximum and the 1 is our minimum 
			docDao.saveDoc(doc);
		}
		
//		if (entry.getValue().getP().getInlinks()!= null)
//		{
//			ArrayList<String> inrank = new ArrayList<String>();
//			
//			inrank = entry.getValue().getP().getInlinks();
//			
//			for (String s : inrank)
//			{
//				
//				
//				rank = toRank.get(s).getRank()/toRank.get(s).getOutgo();
//				
//				System.out.println("rank "+rank);
//				
//				sum = sum + rank;
//			}
//			System.out.println("sum "+sum);
//			
//			rank=PR(N,sum);
//			System.out.println("Calculated : "+rank);
//			
//			entry.getValue().setCurr(rank);
//		}
//		else
//		{
//			entry.getValue().setCurr(entry.getValue().getRank());
//		}
		
		
	}

}
