package com.app.servicios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.servicios.entidades.Calificacion;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, String> {

    
}
