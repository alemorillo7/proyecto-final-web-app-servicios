package com.app.servicios.controladores;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.repositorios.ServicioRepositorio;
import com.app.servicios.repositorios.UsuarioRepositorio;
import com.app.servicios.servicios.OrdenTrabajoServicios;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bandeja")
public class OrdenTrabajoControlador {

    @Autowired
    private OrdenTrabajoServicios ordenTrabajoServicios;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ServicioRepositorio servicioRepositorio;
    
    @GetMapping("/{nombre}")
    @PreAuthorize("hasRole('CLIENTE' , 'PROVEEDOR' , 'CLIENTEPROVEEDOR' , 'ADMIN')")
    public String mostrarBandeja(ModelMap modelo, HttpSession session) {


        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        

        List<OrdenTrabajo> ordenesAbiertoPresupuestar = ordenTrabajoServicios.buscarOrdenesAbiertoPresupuestar(logueado);
        List<OrdenTrabajo> ordenesAbiertoPresupuestado = ordenTrabajoServicios.buscarOrdenesAbiertoPresupuestado(logueado);
        List<OrdenTrabajo> ordenesAbiertoAceptado = ordenTrabajoServicios.buscarOrdenesAbiertoAceptado(logueado);
        List<OrdenTrabajo> ordenesFinalizadoCalificar = ordenTrabajoServicios.buscarOrdenesFinalizadoCalificar(logueado);
        List<OrdenTrabajo> ordenesFinalizadoCalificado = ordenTrabajoServicios.buscarOrdenesFinalizadoCalificado(logueado);
        List<OrdenTrabajo> ordenesCerradoTrabajoRechazado = ordenTrabajoServicios.buscarOrdenesCerradoTrabajoRechazado(logueado);
        List<OrdenTrabajo> ordenesCerradoPresupuestoRechazado = ordenTrabajoServicios.buscarOrdenesCerradoPresupuestoRechazado(logueado);
        List<OrdenTrabajo> ordenesCerradoCanceladoCliente = ordenTrabajoServicios.buscarOrdenesCerradoCanceladoCliente(logueado);
        List<OrdenTrabajo> ordenesCerradoCanceladoProveedor = ordenTrabajoServicios.buscarOrdenesCerradoCanceladoProveedor(logueado);

        modelo.addAttribute("ordenesAbiertoPresupuestar", ordenesAbiertoPresupuestar);
        modelo.addAttribute("ordenesAbiertoPresupuestado", ordenesAbiertoPresupuestado);
        modelo.addAttribute("ordenesAbiertoAceptado", ordenesAbiertoAceptado);
        modelo.addAttribute("ordenesFinalizadoCalificar", ordenesFinalizadoCalificar);
        modelo.addAttribute("ordenesFinalizadoCalificado", ordenesFinalizadoCalificado);
        modelo.addAttribute("ordenesCerradoTrabajoRechazado", ordenesCerradoTrabajoRechazado);
        modelo.addAttribute("ordenesCerradoPresupuestoRechazado", ordenesCerradoPresupuestoRechazado);
        modelo.addAttribute("ordenesCerradoCanceladoCliente", ordenesCerradoCanceladoCliente);
        modelo.addAttribute("ordenesCerradoCanceladoProveedor", ordenesCerradoCanceladoProveedor);

        List<Usuario> usuarios = usuarioRepositorio.findAll();
        modelo.addAttribute("usuarios", usuarios);

        List<Servicio> servicios = servicioRepositorio.findAll();
        modelo.addAttribute("servicios", servicios);


        return "bandeja-ordenes.html";
    }



    
}
