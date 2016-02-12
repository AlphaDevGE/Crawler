package web.crawler.model;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, String> {
   
	public Web findByUrl( String url );
	public Web findByHash( String hash );

}