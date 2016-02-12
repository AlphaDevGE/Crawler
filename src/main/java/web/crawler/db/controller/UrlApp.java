package web.crawler.db.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import web.crawler.constant.DBTable;
import web.crawler.db.bean.UrlConfig;
import web.crawler.db.model.Url;

public class UrlApp {
	
	public static void main(String[] args) {
		// For Annotation
		ApplicationContext ctx = 
                     new AnnotationConfigApplicationContext(UrlConfig.class);
		
		MongoOperations mongoOperation = 
                     (MongoOperations) ctx.getBean("mongoTemplate");

		// Case1 - insert a url, put "url" as collection name
		System.out.println("Case 1...");
		Url url1 = new Url("www.URL1.com", new Date(), "1111111111", "c:/datat/1111111111.txt"); 
		// without specifying collection name by default saves entity as collection name
		mongoOperation.save(url1, DBTable.URL);

		// find
		Query findUrlQuery = new Query();
		findUrlQuery.addCriteria(Criteria.where("url").is("www.URL1.com"));
		Url urlD1 = mongoOperation.findOne(findUrlQuery, Url.class, DBTable.URL);
		System.out.println("URL found by url in DB:");
		System.out.println(urlD1);

		// Case2 - insert a url
		System.out.println("Case 2...");
		Url url2 = new Url("www.URL2.com", new Date(), "2222222222", "c:/datat/2222222222.txt"); 
		mongoOperation.save(url2, DBTable.URL);
		

		// find
		Url urlD2 = mongoOperation.findOne(
                     new Query(Criteria.where("hash").is("2222222222")), Url.class);
		System.out.println("URL found by hash in DB:");
		System.out.println(urlD2);

		// Case3 - insert a list of urls
		System.out.println("Case 3...");
		Url urlA = new Url("www.URLA.com", new Date(), "AAAAAAAAAA", "c:/datat/AAAAAAAAAA.txt"); 
		Url urlB = new Url("www.URLB.com", new Date(), "BBBBBBBBBB", "c:/datat/BBBBBBBBBB.txt"); 
		Url urlC = new Url("www.URLC.com", new Date(), "CCCCCCCCCC", "c:/datat/CCCCCCCCCC.txt"); 
		List<Url> urlList = new ArrayList<Url>();
		urlList.add(urlA);
		urlList.add(urlB);
		urlList.add(urlC);
		mongoOperation.insert(urlList, DBTable.URL);

		// find
		List<Url> urls = mongoOperation.find(
                           new Query(Criteria.where("hash").is("CCCCCCCCCC")),
			   Url.class);

		for (Url u : urls) {
			System.out.println(u);
		}
		
		//save vs insert
		System.out.println("Case 4...");
		//NulpointerException
		Url url11 = mongoOperation.findOne(
                          new Query(Criteria.where("hash").is("1111111111")), Url.class);
		url11.setUrl("www.url11.com");
		
		//E11000 duplicate key error index, _id existed
		//mongoOperation.insert(url11); 
		mongoOperation.save(url11);
		
		//find update url in DB
		findUrlQuery.addCriteria(Criteria.where("hash").is("1111111111"));
		Url urlD11 = mongoOperation.findOne(findUrlQuery, Url.class, DBTable.URL);
		System.out.println("after updating url1:");
		System.out.println(urlD11);
		
	}

}