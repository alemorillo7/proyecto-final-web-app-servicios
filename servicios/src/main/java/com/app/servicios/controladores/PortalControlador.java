package com.app.servicios.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.Rol;
import com.app.servicios.repositorios.ServicioRepositorio;
import com.app.servicios.repositorios.UsuarioRepositorio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ServicioRepositorio servicioRepositorio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registrarProveedor")
    public String resgistrarProveedor(ModelMap modelo) {
        List<Servicio> servicios = servicioRepositorio.findAll();
        modelo.addAttribute("servicios", servicios);
        return "registroProveedor.html";
    }

    @PostMapping("/registroProveedor")
    public String registroProveedor(@RequestParam("email") String email,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam Integer dni,
            @RequestParam String direccion,
            @RequestParam String localidad,
            @RequestParam String barrio,
            @RequestParam List<String> serviciosIds,
            @RequestParam String descripcion,
            @RequestParam Integer telefono,
            @RequestParam String imagen,
            @RequestParam Integer experiencia,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo) {

        try {
            usuarioServicio.registrarProveedor(email, nombre, apellido, dni, direccion, localidad, barrio, serviciosIds,
                    descripcion, telefono, imagen, experiencia, password, password2);
            modelo.put("exito", "Se ha registrado como proveedor correctamente");
            return "index.html:/";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("direccion", direccion);
            modelo.put("localidad", localidad);
            modelo.put("barrio", barrio);
            modelo.put("serviciosIds", serviciosIds);
            modelo.put("descripcion", descripcion);
            modelo.put("telefono", telefono);
            modelo.put("imagen", imagen);
            modelo.put("experiencia", experiencia);
            return "registroProveedor.html";
        }
        // Usuario proveedor = new Usuario();
        // proveedor.setEmail(email);
        // proveedor.setNombre(nombre);
        // proveedor.setApellido(apellido);
        // proveedor.setDni(dni);
        // proveedor.setDireccion(direccion);
        // proveedor.setLocalidad(localidad);
        // proveedor.setBarrio(barrio);
        // proveedor.setDescripcion(descripcion);
        // proveedor.setTelefono(telefono);
        // proveedor.setImagen(imagen);
        // proveedor.setExperiencia(experiencia);
        // proveedor.setRol(Rol.PROVEEDOR);
        // proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        // Set<Servicio> servicios = new HashSet<>();
        // for (String servicioId : serviciosIds) {
        // Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
        // if (servicio != null) {
        // servicios.add(servicio);
        // }
        // }
        // proveedor.setServicios(servicios);
        // usuarioRepositorio.save(proveedor);
        return "index.html:/";
    }

    @GetMapping("/RegistrarCliente")
    public String registrarCliente() {
        return "registroCliente.html";

    }

    @PostMapping("/RegistroCliente")
    public String registroCliente(@RequestParam String email,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String direccion,
            @RequestParam String localidad,
            @RequestParam String barrio,
            @RequestParam Integer telefono,
            @RequestParam String imagen,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo) {

        try {
            usuarioServicio.registrarCliente(email, nombre, apellido, direccion, localidad, barrio, telefono, imagen,
                    password, password2);
            modelo.put("exito", "Se ha registrado como cliente correctamente");
            return "index.html:/";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("direccion", direccion);
            modelo.put("localidad", localidad);
            modelo.put("barrio", barrio);
            modelo.put("telefono", telefono);
            modelo.put("imagen", imagen);
            return "registroCliente.html";
        }

        // Usuario cliente = new Usuario();
        // cliente.setEmail(email);
        // cliente.setNombre(nombre);
        // cliente.setApellido(apellido);
        // cliente.setDireccion(direccion);
        // cliente.setLocalidad(localidad);
        // cliente.setBarrio(barrio);
        // cliente.setTelefono(telefono);
        // cliente.setImagen(imagen);
        // cliente.setRol(Rol.USUARIO);
        // cliente.setPassword(new BCryptPasswordEncoder().encode(password));
        // usuarioRepositorio.save(cliente);

    }

    @GetMapping("/RegistrarClienteProveedor")
    public String registrarClienteProveedor(ModelMap modelo) {
        List<Servicio> servicios = servicioRepositorio.findAll();
        modelo.addAttribute("servicios", servicios);
        return "registroClienteProveedor.html";
    }

    @PostMapping("/RegistroClienteProveedor")
    public String registroClienteProveedor(@RequestParam Integer dni,
            @RequestParam List<String> serviciosIds,
            @RequestParam String descripcion,
            @RequestParam Integer experiencia,
            ModelMap modelo) {
        try {
            usuarioServicio.registrarClienteProveedor(dni, serviciosIds, descripcion, experiencia);
            modelo.put("exito", "Perfil actualizado a proveedor");
            return "index.html:/";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("dni", dni);
            modelo.put("serviciosIds", serviciosIds);
            modelo.put("descripcion", descripcion);
            modelo.put("experiencia", experiencia);
            return "registroClienteProveedor.html";
        }

        // Usuario clienteProveedor = new Usuario();
        // clienteProveedor.setDni(dni);
        // clienteProveedor.setDescripcion(descripcion);
        // clienteProveedor.equals(clienteProveedor);

        // Set<Servicio> servicios = new HashSet<>();
        // for (String servicioId : serviciosIds) {
        // Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
        // if (servicio != null) {
        // servicios.add(servicio);
        // }
        // }
        // clienteProveedor.setServicios(servicios);
        // usuarioRepositorio.save(clienteComoProveedor);
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña inválidos!");
        }
        return "login.html";
    }
    @PreAuthorize ("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_USUARIOPROVEEDOR', 'ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html:";
}