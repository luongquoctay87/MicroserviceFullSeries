package vn.tayjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OneSignalService {

    @Value("${oneSignal.apiUrl}")
    private String oneSignalApiUrl;

    @Value("${oneSignal.apiKey}")
    private String apiKey;

    @Value("${oneSignal.appId}")
    private String appId;

    private final RestTemplate restTemplate;

    public OneSignalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendPushNotification(String message, String playerId) {
        log.info("Sending push notification to {}", playerId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("app_id", appId);

        Map<String, String> contents = new HashMap<>();
        contents.put("en", message);
        body.put("contents", contents);
        body.put("include_player_ids", new String[]{playerId});

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                oneSignalApiUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}


