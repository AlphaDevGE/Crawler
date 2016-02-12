package web.crawler.db.controller;

import java.util.Date;
import java.util.List;

import web.crawler.db.dao.UrlDao;
import web.crawler.db.model.Url;

public class UrlAppDao {
	
	public static void main(String[] args) {
		UrlDao urlDao = new UrlDao();
		Url url = urlDao.getUrlByUrl("www.URL1.com");
		
		//get the Url object from DB using url String 
		System.out.println(url);
		
		
		List<Url> urls = urlDao.getUrlListByTitle("google");
		System.out.println("list found: ");
		System.out.println(urls);
		
		Url newUrl = new Url("www.yahoo.com", new Date(), 
				"123456789", "c:/datat/123456789.txt", "yahoo metaData", 
				"Yahoo header", "yahoo", "www.yahoo.com/home, www.yahoo.com/profile", "www.google.com");
		urlDao.saveUrl(newUrl);

	}

}