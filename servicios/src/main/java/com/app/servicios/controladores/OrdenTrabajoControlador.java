package com.app.servicios.controladores;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.ServicioRepositorio;
import com.app.servicios.repositorios.UsuarioRepositorio;
import com.app.servicios.servicios.CalificacionServicios;
import com.app.servicios.servicios.OrdenTrabajoServicios;
import com.app.servicios.servicios.UsuarioServicios;

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
    @Autowired
    private UsuarioServicios usuarioServicios;
    @Autowired
    private CalificacionServicios calificacionServicios;
//Crear Orden Trabajo

    @GetMapping("/crearOrdenTrabajo/{proveedorId}")
    @PreAuthorize("hasRole('CLIENTE' , 'CLIENTEPROVEEDOR', 'ADMIN')")
    public String crearOrdenTrabajo(ModelMap modelo, 
                                    HttpSession session,
                                    @PathVariable String proveedorId) {
        modelo.addAttribute("proveedorId", proveedorId);
        modelo.addAttribute("servicios", servicioRepositorio.buscarServiciosPorIdUsuario(proveedorId));
        return "crearOrdenTrabajo.html";
    }









    @PostMapping("/OrdenTrabajoCreada")
    @PreAuthorize("hasRole('CLIENTE' , 'CLIENTEPROVEEDOR' , 'ADMIN')")
    public String crearOrdenTrabajo(ModelMap modelo,
                                    HttpSession session,
                                    @RequestParam String titulo,
                                    @RequestParam String proveedorId,
                                    
                                    @RequestParam List<String> serviciosIds,
                                    @RequestParam String descripcion,
                                    MultipartFile archivo) throws MiExcepcion {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        String clienteId = logueado.getId();

        ordenTrabajoServicios.crearOrdenTrabajo(titulo, proveedorId, clienteId, serviciosIds, descripcion, archivo);
        
        return "redirect:/bandeja/session.usuariosession.nombre";//ver si funciona//
    }
//Trabajo Presupuestado
    @PostMapping("/ordenTrabajo/trabajoPresupuestado")
    @PreAuthorize("hasRole('PROVEEDOR' , 'CLIENTEPROVEEDOR')")
    public String presupuestado(ModelMap modelo, 
                                HttpSession session,
                                @RequestParam String ordenTrabajoId,
                                @RequestParam Integer presupuesto,
                                @RequestParam String comentarioPresupuesto) throws MiExcepcion {
                                    System.out.println("Entre al contolador");
        ordenTrabajoServicios.trabajoPresupuestadoOrdenTrabajo(ordenTrabajoId, presupuesto, comentarioPresupuesto);
        return "redirect:/bandeja/session.usuariosession.nombre";
    }
//Trabajo Rechazado
    @PostMapping("/ordenTrabajo/trabajoRechazado")
    @PreAuthorize("hasRole('PROVEEDOR' , 'CLIENTEPROVEEDOR')")
    public String rechazado(ModelMap modelo, 
                            HttpSession session,
                            @RequestParam String ordenTrabajoId,
                            @RequestParam String comentarioPresupuesto) throws MiExcepcion {
        ordenTrabajoServicios.trabajoRechazadoOrdenTrabajo(ordenTrabajoId, comentarioPresupuesto);
        return "redirect:/bandeja/session.usuariosession.nombre";
    }
//Presupuesto Aceptado
    @PostMapping("/ordenTrabajo/presupuestoAceptado")
    @PreAuthorize("hasRole('CLIENTE' , 'CLIENTEPROVEEDOR', 'ADMIN')")
    public String presupuestoAceptado(ModelMap modelo, 
                                      HttpSession session,
                                      @RequestParam String ordenTrabajoId) throws MiExcepcion {
        ordenTrabajoServicios.presupuestoAceptadoOrdenTrabajo(ordenTrabajoId);
        return "redirect:/bandeja/session.usuariosession.nombre";
    }
// Presupuesto Rechazado
    @PostMapping("/ordenTrabajo/presupuestoRechazado")
    @PreAuthorize("hasRole('CLIENTE' , 'CLIENTEPROVEEDOR', 'ADMIN')")
    public String presupuestoRechazado(ModelMap modelo, 
                                       HttpSession session,
                                       @RequestParam String ordenTrabajoId) throws MiExcepcion {
        ordenTrabajoServicios.presupuestoRechazadoOrdenTrabajo(ordenTrabajoId);
        return "redirect:/bandeja/session.usuariosession.nombre";
    }
// Trabajo Terminado
    @PostMapping("/ordenTrabajo/trabajoTerminado")
    @PreAuthorize("hasRole('PROVEEDOR' , 'CLIENTEPROVEEDOR')")
    public String trabajoTerminado(ModelMap modelo, 
                                   HttpSession session,
                                   @RequestParam String ordenTrabajoId) throws MiExcepcion {
        ordenTrabajoServicios.trabajoTerminadoOrdenTrabajo(ordenTrabajoId);
        return "redirect:/bandeja/session.usuariosession.nombre";
    }
// Cliente Cancela trabajo
    @PostMapping("/ordenTrabajo/clienteCancelaTrabajo")
    @PreAuthorize("hasRole('CLIENTE' , 'CLIENTEPROVEEDOR', 'ADMIN')")
    public String clienteCancelaTrabajo(ModelMap modelo, 
                                        HttpSession session,
                                        @RequestParam String ordenTrabajoId) throws MiExcepcion {
        ordenTrabajoServicios.clienteCancelaTrabajoOrdenTrabajo(ordenTrabajoId);
        return "redirect:/bandeja/session.usuariosession.nombre";
    }
// Proveedor Cancela trabajo
    @PostMapping("/ordenTrabajo/proveedorCancelaTrabajo")
    @PreAuthorize("hasRole('PROVEEDOR' , 'CLIENTEPROVEEDOR')")
    public String proveedorCancelaTrabajo(ModelMap modelo, 
                                          HttpSession session,
                                          @RequestParam String ordenTrabajoId) throws MiExcepcion {
        ordenTrabajoServicios.proveedorCancelaTrabajoOrdenTrabajo(ordenTrabajoId);
        return "redirect:/bandeja/session.usuariosession.nombre";
    }

//**************************************************************************
//ACA VA CALIFICAR, necesitamos calificacion
//**************************************************************************

    
    @GetMapping("/{nombre}")
    @PreAuthorize("hasRole('CLIENTE' , 'PROVEEDOR' , 'CLIENTEPROVEEDOR' , 'ADMIN')")
    public String mostrarBandeja(ModelMap modelo, HttpSession session) throws MiExcepcion {


        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        String mostrarPromedio = usuarioServicios.obtenerPromedioCalificaciones(logueado);
        modelo.addAttribute("promedioCalificacion", mostrarPromedio);
        List<OrdenTrabajo> ordenesAbiertoPresupuestar = ordenTrabajoServicios.buscarOrdenesAbiertoPresupuestar(logueado);
        List<OrdenTrabajo> ordenesAbiertoPresupuestado = ordenTrabajoServicios.buscarOrdenesAbiertoPresupuestado(logueado);
        List<OrdenTrabajo> ordenesAbiertoAceptado = ordenTrabajoServicios.buscarOrdenesAbiertoAceptado(logueado);
        List<OrdenTrabajo> ordenesFinalizadoCalificar = ordenTrabajoServicios.buscarOrdenesFinalizadoCalificar(logueado);
        List<OrdenTrabajo> ordenesFinalizadoCalificado = ordenTrabajoServicios.buscarOrdenesFinalizadoCalificado(logueado);
        List<OrdenTrabajo> ordenesCerradoTrabajoRechazado = ordenTrabajoServicios.buscarOrdenesCerradoTrabajoRechazado(logueado);
        List<OrdenTrabajo> ordenesCerradoPresupuestoRechazado = ordenTrabajoServicios.buscarOrdenesCerradoPresupuestoRechazado(logueado);
        List<OrdenTrabajo> ordenesCerradoCanceladoCliente = ordenTrabajoServicios.buscarOrdenesCerradoCanceladoCliente(logueado);
        List<OrdenTrabajo> ordenesCerradoCanceladoProveedor = ordenTrabajoServicios.buscarOrdenesCerradoCanceladoProveedor(logueado);
        String proveedorId = logueado.getId();
        List<Calificacion> calificaciones = calificacionServicios.buscarCalificacionesPorProveedor(proveedorId);
        modelo.addAttribute("ordenesAbiertoPresupuestar", ordenesAbiertoPresupuestar);
        modelo.addAttribute("ordenesAbiertoPresupuestado", ordenesAbiertoPresupuestado);
        modelo.addAttribute("ordenesAbiertoAceptado", ordenesAbiertoAceptado);
        modelo.addAttribute("ordenesFinalizadoCalificar", ordenesFinalizadoCalificar);
        modelo.addAttribute("ordenesFinalizadoCalificado", ordenesFinalizadoCalificado);
        modelo.addAttribute("ordenesCerradoTrabajoRechazado", ordenesCerradoTrabajoRechazado);
        modelo.addAttribute("ordenesCerradoPresupuestoRechazado", ordenesCerradoPresupuestoRechazado);
        modelo.addAttribute("ordenesCerradoCanceladoCliente", ordenesCerradoCanceladoCliente);
        modelo.addAttribute("ordenesCerradoCanceladoProveedor", ordenesCerradoCanceladoProveedor);
        modelo.addAttribute("calificaciones", calificaciones);
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        modelo.addAttribute("usuarios", usuarios);

        List<Servicio> servicios = servicioRepositorio.findAll();
        modelo.addAttribute("servicios", servicios);


        return "bandeja-ordenes.html";
    }



    
}
