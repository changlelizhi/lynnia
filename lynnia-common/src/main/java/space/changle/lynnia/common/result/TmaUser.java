package space.changle.lynnia.common.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/3 22:11
 * @description
 */
@Data
public class TmaUser {

    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("username")
    private String tgUserName;

    @JsonProperty("photo_url")
    private String photoUrl;
}
