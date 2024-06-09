package com.app.servicios.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.servicios.entidades.Servicio;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.servicios.ServicioServicio;

@Controller
@RequestMapping("/servicios")
public class SercicioControlador {

    @Autowired
    private ServicioServicio servicioServicio;

    @GetMapping
    public String ListarServicio (Model model){
        model.addAttribute("servicios", servicioServicio.listarServiciosTodos());
        return "servicio";
    }

    @GetMapping("/{id}")
    public String obtenerServicios(@PathVariable String id, Model model){
        Servicio servicio = servicioServicio.buscarServicio(id);
        model.addAttribute("servicio", servicio);
        return "servicio.html";
    }

    @GetMapping("/nuevo")
    public String nuevoServicio(Model model){
        model.addAttribute("servicio", new Servicio());
        return "formularioServicio";
    }

    @PostMapping
    public String guardarServicio(@ModelAttribute String nombre) throws MiExcepcion{
        servicioServicio.crearServicio(nombre);
        return "redirect:/servicio";
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
