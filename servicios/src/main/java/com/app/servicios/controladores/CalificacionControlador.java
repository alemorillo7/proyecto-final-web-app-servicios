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
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.servicios.CalificacionServicios;

@Controller
@RequestMapping("/calificaciones")
public class CalificacionControlador {

    @Autowired
    private CalificacionServicios calificacionServicios;


    // Crear calificación
    @PostMapping("/nueva")
    public String crearCalificacion(@RequestParam Integer puntaje, @RequestParam String comentario,
                                    @RequestParam String clienteId, @RequestParam String proveedorId,
                                    @RequestParam String ordenTrabajoId, ModelMap modelo) {
        try {
            calificacionServicios.crearCalificacion(puntaje, comentario, clienteId, proveedorId, ordenTrabajoId);
            return "redirect:/bandeja/session.usuariosession.nombre";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/bandeja/session.usuariosession.nombre";
        }
    }


    // Modificar calificación
    @PostMapping("/modificar/{id}")
    public String modificarCalificacion(@PathVariable String id,
                                        @RequestParam String comentario, ModelMap modelo) {
        try {
            calificacionServicios.modificarCalificacion(id, comentario);
            return "redirect:/calificaciones/listar";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/calificaciones/modificar/" + id;
        }
    }

    // Denunciar Calificacion
    @PostMapping("/denunciar/{id}")
    public String denunciarCalificacion(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.denunciarCalificacion(id);
            return "redirect:/bandeja/session.usuariosession.nombre";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/calificaciones/denunciar/" + id;
        }
    }

    //Censurar Calificacion
    @PostMapping("/censurarComentario/{id}")
    public String censurarCalificacion(@PathVariable String id, 
                                        @RequestParam String comentario, 
                                        ModelMap modelo) {
        try {
            calificacionServicios.censurarCalificacion(id, comentario);
            return "redirect:/calificaciones/listar";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/calificaciones/censurar/" + id;
        }
    }

     // Eliminar comentario calificacion
    @PostMapping("/eliminarComentario/{id}")
    public String eliminarComentario(@PathVariable String id, ModelMap modelo) {
        try {
            calificacionServicios.eliminarComentarioCalificacion(id);
            return "redirect:/calificaciones/listar";
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "redirect:/calificaciones/eliminar/" + id;
        }
    }

    


    // Mostrar calificaciones por proveedor
    @GetMapping("/proveedor/{proveedorId}")
    public String listarCalificacionesPorProveedor(@PathVariable String proveedorId, ModelMap modelo) {
        List<Calificacion> calificaciones = calificacionServicios.buscarCalificacionesPorProveedor(proveedorId);
        modelo.put("calificaciones", calificaciones);
        return "tablaCalificaciones";
    }
    @GetMapping("proveedor/{proveedorId}/calificaciones")
    public String buscarCalificacionesPorProveedor(@PathVariable String proveedorId, ModelMap modelo) {
        List<Calificacion> calificaciones = calificacionServicios.buscarCalificacionesPorProveedor(proveedorId);
        modelo.put("calificaciones", calificaciones);
        return "bandeja-ordenes";
    }

    // Mostrar calificaciones por cliente
    @GetMapping("/cliente/{clienteId}")
    public String listarCalificacionesPorCliente(@PathVariable String clienteId, ModelMap modelo) {
        List<Calificacion> calificaciones = calificacionServicios.buscarCalificacionesPorCliente(clienteId);
        modelo.put("calificaciones", calificaciones);
        return "tablaCalificaciones";
    }
    
}
