package com.pontimarriott.ms_usuarios.Service;

import com.pontimarriott.ms_usuarios.Model.Empleado;
import com.pontimarriott.ms_usuarios.Model.EmpleadoNuevo;

public interface RegistroService {
    Empleado registro(EmpleadoNuevo user);
}
