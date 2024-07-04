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

import com.app.servicios.entidades.Servicio;
import com.app.servicios.servicios.ServicioServicios;

@Controller@RequestMapping("/servicios")
public class ServicioControlador {

    @Autowired
    private ServicioServicios servicioServicios;


    //Controlador de lista de servicios//
    @GetMapping("/listar")
    public String listarServicios(ModelMap modelo) {

        List<Servicio> listaServicios = servicioServicios.listarServiciosTodos();
        modelo.put("servicios", listaServicios);
        return "tablaServicios";
}
//////////////////////////////////////////////////////////////////////////////////////////////////////

    //Controlador para formulario de registar servicio nuevo//
    @GetMapping("/nuevo")
    public String crearServicio(ModelMap modelo) {
        return "formularioServicio";
    }

    @PostMapping("/guardar")
    public String guardarServicio(@RequestParam String nombre, ModelMap modelo) throws Exception {

        try {
            servicioServicios.crearServicio(nombre);
            return "redirect:/admin/panel";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/servicios/nuevo";
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //Controlador para editar servicio//
    @GetMapping("/modificar/{id}")
    public String editarServicio(@PathVariable String id, ModelMap modelo) {
        Servicio servicio = servicioServicios.buscarServicioPorId(id);
        modelo.put("servicio", servicio);
        return "modificarServicio";
    }

    @PostMapping("/modificar/{id}")
    public String modificarServicio(@PathVariable String id, @RequestParam String nombre, ModelMap modelo) throws Exception {
        try {
            servicioServicios.modificarServicio(id, nombre);
            return "redirect:/servicios/listar";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/servicios/modificar/" + id;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //Controlador para desactivar servicio//
    @GetMapping("/desactivar/{id}")
    public String desactivarServicio(@PathVariable String id, ModelMap modelo) throws Exception {
        try {
            servicioServicios.desactivarServicio(id);
            return "redirect:/servicios/listar";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/servicios/listar";
        }
    }

    @PostMapping("/desactivar/{id}")
    public String desactivarServicio(@PathVariable String id) throws Exception {
        try {
            servicioServicios.desactivarServicio(id);
            return "redirect:/servicios/listar";
        } catch (Exception ex) {
            return "redirect:/servicios/listar";
        }
    }
    
}

