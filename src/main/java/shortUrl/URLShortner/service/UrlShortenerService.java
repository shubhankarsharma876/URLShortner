package shortUrl.URLShortner.service;

import org.springframework.stereotype.Service;
import shortUrl.URLShortner.model.Url;

import java.util.Optional;

@Service
public interface UrlShortenerService {
    Url shortenUrl(String originalUrl);
    Optional<Url> getOriginalUrl(String id);
}
