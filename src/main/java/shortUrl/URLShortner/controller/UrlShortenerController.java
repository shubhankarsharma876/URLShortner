package shortUrl.URLShortner.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shortUrl.URLShortner.model.Url;
import shortUrl.URLShortner.service.UrlShortenerService;

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
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortId) {
        Optional<Url> url = urlShortenerService.getOriginalUrl(shortId);
        return url.map(value -> ResponseEntity.ok(value.getOriginalUrl()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}