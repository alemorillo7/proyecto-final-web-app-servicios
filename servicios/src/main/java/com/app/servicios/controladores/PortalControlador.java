package com.app.servicios.controladores;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.repositorios.ServicioRepositorio;
import com.app.servicios.servicios.ServicioServicios;
import com.app.servicios.servicios.UsuarioServicios;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private ServicioServicios servicioServicios;

    @Autowired 
    private UsuarioServicios usuarioServicios;

    @Autowired
    private ServicioRepositorio servicioRepositorio;

    @GetMapping("/")
    public String index(ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
        return "index.html";
    }

    @GetMapping("/registrarCliente")
    public String registrarCliente() {
        return "formularioCliente.html";
    }

    @PostMapping("/registroCliente")
    public String registrarClientePost(@RequestParam String nombre, 
                                       @RequestParam String apellido, 
                                       @RequestParam String localidad, 
                                       @RequestParam String direccion, 
                                       @RequestParam String barrio, 
                                       @RequestParam String telefono, 
                                       @RequestParam String email, 
                                       @RequestParam String password, 
                                       @RequestParam String password2, 
                                       ModelMap modelo, 
                                       @RequestParam byte[] imagen) throws Exception {
        try {
            usuarioServicios.crearCliente(nombre, apellido, direccion, localidad, barrio, telefono, email, password, password2, imagen);
            return "login.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/registrarCliente";
        }
    }

    @GetMapping("/registrarProveedor")
    public String registrarProveedor(ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
        return "formularioProveedor.html";
    }

    @PostMapping("/registroProveedor")
    public String registrarProveedorPost(@RequestParam String email,
                                         @RequestParam String nombre,
                                         @RequestParam String apellido,
                                         @RequestParam Integer dni,
                                         @RequestParam String direccion,
                                         @RequestParam String localidad,
                                         @RequestParam String barrio,
                                         @RequestParam List<String> serviciosIds,
                                         @RequestParam String descripcion,
                                         @RequestParam String telefono,
                                         @RequestParam byte[] imagen,
                                         @RequestParam Integer experiencia,
                                         @RequestParam String password,
                                         @RequestParam String password2,
                                         ModelMap modelo) {
        try {
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }
            usuarioServicios.crearProveedor(nombre, apellido, direccion, localidad, barrio, telefono, email, password, password2, imagen, dni, experiencia, descripcion, servicios);
            return "login.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/registrarProveedor";
        }
    }

    @GetMapping("/registarClienteProveedor/{id}")
    public String registrarClienteProveedor(@PathVariable String id, ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
        return "registrarClienteProveedor.html";
    }

    @PostMapping("/registroClienteProveedor/{id}")
    public String registrarClienteProveedorPost(@PathVariable String id,
                                                @RequestParam Integer dni, 
                                                @RequestParam Integer experiencia, 
                                                @RequestParam String descripcion, 
                                                @RequestParam List<String> serviciosIds, 
                                                ModelMap modelo) {
        try {
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }
            usuarioServicios.crearClienteProveedor(experiencia, descripcion, dni, servicios, id);
            return "inicio.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/registrarProveedor";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Email o contraseña incorrectos");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin";
        }
        return "inicio.html";
    }
}
