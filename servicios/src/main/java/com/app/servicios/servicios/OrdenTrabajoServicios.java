package com.app.servicios.servicios;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.servicios.entidades.Imagen;
import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.EstadoOrden;
import com.app.servicios.enumeraciones.EstadoTrabajo;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.OrdenTrabajoRepositorio;
import com.app.servicios.repositorios.ServicioRepositorio;
import com.app.servicios.repositorios.UsuarioRepositorio;

@Service
public class OrdenTrabajoServicios {
    @Autowired
    private ImagenServicios imagenServicios;
    @Autowired
    private OrdenTrabajoRepositorio ordenTrabajoRepositorio;
    @Autowired
    private ServicioRepositorio servicioRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // agregar titulo hablarlo
    public void validarOrdenTrabajoCliente(String titulo,
            String proveedorId,
            String clienteId,
            List<String> serviciosIds,
            String descripcion)
            throws MiExcepcion {

        if (titulo == null || titulo.isEmpty()) {
            throw new MiExcepcion("El titulo no puede estar vacío");
        }
        if (proveedorId == null) {
            throw new MiExcepcion("El proveedor no puede estar vacío");
        }
        if (clienteId == null) {
            throw new MiExcepcion("El cliente no puede estar vacío");
        }
        if (serviciosIds == null || serviciosIds.isEmpty()) {
            throw new MiExcepcion("El servicio no puede estar vacío");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiExcepcion("La descripción no puede estar vacía");
        }
    }

    public void validarOrdenTrabajoProveedor(Integer presupuesto,
            String comentarioPresupuesto)
            throws MiExcepcion {

        if (presupuesto == null) {
            throw new MiExcepcion("El presupuesto no puede estar vacío");
        }
        if (presupuesto <= 0) {
            throw new MiExcepcion("El presupuesto debe ser mayor a 0");
        }
        if (comentarioPresupuesto == null || comentarioPresupuesto.isEmpty()) {
            throw new MiExcepcion("El comentario del presupuesto no puede estar vacío");
        }

    }

    // CREATE//
    // Crea orden de trabajo ver de agregar titulo
    @Transactional
    public void crearOrdenTrabajo(String titulo,
            String proveedorId,
            String clienteId,
            List<String> serviciosIds,
            String descripcion,
            MultipartFile archivo)
            throws MiExcepcion {

        validarOrdenTrabajoCliente(titulo, proveedorId, clienteId, serviciosIds, descripcion);

        OrdenTrabajo ordenTrabajo = new OrdenTrabajo();
        ordenTrabajo.setTitulo(titulo);
        Usuario proveedor = usuarioRepositorio.findById(proveedorId).orElse(null);
        ordenTrabajo.setProveedor(proveedor);
        Usuario cliente = usuarioRepositorio.findById(clienteId).orElse(null);
        ordenTrabajo.setCliente(cliente);
        Set<Servicio> servicios = new HashSet<>();
        for (String servicioId : serviciosIds) {
            Servicio servicio = servicioRepositorio.findById(servicioId).orElse(null);
            if (servicio != null) {
                servicios.add(servicio);
            }
        }
        ordenTrabajo.setServicios(servicios);
        ordenTrabajo.setDescripcion(descripcion);
        ordenTrabajo.setFecha(new Date());
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = imagenServicios.guardarImagen(archivo);
                ordenTrabajo.setImagen(imagen);
            } catch (MiExcepcion e) {
                throw new MiExcepcion("Error al guardar la imagen: " + e.getMessage());
            }
        } 
        
        ordenTrabajo.setEstadoOrden(EstadoOrden.ABIERTO);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.PRESUPUESTAR);
        ordenTrabajo.setEstado(true);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // READ//
    // Listar ordenes de trabajo
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesTrabajo() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.findAll();
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesTrabajoPorCliente(String id) {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarOrdenPorCliente(id);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesTrabajoPorProveedor(String id) {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarOrdenPorProveedor(id);
        return ordenesTrabajo;
    }

    // VERQUERY//

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesAbierto() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoOrden(EstadoOrden.ABIERTO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> ordenesTrabajoCerrado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoOrden(EstadoOrden.CERRADO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> ordenesTrabajoCalificar() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoOrden(EstadoOrden.CALIFICAR);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> ordenesTrabajoCalificado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoOrden(EstadoOrden.CALIFICADO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesPresupuestar() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.PRESUPUESTAR);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesPresupuestado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.PRESUPUESTADO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesAceptado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.ACEPTADO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesPresupuestoRechazado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio
                .buscarPorEstadoTrabajo(EstadoTrabajo.PRESUPUESTO_RECHAZADO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesTrabajoRechazado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio
                .buscarPorEstadoTrabajo(EstadoTrabajo.TRABAJO_RECHAZADO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesFinalizado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.FINALIZADO);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesCancelado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio
                .buscarPorEstadoTrabajo(EstadoTrabajo.CANCELADO_CLIENTE);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesActivas() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstado(Boolean.TRUE);
        return ordenesTrabajo;
    }

    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesDesactivadas() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstado(Boolean.FALSE);
        return ordenesTrabajo;
    }

    // UPDATE//
    // Presupuesta orden de trabajo
    @Transactional
    public void trabajoPresupuestadoOrdenTrabajo(String ordenTrabajoId,
            Integer presupuesto,
            String comentarioPresupuesto)
            throws MiExcepcion {

        validarOrdenTrabajoProveedor(presupuesto, comentarioPresupuesto);

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(ordenTrabajoId).orElse(null);
        ordenTrabajo.setPresupuesto(presupuesto);
        ordenTrabajo.setComentarioPresupuesto(comentarioPresupuesto);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.PRESUPUESTADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Proveedpr rechaza Trabajo
    @Transactional
    public void trabajoRechazadoOrdenTrabajo(String id, String comentarioPresupuesto) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.TRABAJO_RECHAZADO);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CERRADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }
    // Presupuesto aceptado

    @Transactional
    public void presupuestoAceptadoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.ACEPTADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Presupuesto rechazado
    @Transactional
    public void presupuestoRechazadoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CERRADO);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.PRESUPUESTO_RECHAZADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Proveedor termina trabajo
    @Transactional
    public void trabajoTerminadoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.FINALIZADO);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CALIFICAR);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Cliente cancela trabajo
    @Transactional
    public void clienteCancelaTrabajoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.CANCELADO_CLIENTE);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CERRADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Proveedor cancela trabajo
    @Transactional
    public void proveedorCancelaTrabajoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.CANCELADO_PROVEEDOR);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CERRADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // calificar los trabajos,
    @Transactional
    public void calificarOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CALIFICADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // DELETE//

    // *Borrar orden trabajo (delete) */
    public void desactivarOrdenTrabajo(String id) throws MiExcepcion {
        Optional<OrdenTrabajo> ordenTrabajo = ordenTrabajoRepositorio.findById(id);
        if (ordenTrabajo.isEmpty()) {
            throw new MiExcepcion("No existe la orden de trabajo");
        }
        ordenTrabajo.get().setEstado(false);
        ordenTrabajoRepositorio.save(ordenTrabajo.get());
    }

    // *Activar orden trabajo (update) */
    public void activarOrdenTrabajo(String id) throws MiExcepcion {
        Optional<OrdenTrabajo> ordenTrabajo = ordenTrabajoRepositorio.findById(id);
        if (ordenTrabajo.isEmpty()) {
            throw new MiExcepcion("No existe la orden de trabajo");
        }
        ordenTrabajo.get().setEstado(true);
        ordenTrabajoRepositorio.save(ordenTrabajo.get());
    }
    //Buscar por ID
    @Transactional(readOnly = true)
    public OrdenTrabajo buscarOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        if (ordenTrabajo!=null) {
            return ordenTrabajo;
        } else {
            throw new MiExcepcion("No existe la orden de trabajo");
        }
        
    }
    // Queries de busqueda para tablas en bandeja//
    public List<OrdenTrabajo> buscarOrdenesAbiertoPresupuestar(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.ABIERTO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.PRESUPUESTAR;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesAbiertoPresupuestado(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.ABIERTO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.PRESUPUESTADO;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesAbiertoAceptado(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.ABIERTO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.ACEPTADO;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesFinalizadoCalificar(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.CALIFICAR;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.FINALIZADO;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesFinalizadoCalificado(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.CALIFICADO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.FINALIZADO;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesCerradoTrabajoRechazado(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.CERRADO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.TRABAJO_RECHAZADO;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesCerradoPresupuestoRechazado(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.CERRADO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.PRESUPUESTO_RECHAZADO;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesCerradoCanceladoCliente(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.CERRADO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.CANCELADO_CLIENTE;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }

    public List<OrdenTrabajo> buscarOrdenesCerradoCanceladoProveedor(Usuario idUsuario) {

        EstadoOrden estadoOrden = EstadoOrden.CERRADO;
        EstadoTrabajo estadoTrabajo = EstadoTrabajo.CANCELADO_PROVEEDOR;

        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarOrdenes(idUsuario,
                estadoOrden, estadoTrabajo);

        return ordenesTrabajoAbiertoPresupuestar;

    }
}
