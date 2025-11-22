package com.pontimarriott.ms_usuarios.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pontimarriott.ms_usuarios.Model.UsuarioDTO;
import org.springframework.web.bind.annotation.GetMapping;
import com.pontimarriott.ms_usuarios.Service.InicioSesionService;

import jakarta.ws.rs.QueryParam;

@RequestMapping("/inicioSesion")
@RestController
public class InicioSesionController {

    @Autowired
    private InicioSesionService serviceIniciarSesion;

    @GetMapping
    public UsuarioDTO iniciarSesion(@QueryParam("correo") String correo, @QueryParam("contrasenia") String contrasenia) throws JsonProcessingException {
        return serviceIniciarSesion.login(correo, contrasenia);
    }
    
}
