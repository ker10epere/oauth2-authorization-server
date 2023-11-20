package com.kerdagreat.authserverspringweb.config;

import com.nimbusds.jose.jwk.RSAKey;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class KeyManager {
    @Value("classpath:keys/public.pem")
    Resource publicKeyResource;
    @Value("classpath:keys/key.pem")
    Resource privateKeyResource;

    private RSAPublicKey publicKey() throws Exception {
        String key = new String(Files.readAllBytes(publicKeyResource.getFile().toPath()), Charset.defaultCharset());

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.decodeBase64(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private RSAPrivateKey privateKey() throws Exception {
        String key = new String(Files.readAllBytes(privateKeyResource.getFile().toPath()), Charset.defaultCharset());

        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.decodeBase64(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    @Bean
    public RSAKey rsaKey() throws Exception {
        return new RSAKey.Builder(publicKey())
                .privateKey(privateKey())
                // Should be dynamic in production
                // Used this for development so that I can use same RSA on each server reload
                .keyID("001-Static-KeyId")
                .build();
    }
}
