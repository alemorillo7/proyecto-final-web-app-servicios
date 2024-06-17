package com.app.servicios.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.servicios.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %:nombre%")
    public List<Usuario> buscarPorNombre(@Param("nombre") String nombre);

    
}
