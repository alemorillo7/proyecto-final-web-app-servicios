package com.app.servicios.entidades;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.app.servicios.enumeraciones.Rol;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Usuario {
    
    @Id
    @GeneratedValue (generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String email;

    private String nombre;

    private String apellido;

    private Integer dni;

    private String direccion;

    private String localidad;

    private String barrio;

    private String telefono;

   @OneToOne
   private Imagen imagen;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (name = "proveedor_servicio", joinColumns = @JoinColumn(name = "proveedor_id"), inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private Set<Servicio> servicios = new HashSet<>();

    private Integer experiencia;

    private String descripcion;

    private Boolean estado;
}
