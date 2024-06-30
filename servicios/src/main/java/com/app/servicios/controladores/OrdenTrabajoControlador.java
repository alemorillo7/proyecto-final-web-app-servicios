package com.app.servicios.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bandeja")
public class OrdenTrabajoControlador {
    
    @GetMapping("/{nombre}")
    @PreAuthorize("hasRole('CLIENTE' , 'PROVEEDOR' , 'CLIENTEPROVEEDOR' , 'ADMIN')")
    public String mostrarBandeja(ModelMap modelo, HttpSession session) {

        




        return "bandeja-ordenes.html";
    }
}
