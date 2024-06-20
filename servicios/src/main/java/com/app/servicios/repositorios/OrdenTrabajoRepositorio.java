package com.app.servicios.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.servicios.entidades.OrdenTrabajo;

@Repository
public interface OrdenTrabajoRepositorio extends JpaRepository<OrdenTrabajo, String> {
    
       // Buscar una orden de trabajo por su número de orden
    @Query("SELECT o FROM OrdenTrabajo o WHERE o.nroOrden = :nroOrden")
    public OrdenTrabajo buscarOrdenTrabajoPorNroOrden(@Param("nroOrden") Integer nroOrden);
        
    // Buscar órdenes de trabajo por estado
    @Query("SELECT o FROM OrdenTrabajo o WHERE o.estado = :estado")
    public List<OrdenTrabajo> buscarOrdenesTrabajoPorEstado(@Param("estado") String estado);

    // Buscar órdenes de trabajo por proveedor
    @Query("SELECT o FROM OrdenTrabajo o WHERE o.proveedor.id = :proveedorId")
    public List<OrdenTrabajo> buscarOrdenesTrabajoPorProveedor(@Param("proveedorId") String proveedorId);
        
    // Buscar órdenes de trabajo por cliente
    @Query("SELECT o FROM OrdenTrabajo o WHERE o.cliente.id = :clienteId")
    public List<OrdenTrabajo> buscarOrdenesTrabajoPorCliente(@Param("clienteId") String clienteId);

}
