package web.crawler.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import web.crawler.db.dao.DocDao;
import web.crawler.db.model.Address;
import web.crawler.db.model.Doc;

public class LinkAnalysis {
	
	public static void main(String[] args) {
		
		System.out.println("Wait! Creating Incoming URL from your Database...");
		List<Doc> docs = new ArrayList<Doc>();
		DocDao docDao = new DocDao();
		docs = docDao.getAllDocs(); //get all Docs from DB	
		
		for(Doc d1: docs)
		{
			System.out.println("****** Looking at Doc: " + d1.getUrl());
			System.out.println("****** No of OutgoingUrl found: " + d1.getOutgoingDocsStr().size() );
			int outgoingSize = d1.getOutgoingDocsStr().size();
			//create the outgoing Address for current opening Doc
			if(d1.getOutgoingAddresses() == null)
				d1.setOutgoingAddresses(new HashSet<Address>());
			
			Set<String> incomings = new HashSet<String>();
			Set<Address> incomingAddresses = new HashSet<Address>();
			Set<Address> outgoingAddresses = new HashSet<Address>();
			for(Doc d2: docs)
			{
				if(d1.getUrl().equals(d2.getUrl()))
					continue;
				
				for(String str : d2.getOutgoingDocsStr())
				{
					if(str.equals(d1.getUrl()))
					{
						incomings.add(d2.getUrl());
						incomingAddresses.add(new Address(d2.getUrl(), d2.getPath(), d2.getOutgoingDocsStr().size()));
					}	
				}
			}
			
			d1.setIncomingDocsStr(incomings);
			d1.setIncomingAddresses(incomingAddresses);
			
			
			docDao.saveDoc(d1);
		}
	
	
		
		
//		for(Doc d: docs)
//		{
//			System.out.println("****** No of OutgoingUrl found: " + d.getOutgoingDocsStr().size() );
//			for(String outgoingUrl: d.getOutgoingDocsStr())
//			{
//				Doc outgoingDoc = docDao.getDocByUrl(outgoingUrl);
//				System.out.println("**** OutgoingUrl Found: " + outgoingDoc.getUrl() );
//				if(outgoingDoc.getIncomingDocsStr() == null)
//					outgoingDoc.setIncomingDocsStr(new HashSet<String>());
//				outgoingDoc.getIncomingDocsStr().add(d.getUrl());
//				docDao.updateIncommingLink(outgoingUrl, outgoingDoc.getIncomingDocsStr());
//			}
//		}
		
		System.out.println("Incoming Links Creation Complited...");
	}

}
