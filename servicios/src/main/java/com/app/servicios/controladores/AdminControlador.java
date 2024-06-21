package com.app.servicios.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.Rol;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.servicios.CalificacionServicios;
import com.app.servicios.servicios.UsuarioServicios;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
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

    @PutMapping("/usuarios/{id}/activar")
    public String activarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.activarUsuario(id);
            return "redirect:/admin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @PutMapping("/usuarios/{id}/desactivar")
    public String desactivarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.desactivarUsuario(id);
            return "redirect:/admin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @PutMapping("/usuarios/{id}/rol")
    public String cambiarRol(@PathVariable String id, @RequestParam Rol rol, ModelMap modelo) {
        try {
            Usuario usuario = usuarioServicios.buscarUsuario(id);
            // admin no puede cambiar a admin ni superAdmin
            if (rol == Rol.ADMIN || rol == Rol.SUPERADMIN) {
                modelo.put("error", "No se puede asignar el rol " + rol + " a este usuario.");
                return "redirect:/admin/usuarios";
            }
            usuario.setRol(rol);
            //Corregir para que el metodo envie los parametros correspondientes//
            //usuarioServicios.modificarUsuario(usuario);//
            return "redirect:/admin/usuarios";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/usuarios";
        }
    }

    @PutMapping("/calificaciones/{id}/activar")
    public String activarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.activarCalificacion(id);
            return "redirect:/admin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/calificaciones";
        }
    }

    @PutMapping("/calificaciones/{id}/desactivar")
    public String desactivarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.desactivarCalificacion(id);
            return "redirect:/admin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/calificaciones";
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
            return "redirect:/admin/calificaciones";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/admin/calificaciones/modificar/" + id;
        }
    }
}
