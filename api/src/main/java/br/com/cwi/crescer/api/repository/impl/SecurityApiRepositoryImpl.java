package br.com.cwi.crescer.api.repository.impl;

import br.com.cwi.crescer.api.repository.SecurityApiRepository;
import br.com.cwi.crescer.api.security.AuthenticatedUser;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Repository
public class SecurityApiRepositoryImpl implements SecurityApiRepository {

    @Value("${auth-url}")
    private String authUrl;

    @Override
    public String loginUser(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));

        headers.add(HttpHeaders.USER_AGENT, "Application");
        headers.setBasicAuth("crescer-app", "crescer-homolog");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);
        map.add("scope", "crescer-api");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<JsonNode> response = new RestTemplate().postForEntity(
                authUrl.concat("/token"),
                request,
                JsonNode.class
        );

        return response.getBody().get("access_token").asText();
    }

    @Override
    public AuthenticatedUser getUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));

        headers.add(HttpHeaders.USER_AGENT, "Application");
        headers.setBasicAuth("crescer-api", "crescer-homolog");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {

            ResponseEntity<JsonNode> response = new RestTemplate().postForEntity(
                    authUrl.concat("/introspect"),
                    request,
                    JsonNode.class
            );

            if (!response.getBody().get("active").asBoolean()) {
                throw new UsernameNotFoundException("Token Expirado");
            }

            return new AuthenticatedUser(
                    response.getBody().get("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier").asText(),
                    response.getBody().get("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress").asText(),
                    response.getBody().get("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name").asText()
            );

        } catch (Exception exception) {
            throw new UsernameNotFoundException("Usuário não econtrado");
        }
    }
}
