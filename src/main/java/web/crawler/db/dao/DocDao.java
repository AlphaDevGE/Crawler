package web.crawler.db.dao;

import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
	
	public Doc getDocById(String id){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("id").is(id));
		Doc dbDoc = mongoOperation.findOne(findQuery, Doc.class, DBTable.DOC);
		
		return dbDoc;
	}	
	
	public Doc getDocByUrl(String url){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("url").is(url));
		Doc dbDoc = mongoOperation.findOne(findQuery, Doc.class, DBTable.DOC);
		
		return dbDoc;
	}
	
	public Doc getDocByPath(String path){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("location").is(path));
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
	
	public List<Doc> getAllDocs(){
		List<Doc> docs = mongoOperation.findAll(Doc.class, DBTable.DOC);
		
		return docs;
	}
	
	public void dropCollection(){
		mongoOperation.dropCollection(DBTable.DOC);
		
	}
	
	public void saveDoc(Doc doc)
	{
		mongoOperation.save(doc, DBTable.DOC);
		
	}
	
	public void saveDocsList(List<Doc> docs){
		mongoOperation.insertAll(docs);
	}

		
}
