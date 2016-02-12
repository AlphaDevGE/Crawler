package web.crawler.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * if you get "Injection of autowired dependencies failed;" error make sure your 
 * Dao class is in the same package where MongoRepository interface exists
 */

//check other @notation
@SpringBootApplication
public class UrlDao implements CommandLineRunner {

	@Autowired
	private UrlRepository repository;
	
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("args: " + args);
		
		Url url1 =  new Url(
				"www.dummy.com", 
				"HASH_Dummy_123456" ); 
		
		repository.save(url1);
		
		
		System.out.println("UrlDao: retriving Url data from DB: findByUrl('www.dummy.com')");
		System.out.println(repository.findByUrl("www.dummy.com"));
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(UrlDao.class, args);

	}

}