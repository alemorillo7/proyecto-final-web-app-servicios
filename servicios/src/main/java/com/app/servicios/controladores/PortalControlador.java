package com.app.servicios.controladores;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String formularioRegistroProveedor(ModelMap model) {
        List<Servicio> servicios = servicioRepositorio.findAll();
        model.addAttribute("servicios", servicios);
        return "registroProveedor.html";
    }

    @PostMapping("/registroProveedor")
    public String registroProveedor(@RequestParam("email") String email,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("dni") Integer dni,
            @RequestParam("direccion") String direccion,
            @RequestParam("localidad") String localidad,
            @RequestParam("barrio") String barrio,
            @RequestParam("servicios") List<String> serviciosIds,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("telefono") Integer telefono,
            @RequestParam("imagen") String imagen,
            @RequestParam("experiencia") Integer experiencia,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2) {
        if (!password.equals(password2)) {
            // Manejar el error de contraseñas no coincidentes
            return "redirect:/registroProveedor";
        }
        Usuario proveedor = new Usuario();
        proveedor.setEmail(email);
        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setDni(dni);
        proveedor.setDireccion(direccion);
        proveedor.setLocalidad(localidad);
        proveedor.setBarrio(barrio);
        proveedor.setDescripcion(descripcion);
        proveedor.setTelefono(telefono);
        proveedor.setImagen(imagen);
        proveedor.setExperiencia(experiencia);
        proveedor.setRol(Rol.PROVEEDOR);
        proveedor.setPassword(password);
        Set<Servicio> servicios = new HashSet<>();
        for (String servicioId : serviciosIds) {
            Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
            if (servicio != null) {
                servicios.add(servicio);
            }
        }
        proveedor.setServicios(servicios);

    }

    @GetMapping("/RegistrarCliente")
    public String formularioRegistroCliente(ModelMap model) {
        return "registroCliente.html";

    }

    @PostMapping("/RegistroCliente")
    public String registroCliente(@RequestParam("email") String email,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("direccion") String direccion,
            @RequestParam("localidad") String localidad,
            @RequestParam("barrio") String barrio,
            @RequestParam("telefono") Integer telefono,
            @RequestParam("imagen") String imagen,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2) {

        if (!password.equals(password2)) {
            // Manejar el error de contraseñas no coincidentes
            return "redirect:/registroCliente";
            Usuario cliente = new Usuario();
            cliente.setEmail(email);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDireccion(direccion);
            cliente.setLocalidad(localidad);
            cliente.setBarrio(barrio);
            cliente.setTelefono(telefono);
            cliente.setImagen(imagen);
            cliente.setRol(Rol.USUARIO);
            cliente.setPassword(password);
            usuarioRepositorio.save(cliente);
            return "redirect:/";
        }
    }

    @GetMapping("/RegistrarClienteProveedor")
    public String formularioRegistroUsuarioProveedor(ModelMap model) {
        List<Servicio> servicios = servicioRepositorio.findAll();
        model.addAttribute("servicios", servicios);
        return "registroClienteProveedor.html";
    }

    @PostMapping("/RegistroClienteProveedor")
    public String registroClienteProveedor(@RequestParam("dni") Integer dni,
            @RequestParam("servicios") List<String> serviciosIds,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("experiencia") Integer experiencia) {
        Usuario clienteProveedor = new Usuario();
        clienteProveedor.setDni(dni);
        clienteProveedor.setDescripcion(descripcion);
        clienteProveedor.equals(clienteProveedor);

        Set<Servicio> servicios = new HashSet<>();
        for (String servicioId : serviciosIds) {
            Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
            if (servicio != null) {
                servicios.add(servicio);
            }
        }
        clienteProveedor.setServicios(servicios);
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
}