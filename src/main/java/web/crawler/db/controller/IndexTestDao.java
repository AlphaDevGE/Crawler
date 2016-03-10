package web.crawler.db.controller;


import java.util.ArrayList;
import java.util.List;

import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.UrlDao;
import web.crawler.db.dao.WordDocDao;
import web.crawler.db.model.Index;
import web.crawler.db.model.Url;
import web.crawler.db.model.WordDoc;

public class IndexTestDao {
	
	public static void main(String[] args) {
		
		
		IndexDao indexDao = new IndexDao();
		WordDocDao wdDao = new WordDocDao();
		
		//create sample data
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(11);
		list.add(111);
		WordDoc wd1 = new WordDoc("111111", 1.25, 1.1, 1.50, 1, 3, list, 1);
		WordDoc wd2 = new WordDoc("222222", 2.25, 2.1, 2.50, 2, 5, list, 2);
		
		Index i1 = new Index("cat", new ArrayList<WordDoc>());
		i1.addWordDoc(wd1);
		i1.addWordDoc(wd2);
		
		Index i2 = new Index("dog", new ArrayList<WordDoc>());
		i2.addWordDoc(wd2);
		
		Index i3 = new Index("dogma", new ArrayList<WordDoc>());
		i3.addWordDoc(wd1);
		
		indexDao.saveIndex(i1);
		indexDao.saveIndex(i2);
		indexDao.saveIndex(i3);
		
		// test data from Db
		System.out.println("Index found using getUrlBySimilarTerm : " + indexDao.getUrlBySimilarTerm("d").get(0).getTerm());
		
		//get the Url object from DB using url String 
//		System.out.println(url);
		
//		List<Url> urls = urlDao.getUrlListByTitle("google");
//		System.out.println("list found: ");
//		System.out.println(urls);
		
		//save in DB
//		Url newUrl = new Url("www.yahoo.com", new Date(), 
//				"123456789", "c:/datat/123456789.txt", "yahoo metaData", 
//				"Yahoo header", "yahoo", null , "www.google.com", "Yahoo Content");
//		urlDao.saveUrl(newUrl);
		
//		List<Url> urls2 = urlDao.getAllUrls();
//		System.out.println("list found: ");
//		System.out.println(urls2);
		
//		urlDao.dropCollection();
//		System.out.println("Collection Url droped...");
		
		
	

	}

}