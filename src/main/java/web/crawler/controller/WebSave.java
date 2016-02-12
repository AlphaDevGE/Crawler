package web.crawler.controller;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.data.mongodb.MongoDbFactory;

import web.crawler.model.WebBean;
import web.crawler.model.WebDao;

public class WebSave {

	public static void main(String[] args) {
	    SpringApplication app = new SpringApplication(WebDao.class);
	    app.setBannerMode(Banner.Mode.OFF);
	    app.run(args);
//		SpringApplication.run(WebDao.class, args);


	}

}
