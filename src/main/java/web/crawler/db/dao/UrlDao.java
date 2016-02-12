package web.crawler.db.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import web.crawler.constant.DBTable;
import web.crawler.db.bean.UrlConfig;
import web.crawler.db.model.Url;

public class UrlDao {
	
	private ApplicationContext ctx = new AnnotationConfigApplicationContext(UrlConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
	public Url getUrlByUrl(String url){
		
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("url").is(url));
		Url dbUrl = mongoOperation.findOne(findQuery, Url.class, DBTable.URL);
		
		return dbUrl;
	}
	
	public Url getUrlByHash(String hash){

		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("hash").is(hash));
		Url dbUrl = mongoOperation.findOne(findQuery, Url.class, DBTable.URL);
		
		return dbUrl;
	}
	
	public List<Url> getUrlListByTitle(String title){
		
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("title").is(title));
		List<Url> urls = mongoOperation.find(findQuery, Url.class, DBTable.URL);
		
		return urls;
	}
	
	public void saveUrl(Url url)
	{
		mongoOperation.save(url, DBTable.URL);
	}
	
	
	
}
