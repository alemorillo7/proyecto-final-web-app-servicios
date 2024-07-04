package com.app.servicios.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.servicios.CalificacionServicios;
import com.app.servicios.servicios.ServicioServicios;
import com.app.servicios.servicios.UsuarioServicios;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private ServicioServicios servicioServicios;

    @Autowired
    private UsuarioServicios usuarioServicios;

    @Autowired
    private CalificacionServicios calificacionServicios;
// panel control
    @GetMapping("/panel")
    public String panelAdministrador(ModelMap modelo, HttpSession session) {

        List<Servicio> servicios = servicioServicios.listarServiciosTodos();
        modelo.addAttribute("servicios", servicios);
        List<Usuario> cliente = usuarioServicios.listarClientes();
        modelo.addAttribute("clientes", cliente);
        List<Usuario> proveedor = usuarioServicios.listarProveedores();
        modelo.addAttribute("proveedor", proveedor);
        List<Usuario> clienteProveedor = usuarioServicios.listarClienteProveedores();
        modelo.addAttribute("clienteproveedor", clienteProveedor);
        List<Calificacion> calificacionesDenunciadas = calificacionServicios.listarCalificacionesDenunciadas();
        modelo.addAttribute("denuncias", calificacionesDenunciadas);
        
        return "administrador.html";
    }
// censurar comentario
@PostMapping("/censurarComentario")
public String censurarComentario(@RequestParam("id") String id, @RequestParam("comentario") String comentario, ModelMap modelo) throws Exception {
    System.out.println("entre al controlador");
        calificacionServicios.censurarComentario(id, comentario);
        List<Calificacion> calificacionesDenunciadas = calificacionServicios.listarCalificacionesDenunciadas();
        modelo.addAttribute("denuncias", calificacionesDenunciadas);
    return "administrador.html";
}
    // Mostrar lista de servicios
    @GetMapping("/servicios")
    public String listarServicios(ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosTodos();
        modelo.put("servicios", servicios);
        return "tablaServicios";
    }

    // Mostrar formulario para crear servicio
    @GetMapping("/servicios/nuevo")
    public String mostrarFormularioCrearServicio() {
        return "formularioServicio";
    }

    // Crear servicio
    @PostMapping("/servicios/nuevo")
    public String crearServicio(@RequestParam String nombre, ModelMap modelo) {
        try {
            servicioServicios.crearServicio(nombre);
            return "redirect:/admin/servicios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "formularioServicio";
        }
    }

    // Mostrar formulario para modificar servicio
    @GetMapping("/servicios/modificar/{id}")
    public String mostrarFormularioModificarServicio(@PathVariable String id, ModelMap modelo) {
        Servicio servicio = servicioServicios.buscarServicioPorId(id);
        modelo.put("servicio", servicio);
        return "modificarServicio";
    }

    // Modificar servicio
    @PostMapping("/servicios/modificar/{id}")
    public String modificarServicio(@PathVariable String id, @RequestParam String nombreNuevo, ModelMap modelo) {
        try {
            servicioServicios.modificarServicio(id, nombreNuevo);
            return "redirect:/admin/servicios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "modificarServicio";
        }
    }

    // Desactivar servicio
    @PostMapping("/servicios/desactivar/{id}")
    public String desactivarServicio(@PathVariable String id, ModelMap modelo) {
        try {
            servicioServicios.desactivarServicio(id);
            return "redirect:/admin/servicios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/servicios";
        }
    }

    // Mostrar lista de usuarios
    @GetMapping("/usuarios")
    public String listarUsuarios(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicios.listarTodos();
        modelo.put("usuarios", usuarios);
        return "tablaUsuarios";
    }

    // Mostrar lista de clientes
    @GetMapping("/usuarios/clientes")
    public String listarClientes(ModelMap modelo) {
        List<Usuario> clientes = usuarioServicios.listarClientes();
        modelo.put("usuarios", clientes);
        return "tablaUsuarios";
    }

    // Mostrar lista de proveedores
    @GetMapping("/usuarios/proveedores")
    public String listarProveedores(ModelMap modelo) {
        List<Usuario> proveedores = usuarioServicios.listarProveedores();
        modelo.put("usuarios", proveedores);
        return "tablaUsuarios";
    }

    // Mostrar lista de clientes y proveedores
    @GetMapping("/usuarios/clientes-proveedores")
    public String listarClientesProveedores(ModelMap modelo) {
        List<Usuario> clientesProveedores = usuarioServicios.listarClientesProveedores();
        modelo.put("usuarios", clientesProveedores);
        return "tablaUsuarios";
    }

    // Activar usuario
    @PostMapping("/usuarios/{id}/activar")
    public String activarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.activarUsuario(id);
            return "redirect:/admin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    // Desactivar usuario
    @PostMapping("/usuarios/{id}/desactivar")
    public String desactivarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.desactivarUsuario(id);
            return "redirect:/admin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    // Activar calificación
    @PostMapping("/calificaciones/{id}/activar")
    public String activarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.activarCalificacion(id);
            return "redirect:/admin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/calificaciones";
        }
    }

    // Desactivar calificación
    @PostMapping("/calificaciones/{id}/desactivar")
    public String desactivarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.desactivarCalificacion(id);
            return "redirect:/admin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/calificaciones";
        }
    }

    // Mostrar formulario para modificar calificación
    @GetMapping("/calificaciones/modificar/{id}")
    public String mostrarFormularioModificarCalificacion(@PathVariable String id, ModelMap modelo) {
        Calificacion calificacion = calificacionServicios.buscarCalificacionPorId(id);
        modelo.put("calificacion", calificacion);
        return "modificarCalificacion";
    }

    // Modificar calificación
    @PostMapping("/calificaciones/modificar/{id}")
    public String modificarCalificacion(@PathVariable String id,
            @RequestParam String comentario, ModelMap modelo) {
        try {
            calificacionServicios.modificarCalificacion(id, comentario);
            return "redirect:/admin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/calificaciones/modificar/" + id;
        }
    }
}
