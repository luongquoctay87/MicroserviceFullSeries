package vn.tayjava.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    @JsonProperty("active")
    ACTIVE("active"),

    @JsonProperty("inactive")
    INACTIVE("inactive"),

    @JsonProperty("locked")
    LOCKED("locked"),

    @JsonProperty("none")
    NONE("none");

    private String value;
}
