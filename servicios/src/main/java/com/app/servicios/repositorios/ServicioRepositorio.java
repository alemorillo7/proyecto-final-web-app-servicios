package com.app.servicios.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.servicios.entidades.Servicio;

@Repository
public interface ServicioRepositorio extends JpaRepository<Servicio, String> {
    
    @Query("SELECT s FROM Servicio s WHERE s.nombre = :nombre")
    public Servicio buscarServicioPorNombre(@Param("nombre") String nombre);

    @Query("SELECT s FROM Servicio s WHERE s.activo = true")
    public List<Servicio> buscarServiciosActivos();

    @Query("SELECT s FROM Servicio s WHERE s.activo = false")
    public List<Servicio> buscarServiciosInactivos();

    @Query("SELECT s FROM Servicio s JOIN s.proveedores u WHERE u.id = :usuarioId")
    List<Servicio> buscarServiciosPorIdUsuario(@Param("usuarioId") String usuarioId);
}
