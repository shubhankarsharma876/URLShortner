package shortUrl.URLShortner.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import shortUrl.URLShortner.model.Url;

@Repository
public interface UrlRepository extends MongoRepository<Url, String> {

}
