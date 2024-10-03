package vn.tayjava.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("blocked")
    BLOCKED,
    @JsonProperty("none")
    NONE

}
