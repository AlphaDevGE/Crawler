package web.crawler.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;

import com.mongodb.DB;

public class WebBean 
{
	private final MongoDbFactory mongo;

    @Autowired
    public WebBean(MongoDbFactory mongo) {
        this.mongo = mongo;
    }


    public void example() {
        DB db = mongo.getDb();
        db.getCollection("web");

    }

}
