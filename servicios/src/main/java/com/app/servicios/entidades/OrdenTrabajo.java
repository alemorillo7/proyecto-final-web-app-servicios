package com.app.servicios.entidades;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.app.servicios.enumeraciones.EstadoOrden;
import com.app.servicios.enumeraciones.EstadoTrabajo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
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
    
    private Integer numeroDeOrden;

    private String titulo;

    @ManyToOne
    private Usuario proveedor;

    @ManyToOne
    private Usuario cliente;

    @ManyToMany
    @JoinTable (name = "ordenTrabajo_servicio", joinColumns = @JoinColumn(name = "ordenTrabajo_id"), inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private Set<Servicio> servicios = new HashSet<>();

    private String descripcion;

   
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imagen;

    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] video;

    private Integer presupuesto;

    private String comentarioPresupuesto;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoOrden; // ABIERTO, CERRADO, CALIFICAR, CALIFICADO

    @Enumerated(EnumType.STRING)
    private EstadoTrabajo estadoTrabajo; // PRESUPUESTAR, PRESUPUESTADO, ACEPTADO, PRESUPUESTO_RECHAZADO, TRABAJO_RECHAZADO, FINALIZADO, CANCELADO

    private Boolean estado;
}
