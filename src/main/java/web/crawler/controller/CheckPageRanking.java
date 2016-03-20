package web.crawler.controller;

import java.util.List;

import web.crawler.db.dao.DocDao;
import web.crawler.db.model.Doc;

public class CheckPageRanking {

	public static void main(String[] args) {
		DocDao docDao = new DocDao();
		List<Doc> docs = docDao.getAllDocs();
		
		//display some sample page ranking
		int count =0;
		docs = docDao.getAllDocs();
		System.out.println(" ****************** Sample Page Ranking *********************** ");
		Doc docWithMaxOutgoing = docs.get(0);
		Doc docWithMaxRanks = docs.get(0);
		Doc docWithMinRanks = docs.get(0);
		for(Doc doc : docs)
		{
			if(doc.getIncomingDocsStr() == null)
				System.out.println("INCOMING LINK IS NULL !!!");
			else if(doc.getIncomingDocsStr().size() > 0)
			{
				if(doc.getIncomingDocsStr().size() > docWithMaxOutgoing.getIncomingDocsStr().size())
					docWithMaxOutgoing = doc;
				count++;
				System.out.println("Incoming Size: " +doc.getIncomingDocsStr().size()+ " Rankins: " + doc.getPageRankings());
			}
			
			if( docWithMaxRanks.getPageRankings().size() < doc.getPageRankings().size() )
				docWithMaxRanks = doc;
			if(doc.getPageRankings().get(doc.getPageRankings().size()-1) < 
					docWithMinRanks.getPageRankings().get(docWithMinRanks.getPageRankings().size()-1) )
				docWithMinRanks = doc;
		}
		
		System.out.println("Total Docs: "+docs.size() + " | Toatal # Docs with outgoings: " + count);
		
		System.out.println("Doc with Max Incomings: ");
		System.out.println("URL: " + docWithMaxOutgoing.getUrl());
		System.out.println("No of Incomings: " + docWithMaxOutgoing.getIncomingDocsStr().size());
		System.out.println("Ranks: ");
		for(double d : docWithMaxOutgoing.getPageRankings())
			System.out.println(d);
		
		System.out.println("Doc with maximum number of rankings: url = " + docWithMaxRanks.getUrl() );
		for(double d : docWithMaxRanks.getPageRankings())
			System.out.println(d);
		
		System.out.println("Doc with Miniuimum number page ranks : url = " + docWithMinRanks.getUrl() );
		for(double d : docWithMinRanks.getPageRankings())
			System.out.println(d); 

	}

}
