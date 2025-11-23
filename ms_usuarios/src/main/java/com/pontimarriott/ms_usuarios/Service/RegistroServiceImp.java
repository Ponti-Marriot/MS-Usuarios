package com.pontimarriott.ms_usuarios.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



import com.pontimarriott.ms_usuarios.Model.Empleado;
import com.pontimarriott.ms_usuarios.Model.EmpleadoNuevo;
import com.pontimarriott.ms_usuarios.Repository.RepositoryRegistrarse;

import org.springframework.http.HttpMethod;



@Service
public class RegistroServiceImp implements RegistroService{

    @Autowired
    RepositoryRegistrarse registrarseRepository;

    @Autowired
    MessageEmpleadoService messageEmpleadoService;

    @Value("${keycloak.login-url}")
    private String loginUrl;

    @Value("${keycloak.register-url}")
    private String registerUrl;


    public String getAdminToken() {
        RestTemplate restTemplate = new RestTemplate();

        String tokenUrl = "http://localhost:9000/realms/myrealm/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Body igual al curl
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", "admin-client");
        body.add("client_secret", "4inrWXpnTE1dQoOEsT80gHNRpTpMhIlJ");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(tokenUrl, request, Map.class);

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

                //assignEmpleadoRole(userId);


                //Empleado usuarioInsertar = new Empleado(userId, user.getNombre(),user.getCorreo());
                Empleado usuarioInsertar = new Empleado();
                usuarioInsertar.setId_keycloak(userId);
                usuarioInsertar.setNombre(user.getNombre());
                usuarioInsertar.setCorreo(user.getCorreo());
                Empleado usuario = registrarseRepository.save(usuarioInsertar);
                messageEmpleadoService.sendMessageEmpleado(usuario);
                return usuario;
            }
        }
        return null;
    }
    public void assignEmpleadoRole(String userId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:9000/admin/realms/myrealm/users/" 
                    + userId 
                    + "/role-mappings/clients/af907948-5cce-444f-b92b-43ccbaf153a9";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Rol que se va a asignar
        Map<String, Object> role = new HashMap<>();
        role.put("id", "c03e5b88-fb8f-432a-b35e-707b6e9a0b67");   // UUID del rol empleado
        role.put("name", "empleado");                             // nombre del rol

        HttpEntity<Object> request = new HttpEntity<>(List.of(role), headers);

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Void.class
        );
    }
    
}
