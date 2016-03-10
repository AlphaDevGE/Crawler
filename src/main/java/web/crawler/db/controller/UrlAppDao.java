package web.crawler.db.controller;


import web.crawler.db.dao.UrlDao;
import web.crawler.db.model.Url;

public class UrlAppDao {
	
	public static void main(String[] args) {
		UrlDao urlDao = new UrlDao();
		
		Url url = urlDao.getUrlByUrl("https://www.yahoo.com/parenting/");
		
		// test data from Db
		System.out.println("Metadate found for: " + url.getUrl());
		System.out.println(url.getMetadata());
		
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