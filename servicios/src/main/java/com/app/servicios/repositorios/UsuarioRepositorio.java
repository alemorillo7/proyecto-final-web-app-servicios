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
<<<<<<< HEAD

    //definir metodo donde se obtenga los UsuariosProveedores que esten listados en un servicio especifico.
    @Query("SELECT u FROM Usuario u JOIN u.servicio s WHERE s.id= :id")
    List<Usuario> buscarProveedorPorIdServicio(@Param ("id")String id);
    
=======
>>>>>>> 83e35a1b1e3567aa4f28c0f5b50ccdf4598d6448
}
