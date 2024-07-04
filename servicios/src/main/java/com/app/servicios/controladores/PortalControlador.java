package com.app.servicios.controladores;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.ServicioRepositorio;
import com.app.servicios.servicios.CalificacionServicios;
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

    @Autowired
    private CalificacionServicios calificacionServicios;

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
            ModelMap modelo) throws MiExcepcion, DataIntegrityViolationException, Exception {
        try {
            // Verificar si el cliente ya está registrado con el email
            if (usuarioServicios.existeClientePorEmail(email)) {
                modelo.put("error", "El email ya está registrado.");
                return "formularioCliente.html";
            }

            // Verificar si el cliente ya está registrado con el DNI
            if (usuarioServicios.existeClientePorDni(dni)) {
                modelo.put("error", "El DNI ya está registrado.");
                return "formularioCliente.html";
            }

            // Crear el cliente si no está registrado
            usuarioServicios.crearCliente(nombre, apellido, dni, localidad, direccion, barrio, telefono, email,
                    password, password2, archivo);
            modelo.put("exito", "Te has registrado correctamente");
            return "index.html";

            // Evalua demás excepciones de errores en la carga de datos:
        } catch (MiExcepcion ex) {
            modelo.put("error", "Error en la carga de datos, intente de nuevo" + ex.getMessage());
            return "formularioCliente.html";
        } catch (DataIntegrityViolationException ex) {
            modelo.put("error", "Error en la carga de datos, intente de nuevo" + ex.getMessage());
            return "formularioCliente.html";
        } catch (Exception ex) {
            modelo.put("error", "Error inesperado" + ex.getMessage());
            return "formularioCliente.html";
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
            ModelMap modelo) throws MiExcepcion, DataIntegrityViolationException, Exception {
        try {
            // Verificar si el proveedor ya está registrado con el email
            if (usuarioServicios.existeProveedorPorEmail(email)) {
                modelo.put("error", "El email ya está registrado.");
                List<Servicio> servicios = servicioServicios.listarServiciosActivos();
                modelo.addAttribute("servicios", servicios);
                return "formularioProveedor.html";
            }
            // Verificar si el proveedor ya está registrado con el email
            if (usuarioServicios.existeProveedorPorDni(dni)) {
                modelo.put("error", "El DNI ya está registrado.");
                List<Servicio> servicios = servicioServicios.listarServiciosActivos();
                modelo.addAttribute("servicios", servicios);
                return "formularioProveedor.html";
            }

            // Recopilar los servicios
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }

            // Crear el proveedor si no está registrado
            usuarioServicios.crearProveedor(nombre, apellido, dni, localidad, direccion, telefono, email, password,
                    password2, experiencia, descripcion, servicios);
            modelo.put("exito", "Te has registrado correctamente");
            return "index.html";

            // Evalua demás excepciones de errores en la carga de datos
        } catch (MiExcepcion ex) {
            modelo.put("error", "Error en la carga de datos, intente de nuevo" + ex.getMessage());
            List<Servicio> servicios = servicioServicios.listarServiciosActivos();
            modelo.addAttribute("servicios", servicios);
            return "formularioProveedor.html";
        } catch (DataIntegrityViolationException ex) {
            modelo.put("error", "Error en la carga de datos, intente de nuevo" + ex.getMessage());
            List<Servicio> servicios = servicioServicios.listarServiciosActivos();
            modelo.addAttribute("servicios", servicios); 
            return "formularioProveedor.html";
        } catch (Exception ex) {
            modelo.put("error", "Error inesperado" + ex.getMessage());
            List<Servicio> servicios = servicioServicios.listarServiciosActivos();
            modelo.addAttribute("servicios", servicios);
             return "formularioProveedor.html";
        }
    }

    @GetMapping("/registrarClienteProveedor/{id}")
    public String registrarClienteProveedor(@PathVariable String id, ModelMap modelo) {
        List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
        return "formularioClienteProveedor.html";
    }

    @PostMapping("/registroClienteProveedor/{id}")
    public String registrarClienteProveedorPost(@PathVariable String id,
            @RequestParam Integer experiencia,
            @RequestParam String descripcion,
            @RequestParam List<String> serviciosIds,
            ModelMap modelo, HttpSession session) {
        try {
            Set<Servicio> servicios = new HashSet<>();
            for (String servicioId : serviciosIds) {
                Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
                if (servicio != null) {
                    servicios.add(servicio);
                }
            }
            usuarioServicios.crearClienteProveedor(experiencia, descripcion, servicios, id);
            Usuario usuario = usuarioServicios.buscarUsuario(id);
            session.setAttribute("usuariosession", usuario.getRol());
            return "perfilUsuario.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            List<Servicio> servicios = servicioServicios.listarServiciosActivos();
        modelo.addAttribute("servicios", servicios);
            return "formularioProveedor.html";
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
                return "redirect:/perfil";
            case "CLIENTEPROVEEDOR":
                return "redirect:/inicio";
            case "ADMIN":
                return "redirect:/admin/panel";
            case "SUPERADMIN":
                return "redirect:/admin/panel";
            default:
                return "redirect:/login"; // Manejar caso de roles no esperados
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("SUPERADMIN")) {
            return "redirect:/admin/panel";
        }
        modelo.put("exito", "Bienvenido " + logueado.getNombre());

        return "inicio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVEEDOR', 'ROLE_CLIENTEPROVEEDOR', 'ROLE_CLIENTE', 'ROLE_SUPERADMIN')")
    @GetMapping("/panelUsuario")
    public String panelUsuario(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/panel";
        }
        if (logueado.getRol().toString().equals("SUPERADMIN")) {
            return "redirect:/admin/panel";
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
    public String mostrarProveedoresPorServicio(@PathVariable String nombreServicio, ModelMap modelo) throws MiExcepcion {

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
        //probando
        Map<String, String> promedios = new HashMap<>();
        Map<String, Integer> cantidades = new HashMap<>();
        Map<String, List<Calificacion>> calificacionesPorProveedor = new HashMap<>();

        for (Usuario proveedor : proveedores) {
            String promedio = usuarioServicios.obtenerPromedioCalificaciones(proveedor);
            int cantidad = usuarioServicios.contarCalificaciones(proveedor);
            promedios.put(proveedor.getId(), promedio);
            cantidades.put(proveedor.getId(), cantidad);
            List<Calificacion> calificaciones = calificacionServicios.buscarCalificacionesPorProveedor(proveedor.getId());
            calificacionesPorProveedor.put(proveedor.getId(), calificaciones);
        }
        modelo.addAttribute("promedios", promedios);
        modelo.addAttribute("cantidades", cantidades);
        modelo.addAttribute("calificacionesPorProveedor", calificacionesPorProveedor);
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
            usuarioServicios.modificarCliente(nombre, apellido, dni, localidad, direccion, barrio, telefono, email,
                    password, password2, id);

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
            usuarioServicios.modificarProveedor(nombre, apellido, dni, localidad, direccion, telefono, email, password,
                    password2, experiencia, descripcion, servicios, id);

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
            usuarioServicios.modificarClienteProveedor(nombre, apellido, dni, localidad, direccion, barrio, telefono,
                    email, password, password2, experiencia, descripcion, servicios, id);

            session.setAttribute("usuariosession", usuarioServicios.buscarPorEmail(email));

            return "redirect:/perfil";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }
    }


@GetMapping("/recuperarPass")
public String recuperarPass() {
    return "formularioRecuperoPass.html";
}

@PostMapping("/recuperoPass")
public String recuperarPassPost(@RequestParam String email, ModelMap modelo, @RequestParam String password, @RequestParam String password2) throws MiExcepcion{
    try {
        usuarioServicios.recuperarPass(email, password, password2);
        return "index.html";
    } catch (MiExcepcion e) {
        modelo.put("error", e.getMessage());
        return "redirect:/recuperarPass";
    }

}



}
