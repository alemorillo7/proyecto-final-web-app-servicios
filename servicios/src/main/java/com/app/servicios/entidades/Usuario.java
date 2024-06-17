package com.app.servicios.entidades;

import java.util.HashSet;
import java.util.Set;

import com.app.servicios.enumeraciones.Rol;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Basic
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String direccion;
    private String localidad;
    private String barrio;
    private Integer telefono;
    private String imagen;

    // atributos adicionales para proveedor
    @ManyToMany
    @JoinTable(name = "proveedor_servicio", joinColumns = @JoinColumn(name = "proveedor_id"), inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private Set<Servicio> servicios = new HashSet<>();
    private Integer dni;
    private Integer experiencia;
    private String descripcion;

    
    
}
