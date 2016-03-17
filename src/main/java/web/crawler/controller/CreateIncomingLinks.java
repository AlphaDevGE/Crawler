package web.crawler.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import web.crawler.db.dao.DocDao;
import web.crawler.db.model.Address;
import web.crawler.db.model.Doc;

public class CreateIncomingLinks {
	
	public static void main(String[] args) {
		
		System.out.println("Wait! Creating Incoming URL from your Database...");
		List<Doc> docs = new ArrayList<Doc>();
		DocDao docDao = new DocDao();
		docs = docDao.getAllDocs(); //get all Docs from DB	
		
		System.out.println(docs.size());
		
		for(Doc d: docs)
		{
			System.out.println("****** Looking at Doc: " + d.getUrl());
			System.out.println("****** No of OutgoingUrl found: " + d.getOutgoingDocsStr().size() );
			int outgoingSize = d.getOutgoingDocsStr().size();
			//create the outgoing Address for current opening Doc
			if(d.getOutgoingAddresses() == null)
				d.setOutgoingAddresses(new HashSet<Address>());
			
			for(String outgoingUrl: d.getOutgoingDocsStr())
			{			
				Doc outgoingDoc = docDao.getDocByUrl(outgoingUrl);
				
				//if corresponding outgoing Doc found in DB
				if( outgoingDoc != null)
				{
					System.out.println("*** OutgoingUrl Found: " + outgoingDoc.getUrl() );
					
					Set<String> incomings = new HashSet<String>();
					if(incomings == null)
					{
						outgoingDoc.setIncomingDocsStr(new HashSet<String>());
						System.out.println("*** New Incoming String HashSet created for outgoing Doc." + outgoingDoc.getUrl());
					}
					for(String s : outgoingDoc.getIncomingDocsStr())
						incomings.add(s);
					
					//add the new incoming
					incomings.add(d.getUrl());
					
					//replace the old incomingDocStr with new one
					outgoingDoc.setIncomingDocsStr(incomings);
						
//					if(outgoingDoc.getIncomingAddresses() == null)
//					{
//						outgoingDoc.setIncomingAddresses(new HashSet<Address>());
//						System.out.println("*** New Incoming Address HashSet created for outgoing Doc url: " + outgoingDoc.getUrl());
//					}
					
					Address outgoingDocAddress = new Address(
							outgoingDoc.getUrl(), outgoingDoc.getPath(), outgoingDoc.getOutgoingAddresses().size()); 				
					d.getOutgoingAddresses().add(outgoingDocAddress);
					
					Address currentDocAddress = new Address(d.getUrl(), d.getPath(), outgoingSize);
					outgoingDoc.getIncomingAddresses().add(currentDocAddress);					
					
//					outgoingDoc.getIncomingDocsStr().add(d.getUrl());
					//save to DB
					docDao.updateIncommingLink(outgoingUrl, outgoingDoc.getIncomingDocsStr());
					docDao.saveDoc(outgoingDoc);
					
				}
				else
					System.out.println("Outgoing Url: " + outgoingUrl + " Not found in DB....");
				
				docDao.saveDoc(d);
			}
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
