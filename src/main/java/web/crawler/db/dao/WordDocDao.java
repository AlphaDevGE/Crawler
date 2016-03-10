package web.crawler.db.dao;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import web.crawler.constant.DBTable;
import web.crawler.db.bean.UrlConfig;
import web.crawler.db.model.WordDoc;

public class WordDocDao {
	
	private ApplicationContext ctx = new AnnotationConfigApplicationContext(UrlConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

	public WordDoc getWordDocByDocHash(String hash){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("docHash").is(hash));
		WordDoc dbWordDoc = mongoOperation.findOne(findQuery, WordDoc.class, DBTable.WORD_DOC);
		
		return dbWordDoc;
	}
	
	public List<WordDoc> getWordDocByScore(double score){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("score").is(score));
		List<WordDoc> dbWordDocs = mongoOperation.find(findQuery, WordDoc.class, DBTable.WORD_DOC);
		
		return dbWordDocs;
	}
	
	public List<WordDoc> getAllWordDocs(){
		List<WordDoc> wordDocs = mongoOperation.findAll(WordDoc.class, DBTable.WORD_DOC);
		
		return wordDocs;
	}
	
	public void dropCollection(){
		mongoOperation.dropCollection(DBTable.WORD_DOC);
		
	}
	
	public void saveWordDoc(WordDoc wd)
	{
		mongoOperation.save(wd, DBTable.WORD_DOC);
	}
}
