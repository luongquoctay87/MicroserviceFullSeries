package vn.tayjava.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("RedisToken")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisToken implements Serializable {
    private String id;
    private String accessToken;
    private String refreshToken;
    private String deviceToken;
    private String flatForm;
    private String roles;

}
