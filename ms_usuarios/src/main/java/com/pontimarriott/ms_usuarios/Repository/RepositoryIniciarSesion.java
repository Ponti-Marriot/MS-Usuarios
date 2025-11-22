package com.pontimarriott.ms_usuarios.Repository;

import com.pontimarriott.ms_usuarios.Model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryIniciarSesion extends JpaRepository<Empleado, String> {
    Empleado findByCorreo(String correo);
}
