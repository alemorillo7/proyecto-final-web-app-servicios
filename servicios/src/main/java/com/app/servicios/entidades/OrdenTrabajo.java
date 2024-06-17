package com.app.servicios.entidades;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orden_trabajo")
public class OrdenTrabajo {
    
    
    @Id
    @GeneratedValue (generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private Integer nroOrden;

    @ManyToOne
    private Usuario proveedor;

    @ManyToOne
    private Usuario cliente;

    @ManyToOne
    private Servicio servicio;

    private String descripcion;

    @Column(name = "imagen")
    @Lob
    private byte[] imagen;

    @Column(name = "video")
    @Lob
    private byte[] video;

    private Integer presupuesto;

    private String comentarioPresupuesto;

    private String estado;

    private String estado2;
}
