package web.crawler.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * if you get "Injection of autowired dependencies failed;" error make sure your 
 * Dao class is in the same package where MongoRepository interface exists
 */

@SpringBootApplication
public class WebDao implements CommandLineRunner {

	@Autowired
	private WebRepository repository;
	
	
	@Override
	public void run(String... args) throws Exception {

		repository.save(
				new Web(
						"www.dummy.com", 
						"HASH_Dummy_123456", 
						"Dummy content ...",
						"Dummy header ..."));
		
		
		System.out.println("WebDao: retriving Web data from DB: findByUrl('www.dummy.com')");
		System.out.println(repository.findByUrl("www.dummy.com"));
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(WebDao.class, args);

	}

}
