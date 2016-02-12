package web.crawler.model;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebRepository extends MongoRepository<Web, String> {
   
	public Web findByUrl( String url );
	public Web findByHash( String hash );

}