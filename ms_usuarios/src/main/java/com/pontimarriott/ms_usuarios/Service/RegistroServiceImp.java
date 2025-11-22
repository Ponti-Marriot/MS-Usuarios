package com.pontimarriott.ms_usuarios.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.pontimarriott.ms_usuarios.Model.*;
import com.pontimarriott.ms_usuarios.Repository.RepositoryRegistrarse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RegistroServiceImp implements RegistroService{

    @Autowired
    RepositoryRegistrarse registrarseRepository;

    @Value("${keycloak.login-url}")
    private String loginUrl;

    @Value("${keycloak.register-url}")
    private String registerUrl;


    public String getAdminToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli"); // o el cliente con rol admin
        body.add("username", "admin");      // usuario admin del realm
        body.add("password", "admin123");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                loginUrl,
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }


    @Override
    public Empleado registro( EmpleadoNuevo user) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> userKeycloak = new HashMap<>();
        userKeycloak.put("username", user.getCorreo());
        userKeycloak.put("enabled", true);

        // password inicial
        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", user.getContrasenia());
        credential.put("temporary", false);

        userKeycloak.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userKeycloak, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                registerUrl,
                request,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            String location = response.getHeaders().getFirst("Location");
            if (location != null) {
                String userId = location.substring(location.lastIndexOf("/") + 1);
                Empleado usuarioInsertar = new Empleado(userId, user.getNombre(),user.getCorreo());
                Empleado usuario = registrarseRepository.save(usuarioInsertar);
        
                return usuario;
            }
        }
        return null;
    }
    
}
