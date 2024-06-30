package com.app.servicios.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.entidades.Servicio;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.EstadoOrden;
import com.app.servicios.enumeraciones.EstadoTrabajo;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.OrdenTrabajoRepositorio;

@Service
public class OrdenTrabajoServicios {
    @Autowired
    private OrdenTrabajoRepositorio ordenTrabajoRepositorio;

    // agregar titulo hablarlo
    public void validarOrdenTrabajoCliente(String titulo,
                                            Usuario proveedor,
                                            Usuario cliente,
                                            Set<Servicio> servicios,
                                            String descripcion) 
                                            throws MiExcepcion {

        if (titulo == null || titulo.isEmpty()) {
            throw new MiExcepcion("El titulo no puede estar vacío");
        }
        if (proveedor == null) {
            throw new MiExcepcion("El proveedor no puede estar vacío");
        }
        if (cliente == null) {
            throw new MiExcepcion("El cliente no puede estar vacío");
        }
        if (servicios == null || servicios.isEmpty()) {
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
                                    Usuario proveedor,
                                    Usuario cliente,
                                    Set<Servicio> servicios,
                                    String descripcion,
                                    byte[] imagen,
                                    byte[] video)
                                    throws MiExcepcion {

        validarOrdenTrabajoCliente(titulo, proveedor, cliente, servicios, descripcion);

        OrdenTrabajo ordenTrabajo = new OrdenTrabajo();
        ordenTrabajo.setTitulo(titulo);
        ordenTrabajo.setProveedor(proveedor);
        ordenTrabajo.setCliente(cliente);
        ordenTrabajo.setServicios(servicios);
        ordenTrabajo.setDescripcion(descripcion);
        ordenTrabajo.setImagen(imagen);
        ordenTrabajo.setVideo(video);
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
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.PRESUPUESTO_RECHAZADO);
        return ordenesTrabajo;
    }
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesTrabajoRechazado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.TRABAJO_RECHAZADO);
        return ordenesTrabajo;
    }
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesFinalizado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.FINALIZADO);
        return ordenesTrabajo;
    }
    @Transactional(readOnly = true)
    public List<OrdenTrabajo> listarOrdenesCancelado() {
        List<OrdenTrabajo> ordenesTrabajo = ordenTrabajoRepositorio.buscarPorEstadoTrabajo(EstadoTrabajo.CANCELADO);
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
    public void presupuestarOrdenTrabajo(Integer presupuesto,
                                            String comentarioPresupuesto,
                                            String id) 
                                            throws MiExcepcion {

        validarOrdenTrabajoProveedor(presupuesto, comentarioPresupuesto);

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setPresupuesto(presupuesto);
        ordenTrabajo.setComentarioPresupuesto(comentarioPresupuesto);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.PRESUPUESTADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }
    // Presupuesto aceptado

    @Transactional
    public void aceptaPresupuestoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.ACEPTADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Proveedor termina trabajo
    @Transactional
    public void terminaTrabajoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.FINALIZADO);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CALIFICAR);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Proveedor cancela trabajo
    @Transactional
    public void cancelaTrabajoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.CANCELADO);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CALIFICAR);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Presupuesto rechazado
    @Transactional
    public void rechazaPresupuestoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CERRADO);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.PRESUPUESTO_RECHAZADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Trabajo rechazado
    @Transactional
    public void rechazaTrabajoOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoTrabajo(EstadoTrabajo.TRABAJO_RECHAZADO);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CERRADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // calificar los trabajos, mejorar el buscar buscar por id
    @Transactional
    public void calificarOrdenTrabajo(String id) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElse(null);
        ordenTrabajo.setEstadoOrden(EstadoOrden.CALIFICADO);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    //DELETE//

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




/* 
     @Service
    public List<OrdenTrabajo> buscarOrdenesAbiertoPresupuestar(String idUsuario, String estadoOrden, String estadoTrabajo) {

        //@Query("SELECT o FROM OrdenTrabajo o WHERE (o.cliente = :idUsuario OR o.proveedor = :idUsuario) AND o.estadoOrden = :estadoOrden AND o.estadoTrabajo = :estadoTrabajo")//
        List<OrdenTrabajo> ordenesTrabajoAbiertoPresupuestar = ordenTrabajoRepositorio.buscarAbiertoPresupuestar(idUsuario);


        for(int i = 0; i < ordenesTrabajo.size(); i++) {
            
            String ordenTrabajoId = ordenesTrabajo.get(i).getId();

            List<Servicio> servicios = new ArrayList<>();

            @Query("SELECT s.nombre FROM OrdenTrabajo ot JOIN ot.servicios s WHERE ot.id = ordenTrabajoId")



        }
        



    } */
}
