package com.app.servicios.controladores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.repositorios.ServicioRepositorio;
import com.app.servicios.servicios.ServicioServicios;
import com.app.servicios.servicios.UsuarioServicios;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private ServicioServicios servicioServicios;

    @Autowired 
    private UsuarioServicios usuarioServicios;

    @Autowired
    private ServicioRepositorio servicioRepositorio;

   

    @GetMapping("/")
    public String index(ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
        return "index.html";
    }

    @GetMapping("/registrarCliente")
    public String registrarCliente() {
        return "formularioCliente.html";
    }

    @PostMapping("/registroCliente")
    public String registrarClientePost(@RequestParam String nombre, 
                                       @RequestParam String apellido,
                                       @RequestParam Integer dni, 
                                       @RequestParam String localidad, 
                                       @RequestParam String direccion, 
                                       @RequestParam String barrio, 
                                       @RequestParam String telefono, 
                                       @RequestParam String email, 
                                       @RequestParam String password, 
                                       @RequestParam String password2,
                                       MultipartFile archivo, 
                                       ModelMap modelo) throws Exception {
        try {
            usuarioServicios.crearCliente(nombre, apellido, dni, localidad, direccion, barrio, telefono, email, password, password2, archivo);
            modelo.put("exito", "Te has registrado correctamente");
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/registrarCliente";
        }
    }

    @GetMapping("/registrarProveedor")
    public String registrarProveedor(ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
        return "formularioProveedor.html";
    }

    @PostMapping("/registroProveedor")
    public String registrarProveedorPost(@RequestParam String nombre,
                                         @RequestParam String apellido,
                                         @RequestParam Integer dni,
                                         @RequestParam String localidad,
                                         @RequestParam String direccion,
                                         @RequestParam String telefono,
                                         @RequestParam String email,
                                         @RequestParam String password,
                                         @RequestParam String password2,
                                         @RequestParam Integer experiencia,
                                         @RequestParam String descripcion,
                                         @RequestParam List<String> serviciosIds,
                                         ModelMap modelo) {
        try {
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }
            usuarioServicios.crearProveedor(nombre, apellido, dni, localidad, direccion, telefono, email, password, password2, experiencia, descripcion, servicios);
            modelo.put("exito", "Te has registrado correctamente");
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/registrarProveedor";
        }
    }

    @GetMapping("/registarClienteProveedor/{id}")
    public String registrarClienteProveedor(@PathVariable String id, ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
        return "registrarClienteProveedor.html";
    }

    @PostMapping("/registroClienteProveedor/{id}")
    public String registrarClienteProveedorPost(@PathVariable String id, 
                                                @RequestParam Integer experiencia, 
                                                @RequestParam String descripcion, 
                                                @RequestParam List<String> serviciosIds, 
                                                ModelMap modelo) {
        try {
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }
            usuarioServicios.crearClienteProveedor(experiencia, descripcion, servicios, id);
            return "inicio.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/registrarProveedor";
        }
    }

    @GetMapping("/redirectByRole")
    public String redirectByRole(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado == null) {
            return "redirect:/login"; // Manejar caso de sesión no iniciada adecuadamente
        }
    
        switch (logueado.getRol().toString()) {
            case "CLIENTE":
                return "redirect:/inicio";
            case "PROVEEDOR":
                return "redirect:/panelUsuario";
            case "CLIENTEPROVEEDOR":
                return "redirect:/panelUsuario";
            case "ADMIN":
                return "redirect:/inicio";
            case "SUPERADMIN":
                return "redirect:/superadmin";
            default:
                return "redirect:/login"; // Manejar caso de roles no esperados
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {
       
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        
        if (logueado.getRol().toString().equals("SUPERADMIN")) {
            return "redirect:/superadmin";
        }
        modelo.put("exito", "Bienvenido " + logueado.getNombre());
       
        return "inicio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
    @GetMapping("/panelUsuario")
    public String panelUsuario(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin";
        }
        if (logueado.getRol().toString().equals("SUPERADMIN")) {
            return "redirect:/superadmin";
        }
        return "panelUsuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
    @GetMapping("/proveedores")
    public String vistaProveedores(@RequestParam String servicio, ModelMap modelo) {
        List<Usuario> proveedores = usuarioServicios.listarPorServicio(servicio);
        modelo.addAttribute("proveedores", proveedores);
        return "resultado-busqueda.html";
    }

   

    @GetMapping("/proveedores/{nombreServicio}")
    public String mostrarCarpinteria(@PathVariable String nombreServicio, ModelMap modelo) {
        
        Servicio servicio = new Servicio();

        servicio = servicioServicios.buscarServicioPorNombre(nombreServicio);

        String id = servicio.getId();
        
        List<Usuario> proveedores = usuarioServicios.listarPorServicio(id);
        
        // Crear una lista de listas de nombres de servicios
        List<List<String>> listaDeNombresDeServicios = new ArrayList<>();
        
        for (Usuario proveedor : proveedores) {
            List<String> nombresServicios = proveedor.getServicios().stream()
                                                     .map(Servicio::getNombre) // Suponiendo que tienes un método getNombre en tu entidad Servicio
                                                     .collect(Collectors.toList());
            listaDeNombresDeServicios.add(nombresServicios);
        }
    
        modelo.addAttribute("proveedores", proveedores);
        modelo.addAttribute("servicio", id);
        modelo.addAttribute("listaDeNombresDeServicios", listaDeNombresDeServicios);
    
        return "vistaProveedorPorServicio.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
@GetMapping("/perfil")
public String perfil(HttpSession session, ModelMap modelo) {
    List<Servicio> servicios = servicioServicios.listarServiciosActivos();
    Usuario usuario = (Usuario) session.getAttribute("usuariosession");
    
    List<Servicio> serviciosUsuario = servicioServicios.listarServiciosPorUsuario(usuario.getId());
    
    modelo.addAttribute("servicios", servicios);
    modelo.addAttribute("serviciosUsuario", serviciosUsuario);
    modelo.addAttribute("usuario", usuario);
    
    return "perfilUsuario.html";
}

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
    @GetMapping("/actualizarPerfil/{id}")
    public String actualizarPerfil(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("PROVEEDOR")) {
            return "redirect:/actualizarProveedor";
        }
        if (logueado.getRol().toString().equals("CLIENTEPROVEEDOR")) {
            return "redirect:/actualizarClienteProveedor";
        }
        return "formularioModificarCliente.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
    @PostMapping("/modificarPerfil/cliente/{id}")
    public String modificarCliente(@RequestParam String nombre, 
                                @RequestParam String apellido, 
                                @RequestParam Integer dni,
                                @RequestParam String localidad, 
                                @RequestParam String direccion, 
                                @RequestParam String barrio, 
                                @RequestParam String telefono, 
                                @RequestParam String email, 
                                @RequestParam String password, 
                                @RequestParam String password2,
                                @PathVariable String id,
                                ModelMap modelo,
                                HttpSession session) {

        try {
            usuarioServicios.modificarCliente(nombre, apellido, dni, localidad, direccion, barrio, telefono, email, password, password2, id);

            session.setAttribute("usuariosession", usuarioServicios.buscarPorEmail(email));

            return "redirect:/perfil";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR')")
    @PostMapping("/modificarPerfil/proveedor/{id}")
    public String modificarProveedor(@RequestParam String nombre, 
                                @RequestParam String apellido, 
                                @RequestParam Integer dni,
                                @RequestParam String localidad, 
                                @RequestParam String direccion, 
                                @RequestParam String telefono, 
                                @RequestParam String email, 
                                @RequestParam String password, 
                                @RequestParam String password2,
                                @RequestParam Integer experiencia,
                                @RequestParam String descripcion,
                                @RequestParam String[] serviciosIds,
                                @PathVariable String id,
                                ModelMap modelo,
                                HttpSession session) {

     

        try {
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }
            usuarioServicios.modificarProveedor(nombre, apellido, dni, localidad, direccion, telefono, email, password, password2, experiencia, descripcion, servicios, id);

            session.setAttribute("usuariosession", usuarioServicios.buscarPorEmail(email));

            return "redirect:/perfil";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_CLIENTEPROVEEDOR')")
    @PostMapping("/modificarPerfil/clienteproveedor/{id}")
    public String modificarClienteProveedor(@RequestParam String nombre, 
                                @RequestParam String apellido, 
                                @RequestParam Integer dni,
                                @RequestParam String localidad, 
                                @RequestParam String direccion, 
                                @RequestParam String barrio,
                                @RequestParam String telefono, 
                                @RequestParam String email, 
                                @RequestParam String password, 
                                @RequestParam String password2,
                                @RequestParam Integer experiencia,
                                @RequestParam String descripcion,
                                @RequestParam String[] serviciosIds,
                                @PathVariable String id,
                                ModelMap modelo,
                                HttpSession session) {

     

        try {
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }
            usuarioServicios.modificarClienteProveedor(nombre, apellido, dni, localidad, direccion, barrio, telefono, email, password, password2, experiencia, descripcion, servicios, id);

            session.setAttribute("usuariosession", usuarioServicios.buscarPorEmail(email));

            return "redirect:/perfil";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }
    }
}

