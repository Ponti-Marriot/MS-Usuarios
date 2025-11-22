package com.pontimarriott.ms_usuarios.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoNuevo {
    private String id;
    private String nombre;
    private String correo;
    private String contrasenia;
}




