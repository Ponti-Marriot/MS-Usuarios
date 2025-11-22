package com.pontimarriott.ms_usuarios.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pontimarriott.ms_usuarios.Model.UsuarioDTO;

public interface InicioSesionService {

    UsuarioDTO login(String username, String contrasenia) throws JsonProcessingException;
} 
