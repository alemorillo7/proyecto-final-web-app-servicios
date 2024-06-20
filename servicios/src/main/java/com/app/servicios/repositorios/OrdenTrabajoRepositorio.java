package com.app.servicios.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.enumeraciones.EstadoOrden;
import com.app.servicios.enumeraciones.EstadoTrabajo;

@Repository
public interface OrdenTrabajoRepositorio extends JpaRepository<OrdenTrabajo, String> {
    
    @Query("SELECT o FROM OrdenTrabajo o WHERE o.cliente.id = :id")
    public List<OrdenTrabajo> buscarOrdenPorCliente(@Param("id") String id);
    @Query ("SELECT o FROM OrdenTrabajo o WHERE o.proveedor.id = :id")
    public List<OrdenTrabajo> buscarOrdenPorProveedor(@Param("id") String id);
    @Query ("SELECT o FROM OrdenTrabajo o WHERE o.servicios.id = :id")
    public List<OrdenTrabajo> buscarOrdenPorServicio(@Param("id") String id);
    @Query ("SELECT o FROM OrdenTrabajo o WHERE o.estadoOrden = :estadoOrden")
    public List<OrdenTrabajo> buscarPorEstadoOrden(@Param("estadoOrden") EstadoOrden estadoOrden);
    @Query ("SELECT o FROM OrdenTrabajo o WHERE o.estadoTrabajo = :estadoTrabajo")
    public List<OrdenTrabajo> buscarPorEstadoTrabajo(@Param("estadoTrabajo") EstadoTrabajo estadoTrabajo);
    @Query ("SELECT o FROM OrdenTrabajo o WHERE o.estado = :estado")
    public List<OrdenTrabajo> buscarPorEstado(@Param("estado") Boolean estado);
}
