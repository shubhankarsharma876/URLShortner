package shortUrl.URLShortner.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shortUrl.URLShortner.bloomFilter.BloomFilter;
import shortUrl.URLShortner.model.Url;
import shortUrl.URLShortner.repository.UrlRepository;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    @Autowired
    private UrlRepository urlRepository;

    private BloomFilter<String> bloomFilter;


    //Post Construct: The PostConstruct annotation is used on a method that needs to be executed after dependency
    // injection is done to perform any initialization. This method must be invoked before the class is put into
    // service. This annotation must be supported on all classes that support dependency injection.
    @PostConstruct
    public void init(){
        // Initialize the custom Bloom filter with 3 hash functions
        bloomFilter = new BloomFilter<>(100000, 3, BloomFilter::hash1, BloomFilter::hash2, BloomFilter::hash3);
    }

    @Override
    public Url shortenUrl(String originalUrl) {
        if(bloomFilter.mightContained(originalUrl)){
            Optional<Url> existingUrl = urlRepository.findById(originalUrl);
            if(existingUrl.isPresent()){
                return existingUrl.get();
            }
        }

        String shortUrl = generateShortId();
        Url url = new Url(shortUrl,originalUrl,System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
        //Expires after a week
        bloomFilter.add(originalUrl);
        return urlRepository.save(url);
    }

    @Override
    public Optional<Url> getOriginalUrl(String id) {
        return urlRepository.findById(id);
    }


    private String generateShortId() {
        //generate a random alphanumeric string
        return Long.toHexString(ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE));
    }


}
