package com.app.servicios.entidades;

import org.hibernate.annotations.GenericGenerator;

import com.app.servicios.enumeraciones.EstadoCalificacion;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Calificacion {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private Integer puntaje;

    private String comentario;

    @ManyToOne
    private Usuario cliente;

    @ManyToOne
    private Usuario proveedor;

    @OneToOne
    private OrdenTrabajo ordenTrabajo;

    @Enumerated(EnumType.STRING)
    private EstadoCalificacion estadoCalificacion;

    private boolean activo;
}