package com.app.servicios.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.servicios.entidades.Servicio;
import com.app.servicios.servicios.ServicioServicio;

@Controller
@RequestMapping("/servicios")
public class ServicioControlador {

    @Autowired
    private ServicioServicio servicioServicio;

    @GetMapping ("/listar")
    public String ListarServicio (ModelMap modelo){
        List<Servicio> servicios = servicioServicio.listarServiciosTodos();
        modelo.put("servicios", servicios);
        return "tablaServicios";
    }

    @GetMapping("/{id}")
    public String obtenerServicios(@PathVariable String id, Model model){
        Servicio servicio = servicioServicio.buscarServicio(id);
        model.addAttribute("servicio", servicio);
        return "servicio.html";
    }

    @GetMapping("/nuevo")
    public String nuevoServicio(){
        
        return "formularioServicio";
    }

    @PostMapping("/guardar")
    public String guardarServicio(@RequestParam String nombre, ModelMap modelo){
        try {
            servicioServicio.crearServicio(nombre);
            return "redirect:/servicios/listar";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "formularioServicio";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarServicio(@PathVariable String id, Model model){
        Servicio servicio = servicioServicio.buscarServicio(id);
        model.addAttribute("Servicio", servicio);
        return "FormularioServicio";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarServicio(@PathVariable String id){
        servicioServicio.borrarServicio(id);
        return "redirect:/servicio";
    }

}
