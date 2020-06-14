package versatile_development.entity.nasa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import versatile_development.entity.BaseEntity;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class PictureOfDay extends BaseEntity {
    @JsonProperty("copyright")
    private String copyright;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("explanation")
    private String explanation;

    @JsonProperty("hdurl")
    private String hdUrl;

    @JsonProperty("media_type")
    private String mediaType;

    @JsonProperty("service_version")
    private String serviceVersion;

    @JsonProperty("title")
    private String title;

    @JsonProperty("url")
    private String url;
}
