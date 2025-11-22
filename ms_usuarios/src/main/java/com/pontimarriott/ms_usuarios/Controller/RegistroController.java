package com.pontimarriott.ms_usuarios.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pontimarriott.ms_usuarios.Model.Empleado;
import com.pontimarriott.ms_usuarios.Model.EmpleadoNuevo;
import com.pontimarriott.ms_usuarios.Service.RegistroService;


@RequestMapping("/registro")
@RestController
public class RegistroController {

    @Autowired
    private RegistroService registroService;


    @PostMapping
    public Empleado Registro(@RequestBody EmpleadoNuevo empleado) throws JsonProcessingException {
        return registroService.registro(empleado);
    }
    
}
