package com.pontimarriott.ms_usuarios.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pontimarriott.ms_usuarios.Model.Empleado;

@Repository
public interface RepositoryRegistrarse  extends JpaRepository<Empleado, String>{
    
}
