package com.pontimarriott.ms_usuarios.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pontimarriott.ms_usuarios.Model.Empleado;
import com.pontimarriott.ms_usuarios.Model.UsuarioDTO;
import com.pontimarriott.ms_usuarios.Repository.RepositoryIniciarSesion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class InicioSesionServiceImp  implements InicioSesionService {
    @Autowired
    RepositoryIniciarSesion repositoryIniciarSesion;

    @Value("${keycloak.login-url}")
    private String loginUrl;
    

    @Override
    public UsuarioDTO login(String correo, String contrasenia) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        // headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Empleado empleado = repositoryIniciarSesion.findByCorreo(correo);

        // body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "autenticacion");
        body.add("username", correo);
        body.add("password", contrasenia);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                loginUrl,
                request,
                String.class
        );

        // Parsear JSON de respuesta de Keycloak
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.getBody());

        String accessToken = json.get("access_token").asText();
        String refreshToken = json.get("refresh_token").asText();
        long expiresIn = json.get("expires_in").asLong();

        return new UsuarioDTO(accessToken, refreshToken, expiresIn,empleado);
    }
    
}
