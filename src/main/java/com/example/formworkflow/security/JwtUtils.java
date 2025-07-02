package com.example.formworkflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class JwtUtils {

    public static String getKeyIdFromToken(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length < 2) throw new IllegalArgumentException("Invalid JWT token");

        String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
        Map<String, Object> header = new ObjectMapper().readValue(headerJson, Map.class);

        return (String) header.get("kid");
    }
}

