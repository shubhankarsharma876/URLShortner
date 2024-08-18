package shortUrl.URLShortner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shortUrl.URLShortner.model.Url;
import shortUrl.URLShortner.service.UrlShortenerService;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("api/")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestBody Url urlRequest) {
        if (urlRequest.getOriginalUrl() == null || urlRequest.getOriginalUrl().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Url shortenUrl = urlShortenerService.shortenUrl(urlRequest.getOriginalUrl());
        return ResponseEntity.ok(shortenUrl);
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortId) {
        Optional<Url> url = urlShortenerService.getOriginalUrl(shortId);
        if (url.isPresent()) {
            String originalUrl = url.get().getOriginalUrl();

            // Ensure the original URL has the correct protocol
            if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
                originalUrl = "https://" + originalUrl;
            }

            // Redirect to the original URL
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
