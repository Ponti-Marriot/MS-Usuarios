package com.pontimarriott.ms_usuarios.Model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String id_keycloak;
    private String nombre;
    private String correo;

    public Empleado(String id_keycloak, String nombre, String correo) {
        this.id_keycloak = id_keycloak;
        this.nombre = nombre;
        this.correo = correo;
    }

    
}




