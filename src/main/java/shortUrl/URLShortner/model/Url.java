package shortUrl.URLShortner.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    private String id;
    private String originalUrl;
    private long expirationDate;  // Timestamp for when the URL should expire.
}
