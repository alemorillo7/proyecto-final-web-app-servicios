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
import com.app.servicios.servicios.ServicioServicio;

@Controller
@RequestMapping("/servicios")
public class SercicioControlador {

    @Autowired
    private ServicioServicio servicioServicio;

    @GetMapping ("/listar")
    public String ListarServicio (Model model){
        model.addAttribute("servicios", servicioServicio.encontrarTodos());
        return "servicio";
    }

    @GetMapping("/{id}")
    public String obtenerServicios(@PathVariable Long id, Model model){
        Servicio servicio = servicioServicio.encontrarPorId(id);
        model.addAttribute("servicio", servicio);
        return "servicio";
    }

    @GetMapping("/nuevo")
    public String nuevoServicio(Model model){
        model.addAttribute("servicio", new Servicio());
        return "FormularioServicio";
    }

    @PostMapping ("/crearServicio")
    public String guardarServicio(@ModelAttribute Servicio servicio){
        servicioServicio.guardar(servicio);
        return "redirect:/servicio";
    }

    @GetMapping("/{id}/editar")
    public String editarServicio(@PathVariable Long id, Model model){
        Servicio servicio = servicioServicio.encontrarPorId(id);
        model.addAttribute("Servicio", servicio);
        return "FormularioServicio";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarServicio(@PathVariable Long id){
        servicioServicio.eliminar(id);
        return "redirect:/servicio";
    }

}
