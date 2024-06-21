package com.app.servicios.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.Rol;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.servicios.CalificacionServicios;
import com.app.servicios.servicios.UsuarioServicios;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminControlador {
    @Autowired
    private UsuarioServicios usuarioServicios;

    @Autowired
    private CalificacionServicios calificacionServicios;

    @GetMapping("/usuarios")
    public String listarUsuarios(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicios.listarTodos();
        modelo.put("usuarios", usuarios);
        return "tablaUsuarios";
    }

    @GetMapping("/usuarios/clientes")
    public String listarClientes(ModelMap modelo) {
        List<Usuario> clientes = usuarioServicios.listarClientes();
        modelo.put("usuarios", clientes);
        return "tablaUsuarios";
    }

    @GetMapping("/usuarios/proveedores")
    public String listarProveedores(ModelMap modelo) {
        List<Usuario> proveedores = usuarioServicios.listarProveedores();
        modelo.put("usuarios", proveedores);
        return "tablaUsuarios";
    }

    @GetMapping("/usuarios/clientes-proveedores")
    public String listarClientesProveedores(ModelMap modelo) {
        List<Usuario> clientesProveedores = usuarioServicios.listarClientesProveedores();
        modelo.put("usuarios", clientesProveedores);
        return "tablaUsuarios";
    }

    @PutMapping("/usuarios/{id}/activar")
    public String activarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.activarUsuario(id);
            return "redirect:/superadmin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/usuarios";
        }
    }

    @PutMapping("/usuarios/{id}/desactivar")
    public String desactivarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.desactivarUsuario(id);
            return "redirect:/superadmin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/usuarios";
        }
    }

    @PutMapping("/usuarios/{id}/rol")
    public String cambiarRol(@PathVariable String id, @RequestParam Rol rol, ModelMap modelo) {
        try {
            Usuario usuario = usuarioServicios.buscarUsuario(id);
            // verifica que el rol actual del usuario sea cliente si se intenta cambiar a
            // admin
            if (rol == Rol.ADMIN && usuario.getRol() != Rol.CLIENTE) {
                modelo.put("error", "No se puede asignar el rol " + rol + " a este usuario.");
                return "redirect:/superadmin/usuarios";
            }
            usuario.setRol(rol);
            //Corregir para que el metodo envie los parametros correspondientes//
            //usuarioServicios.modificarUsuario(usuario);//
            return "redirect:/superadmin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/usuarios";
        }
    }

    @PutMapping("/calificaciones/{id}/activar")
    public String activarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.activarCalificacion(id);
            return "redirect:/superadmin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/calificaciones";
        }
    }

    @PutMapping("/calificaciones/{id}/desactivar")
    public String desactivarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.desactivarCalificacion(id);
            return "redirect:/superadmin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/calificaciones";
        }
    }

    @GetMapping("/calificaciones/modificar/{id}")
    public String mostrarFormularioModificarCalificacion(@PathVariable String id, ModelMap modelo) {
        Calificacion calificacion = calificacionServicios.buscarCalificacionPorId(id);
        modelo.put("calificacion", calificacion);
        return "modificarCalificacion";
    }

    @PostMapping("/calificaciones/modificar/{id}")
    public String modificarCalificacion(@PathVariable String id, @RequestParam Integer puntaje,
            @RequestParam String comentario, @RequestParam boolean activo, ModelMap modelo) {
        try {
            calificacionServicios.modificarCalificacion(id, puntaje, comentario, activo);
            return "redirect:/superadmin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/superadmin/calificaciones/modificar/" + id;
        }
    }

}
