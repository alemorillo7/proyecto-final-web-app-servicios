package com.app.servicios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.servicios.entidades.OrdenTrabajo;

@Repository
public interface OrdenTrabajoRepositorio extends JpaRepository<OrdenTrabajo, String> {
    
}
