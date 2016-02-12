package web.crawler.db.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

import web.crawler.constant.DBName;

@Configuration
public class UrlConfig{

	public @Bean
	MongoTemplate mongoTemplate() throws Exception {
		
		MongoTemplate mongoTemplate = 
			new MongoTemplate(new MongoClient("127.0.0.1"), DBName.CRAWLER);
		return mongoTemplate;
		
	}
		
}