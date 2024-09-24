package vn.tayjava.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {

    @JsonProperty("owner")
    OWNER("owner"),

    @JsonProperty("admin")
    ADMIN("admin"),

    @JsonProperty("user")
    USER("user");

    private String value;
}
