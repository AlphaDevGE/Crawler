package web.crawler.db.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import web.crawler.constant.DBTable;
import web.crawler.db.bean.UrlConfig;
import web.crawler.db.model.Doc;
import web.crawler.db.model.Url;

public class DocDao {
	
	private ApplicationContext ctx = new AnnotationConfigApplicationContext(UrlConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
	
	public List<Doc> getDocByUrlTerm(String term){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("url").regex(term+".*"));
		List<Doc> docs = mongoOperation.find(findQuery, Doc.class, DBTable.DOC);
		
		return docs;
	}
	
	
	public Doc getDocByUrl(String url){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("url").is(url));
		Doc dbDoc = mongoOperation.findOne(findQuery, Doc.class, DBTable.DOC);
		
		return dbDoc;
	}
	
	public Doc getDocByHash(String hash){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("hash").is(hash));
		Doc dbDoc = mongoOperation.findOne(findQuery, Doc.class, DBTable.DOC);
		
		return dbDoc;
	}
	
	public List<Doc> getDocListByTitle(String title){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("title").is(title));
		List<Doc> docs = mongoOperation.find(findQuery, Doc.class, DBTable.DOC);
		
		return docs;
	}
	
	public List<Doc> getAllUrls(){
		List<Doc> docs = mongoOperation.findAll(Doc.class, DBTable.DOC);
		
		return docs;
	}
	
	public void dropCollection(){
		mongoOperation.dropCollection(DBTable.DOC);
		
	}
	
	public void saveUrl(Doc doc)
	{
		mongoOperation.save(doc, DBTable.DOC);
	}

	
}
