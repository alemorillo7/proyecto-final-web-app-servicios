package com.app.servicios.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.Rol;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %:nombre%")
    public List<Usuario> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    public List<Usuario> buscarPorRol(@Param("rol") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email")String email);


    //definir metodo donde se obtenga los UsuariosProveedores que esten listados en un servicio especifico.
    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.servicios s WHERE s.nombre = :servicio")
    public List<Usuario> buscarPorServicio(@Param("servicio") String servicio);
    

    @Query("SELECT p FROM Usuario p JOIN p.servicios s WHERE s.id = :servicioId")
    List<Usuario> buscarProveedoresPorIdServicio(@Param("servicioId") String servicioId);

    boolean existsByEmail(String email);

    boolean existsByDni(Integer dni);

}

