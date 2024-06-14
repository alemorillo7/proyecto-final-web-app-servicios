package com.app.servicios.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.Rol;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.UsuarioRepositorio;

;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrarCliente(String nombre, String apellido, long id, String direccion, String localidad, String barrio, Integer telefono, String email, String password, String password2) throws MiExcepcion {
        validar(nombre,apellido,direccion,localidad,barrio,telefono,id, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setDireccion(direccion);
        usuario.setLocalidad(localidad);
        usuario.setBarrio(barrio);
        usuario.setTelefono(telefono);
        
        // Se le da el rol de user de manera predeterminada
        usuario.setRol(Rol.USUARIO);

        usuarioRepositorio.save(usuario);
    }

    private void validarAtributosCliente(String nombre, String apellido, long id, String direccion, String localidad, String barrio, Integer telefono, String email, String password, String password2) throws MiExcepcion {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiExcepcion("El nombre no puede ser nulo o estar vacío");

        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiExcepcion("El apellido no puede ser nulo o estar vacío");

        }
        if (telefono == null) {
            throw new MiExcepcion("El teléfono no puede ser nulo o estar vacío");

        }
        if (telefono <= 0 ) {
            throw new MiExcepcion("El teléfono no puede ser nulo o estar vacío");

        }
        // Validación que el telefono cumpla con la longitud:
        String telefonoStr = String.valueOf(telefono);
        if (telefonoStr.length()<7 || telefonoStr.length()>10 ) { //El telefono fijo debe tener 7 digitos, y un número de celular contiene 10
            throw new MiExcepcion("El teléfono debe contener entre 7 y 10 dígitos");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("El email no puede ser nulo o estar vacío");

        }
        if (direccion.isEmpty() || direccion == null) {
            throw new MiExcepcion("La dirección no puede ser nula o estar vacía");

        }
        if (localidad.isEmpty() || localidad == null) {
            throw new MiExcepcion("La localidad no puede ser nula o estar vacía");

        }
        if (barrio.isEmpty() || barrio == null) {
            throw new MiExcepcion("El barrio no puede ser nulo o estar vacío");

        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExcepcion("La contraseña no puede estar vacía, y debe tener más de 5 digitos");

        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas ingresadas deben ser iguales");

        }

    }

    @Transactional
    public void registrarProveedor(String nombre, String apellido, long id, String direccion, String localidad, Set<Servicio> servicios, String barrio, Integer telefono, String email, Integer dni, Integer experiencia, String password, String password2, String descripcion) throws MiExcepcion {
        validar(nombre,apellido,direccion,localidad,barrio,telefono,id, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setDireccion(direccion);
        usuario.setLocalidad(localidad);
        usuario.setBarrio(barrio);
        usuario.setTelefono(telefono);
        usuario.setExperiencia(experiencia);
        usuario.setDni(dni);
        usuario.setDescripcion(descripcion);
        usuario.setServicios(servicios);

        // Se le da el rol de user de manera predeterminada
        usuario.setRol(Rol.PROVEEDOR);

        usuarioRepositorio.save(usuario);
    }

    private void validarAtributosProveedor(String nombre, String apellido, long id, String direccion, String localidad, Set<Servicio> servicios, String barrio, Integer telefono, String email, Integer dni, Integer experiencia, String password, String password2, String descripcion) throws MiExcepcion {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiExcepcion("El nombre no puede ser nulo o estar vacío");

        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiExcepcion("El apellido no puede ser nulo o estar vacío");

        }
        if (telefono == null) {
            throw new MiExcepcion("El teléfono no puede ser nulo o estar vacío");

        }
        if (telefono <= 0 ) {
            throw new MiExcepcion("El teléfono no puede ser nulo o estar vacío");

        }
        // Validación que el telefono cumpla con la longitud:
        String telefonoStr = String.valueOf(telefono);
        if (telefonoStr.length()<7 || telefonoStr.length()>10 ) { //El telefono fijo debe tener 7 digitos, y un número de celular contiene 10
            throw new MiExcepcion("El teléfono debe contener entre 7 y 10 dígitos");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("El email no puede ser nulo o estar vacío");

        }
        if (direccion.isEmpty() || direccion == null) {
            throw new MiExcepcion("La dirección no puede ser nula o estar vacía");

        }
        if (localidad.isEmpty() || localidad == null) {
            throw new MiExcepcion("La localidad no puede ser nula o estar vacía");

        }
        if (barrio.isEmpty() || barrio == null) {
            throw new MiExcepcion("El barrio no puede ser nulo o estar vacío");

        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExcepcion("La contraseña no puede estar vacía, y debe tener más de 5 digitos");

        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas ingresadas deben ser iguales");

        }
        // Método para validar los atributos específicos según el rol proveedor
        //DNI:
        if (dni == null) {
            throw new MiExcepcion("Dato requerido a proveedores");

        }
        if (dni <= 0 ) {
            throw new MiExcepcion("El teléfono no puede ser nulo o estar vacío");

        }
        // Validación que el telefono cumpla con la longitud:
        String dniStr = String.valueOf(dni);
        if (dniStr.length()<7 || dniStr.length()>8 ) { 
            throw new MiExcepcion("El dni debe contener entre 7 y 8 dígitos");
        }

        //Experiencia:

        if (experiencia == null) {
            throw new MiExcepcion("Dato requerido a proveedores");

        }
        if (experiencia <= 0 ) {
            throw new MiExcepcion("Los Años de experiencia deben ser un número positivo mayor de cero");
       
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiExcepcion("Dato requerido a proveedores");

        }
    }

    // Nombre de usuario (por mail) para Autenticacion
    /**
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // buscar usuario de nuestro dominio y transformarlo en usuario del deminio del spring

        // buscar usuario segun el mail:
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email); //metodo creado en repositorio con este nombre

        if (usuario == null) {
            // crear lista de permisos
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p); // ROLE_USER

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

}