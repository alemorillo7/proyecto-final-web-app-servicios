package com.app.servicios.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.servicios.CalificacionServicios;
import com.app.servicios.servicios.ServicioServicios;
import com.app.servicios.servicios.UsuarioServicios;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminControlador {

    @Autowired
    private ServicioServicios servicioServicios;

    @Autowired
    private UsuarioServicios usuarioServicios;

    @Autowired
    private CalificacionServicios calificacionServicios;

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
        return "crearServicio";
    }

    // Crear servicio
    @PostMapping("/servicios/nuevo")
    public String crearServicio(@RequestParam String nombre, ModelMap modelo) {
        try {
            servicioServicios.crearServicio(nombre);
            return "redirect:/superadmin/servicios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "crearServicio";
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
            return "redirect:/superadmin/servicios";
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
            return "redirect:/superadmin/servicios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/servicios";
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
            return "redirect:/superadmin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/usuarios";
        }
    }

    // Desactivar usuario
    @PostMapping("/usuarios/{id}/desactivar")
    public String desactivarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.desactivarUsuario(id);
            return "redirect:/superadmin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/usuarios";
        }
    }

    // Convertir cliente a admin
    @PostMapping("/usuarios/{id}")
    public String convertirClienteAAdmin(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.convertirClienteAAdmin(id);
            return "redirect:/admin/panel";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/panel";
        }
    }

    // Activar calificación
    @PostMapping("/calificaciones/{id}/activar")
    public String activarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.activarCalificacion(id);
            return "redirect:/superadmin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/calificaciones";
        }
    }

    // Desactivar calificación
    @PostMapping("/calificaciones/{id}/desactivar")
    public String desactivarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.desactivarCalificacion(id);
            return "redirect:/superadmin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/calificaciones";
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
    public String modificarCalificacion(@PathVariable String id, @RequestParam Integer puntaje,
            @RequestParam String comentario, @RequestParam boolean activo, ModelMap modelo) {
        try {
            calificacionServicios.modificarCalificacion(id, comentario);
            return "redirect:/superadmin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/calificaciones/modificar/" + id;
        }
    }

}
