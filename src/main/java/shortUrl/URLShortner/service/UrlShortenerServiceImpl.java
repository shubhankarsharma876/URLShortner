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

    @PostConstruct
    public void init() {
        // Initialize the custom Bloom filter with 3 hash functions
        bloomFilter = new BloomFilter<>(100000, 3, BloomFilter::hash1, BloomFilter::hash2, BloomFilter::hash3);
    }

    @Override
    public Url shortenUrl(String originalUrl) {
        if(bloomFilter.mightContain(originalUrl)) {
            Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
            System.out.println("Url already shortened");
            if(existingUrl.isPresent()) {
                return existingUrl.get();
            }
        }

        String shortUrl = generateShortId();
        Url url = new Url(shortUrl, originalUrl, System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
        // Expires after a week
        bloomFilter.add(originalUrl);
        System.out.println("New URL Made");
        return urlRepository.save(url);
    }

    @Override
    public Optional<Url> getOriginalUrl(String id) {
        return urlRepository.findById(id);
    }

    private String generateShortId() {
        // Generate a random alphanumeric string
        return Long.toHexString(ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE));
    }
}
