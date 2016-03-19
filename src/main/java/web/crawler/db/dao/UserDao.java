package web.crawler.db.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import web.crawler.constant.DBTable;
import web.crawler.db.bean.UrlConfig;
import web.crawler.db.model.User;

public class UserDao {
	
	private ApplicationContext ctx = new AnnotationConfigApplicationContext(UrlConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	
	public User getUserById(String id){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("id").is(id));
		User user = mongoOperation.findOne(findQuery, User.class, DBTable.USER);
		return user;
	}	
	
	public User getUserByIpAddress(String ipAddress){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("ipAddress").is(ipAddress));
		User user = mongoOperation.findOne(findQuery, User.class, DBTable.USER);
		return user;
	}	
	
	public List<User> getUserByZipCode(String zipCode){
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("zipCode").is(zipCode));
		List<User> users = mongoOperation.find(findQuery, User.class, DBTable.USER);
		return users;
	}	
	
	public List<User> getAllUsers(){
		List<User> users = mongoOperation.findAll(User.class, DBTable.USER);
		return users;
	}
	
	public void dropCollection(){
		mongoOperation.dropCollection(DBTable.USER);
	}
	
	public void saveUser(User user)
	{
		mongoOperation.save(user, DBTable.USER);
	}
	
}
