package com.pontimarriott.ms_usuarios.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private Empleado empleado;
}

