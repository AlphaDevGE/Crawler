package web.crawler.db.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import web.crawler.constant.DBTable;
import web.crawler.db.bean.UrlConfig;
import web.crawler.db.model.Doc;
import web.crawler.db.model.Docs;

public class DocsDao {
	
	private ApplicationContext ctx = new AnnotationConfigApplicationContext(UrlConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
	public List<Docs> getAllDocs(){
		List<Docs> docs = mongoOperation.findAll(Docs.class, DBTable.DOCS);
		
		return docs;
	}
	
	public void dropCollection(){
		mongoOperation.dropCollection(DBTable.DOCS);
		
	}
	
	public void saveDocs(List<Doc> docs)
	{
		mongoOperation.save(docs, DBTable.DOCS);
		System.out.println("Docs saved in DB ... ");
		
	}

}
