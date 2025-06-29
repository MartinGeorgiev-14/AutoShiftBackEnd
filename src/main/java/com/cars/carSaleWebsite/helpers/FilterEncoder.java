package com.cars.carSaleWebsite.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@Component
public class FilterEncoder {

    public String encodeFilters(Map<String, Object> filters) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(filters);
        return Base64.getUrlEncoder().encodeToString(json.getBytes());
    }

    public Map<String, Object> decodeFilters(String encoded) throws Exception {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encoded);
        String json = new String(decodedBytes);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }
}
