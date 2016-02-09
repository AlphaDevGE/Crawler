package web.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import web.crawler.model.Web;
import web.crawler.model.WebRepository;

@SpringBootApplication
public class Spider implements CommandLineRunner {

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
		
		
		System.out.println("Spider: retriving Web data from DB:");
		System.out.println(repository.findByUrl("www.dummy.com"));
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Spider.class, args);

	}

}
