package com.app.servicios.repositorios;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.Usuario;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, String> {

    @Query("SELECT c FROM Calificacion c WHERE c.activo = true")
    public List<Calificacion> buscarCalificacionesActivas();

    @Query("SELECT c FROM Calificacion c WHERE c.activo = false")
    public List<Calificacion> buscarCalificacionesInactivas();

    
    @Query("SELECT c FROM Calificacion c WHERE c.proveedor = :proveedor")
    public List<Calificacion> buscarCalificacionesPorProveedor(@Param("proveedor")Usuario proveedor);

    @Query("SELECT c FROM Calificacion c WHERE c.cliente = :cliente")
    public List<Calificacion> buscarCalificacionesPorCliente(@Param("cliente")Usuario cliente);

    @Query("SELECT c FROM Calificacion c WHERE c.cliente = :cliente AND c.proveedor = :proveedor")
    public List<Calificacion> buscarCalificacionesPorClienteYProveedoredor(@Param("cliente")Usuario cliente, @Param("proveedor")Usuario proveedor);

    @Query("SELECT AVG(c.puntaje) FROM Calificacion c WHERE c.proveedor.id = :proveedorId")
    Double promedioCalificacionProveedor(@Param("proveedorId") String proveedorId);

    @Query("SELECT c FROM Calificacion c WHERE c.estadoCalificacion = DENUNCIADA")
    public List<Calificacion> listarCalificacionesDenunciadas();
   


}
