package com.example.formworkflow.security;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtPublicKeyProvider {

    private final KeycloakConfigProperties keycloakConfigProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, PublicKey> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void preloadJwks() {
        try {
            loadJwksFromKeycloak(); // Optionally preload to avoid latency on first call
        } catch (Exception e) {
            log.warn("Unable to preload JWKS from Keycloak: {}", e.getMessage());
        }
    }

    public PublicKey getPublicKey(String kid) {
        if (cache.containsKey(kid)) {
            return cache.get(kid);
        }
        try {
            loadJwksFromKeycloak();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch JWKS: " + e.getMessage(), e);
        }

        PublicKey key = cache.get(kid);
        if (key == null) {
            throw new RuntimeException("Public key with kid=" + kid + " not found in JWKS");
        }
        return key;
    }

    private void loadJwksFromKeycloak() throws Exception {
        String jwksUrl = keycloakConfigProperties.getJwksUrl();
        JsonNode jwks = objectMapper.readTree(new java.net.URL(jwksUrl));

        for (JsonNode keyNode : jwks.get("keys")) {
            String kid = keyNode.get("kid").asText();
            String x5c = keyNode.get("x5c").get(0).asText();

            String pem = "-----BEGIN CERTIFICATE-----\n" + x5c + "\n-----END CERTIFICATE-----";
            PublicKey publicKey = convertX5cToPublicKey(pem);

            cache.put(kid, publicKey);
        }
    }

private PublicKey convertX5cToPublicKey(String pem) throws Exception {
    java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
    try (java.io.InputStream in = new java.io.ByteArrayInputStream(pem.getBytes())) {
        java.security.cert.X509Certificate certificate = (java.security.cert.X509Certificate) certFactory.generateCertificate(in);
        return certificate.getPublicKey();
    }
}

}

