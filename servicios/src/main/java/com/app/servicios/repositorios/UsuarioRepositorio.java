package com.app.servicios.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.servicios.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
        @Query("SELECT u FROM Usuario u WHERE u.email = :email")
        public Usuario buscarPorEmail(@Param("email") String email);

        @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND LOWER(u.apellido) = LOWER(:apellido)")
        List<Usuario> buscarPorNombreYApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

        @Query("SELECT u FROM Usuario u WHERE u.servicio = :servicio")
        List<Usuario> buscarPorServicio(@Param("servicio") String servicio);

        @Modifying
        @Transactional
        @Query("UPDATE Usuario u SET u.validado = true WHERE u.id = :id AND " +
                        "u.nombre IS NOT NULL AND u.apellido IS NOT NULL AND u.direccion IS NOT NULL AND " +
                        "u.localidad IS NOT NULL AND u.barrio IS NOT NULL AND u.telefono IS NOT NULL AND " +
                        "u.email IS NOT NULL AND u.password IS NOT NULL AND " +
                        "LENGTH(u.password) > 5 AND u.password = :password2")
        void validarCliente(@Param("email") String email, @Param("password") String password,
                        @Param("password2") String password2);

        @Modifying
        @Transactional
        @Query("UPDATE Usuario u SET u.validado = true WHERE u.id = :id AND " +
                        "u.nombre IS NOT NULL AND u.apellido IS NOT NULL AND u.direccion IS NOT NULL AND " +
                        "u.localidad IS NOT NULL AND u.servicios IS NOT EMPTY AND u.barrio IS NOT NULL AND " +
                        "u.telefono IS NOT NULL AND u.email IS NOT NULL AND u.password IS NOT NULL AND " +
                        "LENGTH(u.password) > 5 AND u.password = :password2 AND " +
                        "u.dni IS NOT NULL AND u.experiencia IS NOT NULL AND u.experiencia > 0 AND " +
                        "u.descripcion IS NOT NULL")
        void validarProveedor(@Param("email") String email, @Param("password") String password,
                        @Param("password2") String password2);

}
