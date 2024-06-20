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
import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.servicios.OrdenTrabajoServicios;

@Controller
@RequestMapping("/OrdenTrabajo")
public class OrdenTrabajoControlador {

     @Autowired
    private OrdenTrabajoServicios ordenTrabajoServicios;

    // Método para listar todas las órdenes de trabajo
    @GetMapping("/listar")
    public String listarOrdenesTrabajo(ModelMap modelo) {
        List<OrdenTrabajo> listaOrdenesTrabajo = ordenTrabajoServicios.listarTodasOrdenesTrabajo();
        modelo.put("ordentrabajo", listaOrdenesTrabajo);
        return "tablaOrdenesTrabajo";
    } 

    // Método para mostrar el formulario de creación de una nueva orden de trabajo
    @GetMapping("/nueva")
    public String crearOrdenTrabajo(ModelMap modelo) {
        return "formularioOrdenTrabajo";
    }
       
    // Método para procesar la creación de una nueva orden de trabajo
    @PostMapping("/guardar")
    public String guardarOrdenTrabajo(@RequestParam Integer nroOrden, @RequestParam String descripcion, ModelMap modelo) {
        try {
            ordenTrabajoServicios.crearOrdenTrabajo(nroOrden, descripcion);
            return "redirect:/OrdenTrabajo/listar";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "formularioOrdenTrabajo";
        }
    }

    // Método para mostrar el formulario de edición de una orden de trabajo
@GetMapping("/modificar/{id}")
    public String editarOrdenTrabajo(@PathVariable String id, ModelMap modelo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoServicios.buscarOrdenTrabajoPorId(id);
        modelo.put("ordenTrabajo", ordenTrabajo);
        return "modificarOrdenTrabajo";

    }

    // Método para procesar la modificación de una orden de trabajo
    @PostMapping("/modificar/{id}")
    public String modificarOrdenTrabajo(@PathVariable String id, @RequestParam Integer nroOrden, @RequestParam String descripcion, ModelMap modelo) {
        try {
            ordenTrabajoServicios.modificarOrdenTrabajo(id, nroOrden, descripcion);
            return "redirect:/OrdenTrabajo/listar";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/OrdenTrabajo/modificar/" + id;
        }
    }
       
    // Método para cambiar el estado de una orden de trabajo
    @GetMapping("/cambiar-estado/{id}")
    public String cambiarEstadoOrdenTrabajo(@PathVariable String id, @RequestParam String nuevoEstado, ModelMap modelo) {
        try {
            ordenTrabajoServicios.cambiarEstadoOrdenTrabajo(id, nuevoEstado);
            return "redirect:/OrdenTrabajo/listar";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/OrdenTrabajo/listar";
        }
    }
    
}