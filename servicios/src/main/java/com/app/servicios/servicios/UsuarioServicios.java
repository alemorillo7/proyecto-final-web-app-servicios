package com.app.servicios.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.Imagen;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.Rol;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.CalificacionRepositorio;
import com.app.servicios.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioServicios implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ImagenServicios imagenServicios;
    @Autowired
    private CalificacionRepositorio calificacionRepositorio;

    // Crear Clientes y Proveedores//
    @Transactional
    public void crearCliente(String nombre, String apellido, Integer dni,
            String localidad, String direccion,
            String barrio,
            String telefono, String email, String password,
            String password2,
            MultipartFile archivo) throws MiExcepcion {

        validarCliente(nombre, apellido, dni, localidad, direccion, barrio, telefono, email, password, password2);

        Usuario cliente = new Usuario();

        cliente.setNombre(nombre);

        cliente.setApellido(apellido);

        cliente.setDni(dni);

        cliente.setLocalidad(localidad);

        cliente.setDireccion(direccion);

        cliente.setBarrio(barrio);

        cliente.setTelefono(telefono);

        cliente.setEmail(email);

        cliente.setPassword(new BCryptPasswordEncoder().encode(password));

        cliente.setRol(Rol.CLIENTE);

        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = imagenServicios.guardarImagen(archivo);
                cliente.setImagen(imagen);
            } catch (MiExcepcion e) {
                throw new MiExcepcion("Error al guardar la imagen: " + e.getMessage());
            }
        } else {
            throw new MiExcepcion("El archivo no puede estar nulo o vacío");
        }


        cliente.setEstado(true);

        usuarioRepositorio.save(cliente);
    }

    public void crearProveedor(String nombre, String apellido, Integer dni,
            String localidad, String direccion,
            String telefono, String email, String password,
            String password2,
            // MultipartFile archivo,
            Integer experiencia, String descripcion,
            Set<Servicio> servicios) throws MiExcepcion {

        validarProveedor(nombre, apellido, dni, localidad, direccion,
                telefono, email, password, password2,
                experiencia, descripcion, servicios);

        Usuario proveedor = new Usuario();

        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setDni(dni);
        proveedor.setLocalidad(localidad);
        proveedor.setDireccion(direccion);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);
        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        proveedor.setRol(Rol.PROVEEDOR);
        // Imagen imagen = imagenServicios.guardarImagen(archivo);
        // proveedor.setImagen(imagen);
        proveedor.setExperiencia(experiencia);
        proveedor.setDescripcion(descripcion);
        proveedor.setServicios(servicios);
        proveedor.setEstado(true);

        usuarioRepositorio.save(proveedor);
    }

    // Modificar Cliente y Proveedor//

    @Transactional
    public void modificarCliente(String nombre, String apellido, Integer dni,
            String localidad, String direccion,
            String barrio, String telefono, String email,
            String password, String password2,
            // MultipartFile archivo,
            String id) throws MiExcepcion {

        validarCliente(nombre, apellido, dni, localidad, direccion, barrio, telefono, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setLocalidad(localidad);
            cliente.setDireccion(direccion);
            cliente.setBarrio(barrio);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);
            cliente.setPassword(new BCryptPasswordEncoder().encode(password));

            // String idImagen = null;

            // if(cliente.getImagen() != null){
            // idImagen = cliente.getImagen().getId();

            // }

            // Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);

            // cliente.setImagen(imagen);

            usuarioRepositorio.save(cliente);
        }
    }

    @Transactional
    public void modificarProveedor(String nombre, String apellido, Integer dni,
            String localidad, String direccion,
            String telefono, String email,
            String password, String password2,
            // MultipartFile archivo,
            Integer experiencia, String descripcion,
            Set<Servicio> servicios, String id)
            throws MiExcepcion {

        validarProveedor(nombre, apellido, dni, localidad,
                direccion, telefono, email, password, password2,
                experiencia, descripcion, servicios);

        Usuario proveedor = usuarioRepositorio.findById(id).orElse(null);
        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setDni(dni);
        proveedor.setLocalidad(localidad);
        proveedor.setDireccion(direccion);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);
        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));

        // String idImagen = null;

        // if(proveedor.getImagen() != null){
        // idImagen = proveedor.getImagen().getId();

        // }

        // Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);

        // proveedor.setImagen(imagen);

        proveedor.setExperiencia(experiencia);
        proveedor.setDescripcion(descripcion);
        proveedor.setServicios(servicios);

        usuarioRepositorio.save(proveedor);
    }

    @Transactional
    public void crearClienteProveedor(Integer experiencia, String descripcion,
            Set<Servicio> servicios,
            String id) throws MiExcepcion {

        validarClienteProveedor(experiencia, descripcion, servicios);

        Usuario clienteProveedor = usuarioRepositorio.findById(id).orElse(null);
        clienteProveedor.setExperiencia(experiencia);
        clienteProveedor.setDescripcion(descripcion);
        clienteProveedor.setServicios(servicios);
        clienteProveedor.setRol(Rol.CLIENTEPROVEEDOR);

        usuarioRepositorio.save(clienteProveedor);
    }

    @Transactional
    public void modificarClienteProveedor(String nombre, String apellido,
            Integer dni, String localidad,
            String direccion, String barrio,
            String telefono, String email,
            String password, String password2,
            Integer experiencia,
            String descripcion,
            Set<Servicio> servicios, String id)
            throws MiExcepcion {

        validarClienteProveedor(nombre, apellido, dni, localidad, direccion, barrio, telefono, email, password,
                password2, experiencia, descripcion, servicios);

        Usuario clienteProveedor = usuarioRepositorio.findById(id).orElse(null);
        clienteProveedor.setNombre(nombre);
        clienteProveedor.setApellido(apellido);
        clienteProveedor.setDni(dni);
        clienteProveedor.setLocalidad(localidad);
        clienteProveedor.setDireccion(direccion);
        clienteProveedor.setTelefono(telefono);
        clienteProveedor.setEmail(email);
        clienteProveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        clienteProveedor.setExperiencia(experiencia);
        clienteProveedor.setDescripcion(descripcion);
        clienteProveedor.setServicios(servicios);

        usuarioRepositorio.save(clienteProveedor);
    }
// obtener promedio
public String obtenerPromedioCalificaciones(Usuario proveedor) throws MiExcepcion {
        List<Calificacion> calificaciones = calificacionRepositorio.buscarCalificacionesPorProveedor(proveedor); 
        String mostrarPromedio;
        if (calificaciones.isEmpty()) {
            mostrarPromedio = "El profesional aun no ha recibido calificaciones";
        } else{
        double sumaPuntajes = calificaciones.stream()
                                            .mapToInt(Calificacion::getPuntaje)
                                            .sum();

        double apromediar = sumaPuntajes / calificaciones.size();

        Integer promedio = (int) Math.round(apromediar);
        mostrarPromedio = promedio.toString();
        }
        return mostrarPromedio;
    
    }
    // Contar calificaciones
    public int contarCalificaciones(Usuario proveedor) {
        Integer cantidadCalificaciones =calificacionRepositorio.buscarCalificacionesPorProveedor(proveedor).size();
        return cantidadCalificaciones;
    }

    @Transactional
    public void actualizarImagenUsuario(String usuarioId, MultipartFile archivo) throws MiExcepcion {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(usuarioId);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
                Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);
                usuario.setImagen(imagen);
            } else {
                Imagen imagen = imagenServicios.guardarImagen(archivo);
                usuario.setImagen(imagen);
            }

            usuarioRepositorio.save(usuario);
        }

    }

    // Listar Usuarios//
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        List<Usuario> clientes = usuarioRepositorio.buscarPorRol(Rol.CLIENTE);
        return clientes;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarProveedores() {
        List<Usuario> proveedores = usuarioRepositorio.buscarPorRol(Rol.PROVEEDOR);
        return proveedores;
    }
    @Transactional(readOnly = true)
    public List<Usuario> listarClienteProveedores(){
        List<Usuario> clienteproveedores = usuarioRepositorio.buscarPorRol(Rol.CLIENTEPROVEEDOR);
        return clienteproveedores;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarClientesProveedores() {
        List<Usuario> clientesProveedores = usuarioRepositorio.buscarPorRol(Rol.CLIENTEPROVEEDOR);
        return clientesProveedores;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPorServicio(String servicio) {

        List<Usuario> proveedores = usuarioRepositorio.buscarProveedoresPorIdServicio(servicio);

        return proveedores;

    }

    // Buscar usuario//

    @Transactional(readOnly = true)
    public Usuario buscarUsuario(String id) throws MiExcepcion {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if (usuario.isEmpty()) {
            throw new MiExcepcion("No existe el usuario");
        }
        return usuario.get();
    }

    // Activar o Desactivar Usuario//
    @Transactional
    public void desactivarUsuario(String id) throws MiExcepcion {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if (usuario.isEmpty()) {
            throw new MiExcepcion("No existe el usuario");
        }
        usuario.get().setEstado(false);
        usuarioRepositorio.save(usuario.get());
    }

    @Transactional
    public void activarUsuario(String id) throws MiExcepcion {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if (usuario.isEmpty()) {
            throw new MiExcepcion("No existe el usuario");
        }
        usuario.get().setEstado(true);
        usuarioRepositorio.save(usuario.get());
    }

    public void validarCliente(String nombre, String apellido, Integer dni,
            String localidad, String direccion, String barrio,
            String telefono, String email, String password, String password2)
            throws MiExcepcion {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiExcepcion("El nombre no puede estar vacio");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiExcepcion("El apellido no puede estar vacio");
        }
        if (dni == null) {
            throw new MiExcepcion("El dni no puede estar vacio");
        }
        if (localidad.isEmpty() || localidad == null) {
            throw new MiExcepcion("La localidad no puede estar vacia");
        }
        if (direccion.isEmpty() || direccion == null) {
            throw new MiExcepcion("La direccion no puede estar vacia");
        }
        if (barrio.isEmpty() || barrio == null) {
            throw new MiExcepcion("El barrio no puede estar vacio");
        }
        if (telefono.isEmpty() || telefono == null) {
            throw new MiExcepcion("El telefono no puede estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("El email no puede estar vacio");
        }
        if (password.isEmpty() || password == null) {
            throw new MiExcepcion("La contraseña no puede estar vacia");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas no coinciden");
        }
    }

    public void validarProveedor(String nombre, String apellido, Integer dni,
            String localidad, String direccion, String telefono, String email,
            String password, String password2, Integer experiencia, String descripcion,
            Set<Servicio> servicios) throws MiExcepcion {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiExcepcion("El nombre no puede estar vacio");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiExcepcion("El apellido no puede estar vacio");
        }
        if (dni == null) {
            throw new MiExcepcion("El dni no puede estar vacio");
        }
        if (localidad.isEmpty() || localidad == null) {
            throw new MiExcepcion("La localidad no puede estar vacia");
        }
        if (direccion.isEmpty() || direccion == null) {
            throw new MiExcepcion("La direccion no puede estar vacia");
        }
        if (telefono.isEmpty() || telefono == null) {
            throw new MiExcepcion("El telefono no puede estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("El email no puede estar vacio");
        }
        if (password.isEmpty() || password == null) {
            throw new MiExcepcion("La contraseña no puede estar vacia");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas no coinciden");
        }
        if (experiencia == null) {
            throw new MiExcepcion("La experiencia no puede estar vacia");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiExcepcion("La descripción no puede estar vacia");
        }
        if (servicios.isEmpty()) {
            throw new MiExcepcion("Los proveedores deben tener al menos un servicio seleccionado");
        }
    }

    // Este se usa para validar la modificacion
    public void validarClienteProveedor(String nombre, String apellido,
            Integer dni, String localidad, String direccion, String barrio,
            String telefono, String email, String password, String password2,
            Integer experiencia, String descripcion, Set<Servicio> servicios)
            throws MiExcepcion {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiExcepcion("El nombre no puede estar vacio");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MiExcepcion("El apellido no puede estar vacio");
        }
        if (dni == null) {
            throw new MiExcepcion("El dni no puede estar vacio");
        }
        if (localidad.isEmpty() || localidad == null) {
            throw new MiExcepcion("La localidad no puede estar vacia");
        }
        if (direccion.isEmpty() || direccion == null) {
            throw new MiExcepcion("La direccion no puede estar vacia");
        }
        if (barrio.isEmpty() || barrio == null) {
            throw new MiExcepcion("El barrio no puede estar vacio");
        }
        if (telefono.isEmpty() || telefono == null) {
            throw new MiExcepcion("El telefono no puede estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("El email no puede estar vacio");
        }
        if (password.isEmpty() || password == null) {
            throw new MiExcepcion("La contraseña no puede estar vacia");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas no coinciden");
        }
        if (experiencia == null) {
            throw new MiExcepcion("La experiencia no puede estar vacia");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiExcepcion("La descripción no puede estar vacia");
        }
        if (servicios.isEmpty()) {
            throw new MiExcepcion("Los proveedores deben tener al menos un servicio seleccionado");
        }
    }

    // Valida pase de cliente a proveedor, los datos nuevos exigidos
    public void validarClienteProveedor(Integer experiencia, String descripcion, Set<Servicio> servicios)
            throws MiExcepcion {

        if (experiencia == null) {
            throw new MiExcepcion("La experiencia no puede estar vacia");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiExcepcion("La descripción no puede estar vacia");
        }
        if (servicios.isEmpty()) {
            throw new MiExcepcion("Los proveedores deben tener al menos un servicio seleccionado");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            @SuppressWarnings({ "rawtypes", "unchecked" })
            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            return null;
        }
    }

    // Metodos para manejar la logica de filtrado y ordenamiento:

    /*
     * Corregir todo esto porque no funciona
     * public List<Usuario> obtenerListaProveedoresPorIdServicios(String id) {
     * return usuarioRepositorio.buscarProveedorPorIdServicio(id);//
     * }
     * 
     * @Autowired
     * private CalificacionRepositorio calificacionRepositorio;
     * 
     * //Metodo para obtener el promedio de las calificaciones
     * public Double obtenerPromedioCalificacion(Usuario proveedor) {
     * List<Calificacion> calificaciones =
     * calificacionRepositorio.buscarCalificacionesPorProveedoredor(proveedor);
     * if (calificaciones.isEmpty()) {
     * return 0.0;
     * }
     * int sumaPuntaje = 0;
     * for (Calificacion calificacion : calificaciones) {
     * sumaPuntaje += calificacion.getPuntaje();
     * }
     * return (double) sumaPuntaje / calificaciones.size();}
     * 
     * // ordenamiento:
     * public List<Usuario> obtenerProveedorPorFiltro(String id, String orden ){
     * List<Usuario> proveedores =
     * usuarioRepositorio.buscarProveedorPorIdServicio(id);
     * switch (orden.toLowerCase()) {
     * 
     * case "nombre": //caso 1 filtrar por nombre de forma descendente y entregar
     * una lista
     * return
     * proveedores.stream().sorted(Comparator.comparing(Usuario::getNombre).reversed
     * ()).collect(Collectors.toList());
     * 
     * 
     * case "calificacion"://caso 2 filtrar por el promedio obtenido de un metodo
     * que dispara el resultado
     * // entre la suma de todas las calificaciones de cada provedor entre la
     * cantidad de calidicaciones,
     * //entrega una lista
     * return proveedores.stream().sorted(Comparator.comparingDouble(this::
     * obtenerPromedioCalificacion).reversed()).collect(Collectors.toList());
     * 
     * 
     * 
     * case "Expeciencia": //caso 3 filtrar por los años de experiencia y devuelve
     * una lista de forma descendente
     * return proveedores.stream().sorted(Comparator.comparing(Usuario ::
     * getExperiencia).reversed()).collect(Collectors.toList());
     * 
     * 
     * default:
     * return proveedores;
     * }
     * 
     * }
     */

    public Usuario buscarPorEmail(String email) {
        return usuarioRepositorio.buscarPorEmail(email);
    }


    @Transactional
    public void convertirClienteAAdmin(String id) throws MiExcepcion {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (usuario.getRol() == Rol.CLIENTE) {
                usuario.setRol(Rol.ADMIN);
                usuarioRepositorio.save(usuario);
            } else {
                throw new MiExcepcion("El usuario no es un cliente.");
            }
        } else {
            throw new MiExcepcion("No existe el usuario");
        }
    }

  //Buscar cliente en base de datos para validar por mail que no exista y crear usuario
    public boolean existeClientePorEmail(String email) {
        return usuarioRepositorio.existsByEmail(email);
    }
    //Buscar cliente en base de datos para validar por dni que no exista y crear usuario
    public boolean existeClientePorDni(Integer dni) {
        return usuarioRepositorio.existsByDni(dni);
    }

    //Buscar proveedor en base de datos para validar por mail que no exista y crear usuario
    public boolean existeProveedorPorEmail(String email) {
        return usuarioRepositorio.existsByEmail(email);
    }
    //Buscar cliente en base de datos para validar por dni que no exista y crear usuario
    public boolean existeProveedorPorDni(Integer dni) {
        return usuarioRepositorio.existsByDni(dni);
    }


    @Transactional
    public void recuperarPass(String email, String password, String password2) throws MiExcepcion {

        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas no coinciden");
        }
        Optional<Usuario> usuarioOptional = Optional.ofNullable(usuarioRepositorio.buscarPorEmail(email));
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuarioRepositorio.saveAndFlush(usuario);
        } else {
            throw new MiExcepcion("No existe el usuario");
        }


    }

}