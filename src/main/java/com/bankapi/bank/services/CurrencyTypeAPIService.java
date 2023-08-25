package com.bankapi.bank.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CurrencyTypeAPIService {
    private static final String apiUrl = "https://economia.awesomeapi.com.br/json/last/USD";

    private final RestTemplate restTemplate = new RestTemplate();

    public Double fetchAPIData() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

         if(responseEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String jsonResponse = responseEntity.getBody();

        String stringBid = extractBidFromJson(jsonResponse);

        return Double.parseDouble(stringBid);
    } 

    private String extractBidFromJson(String json) {
        return json.contains("\"bid\":") ? json.split("\"bid\":")[1].split("\"")[1] : null;
    }
}
