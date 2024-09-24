package vn.tayjava.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {
    @JsonProperty("web")
    WEB("web"),

    @JsonProperty("ios")
    IOS("ios"),

    @JsonProperty("android")
    ANDROID("android"),

    @JsonProperty("miniApp")
    MINI_APP("miniApp");

    private String value;
}
