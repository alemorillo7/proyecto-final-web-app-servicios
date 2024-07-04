package com.app.servicios.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.enumeraciones.EstadoCalificacion;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.CalificacionRepositorio;
import com.app.servicios.repositorios.UsuarioRepositorio;

@Service
public class CalificacionServicios {

    @Autowired
    private CalificacionRepositorio calificacionRepositorio;
    @Autowired
    private UsuarioServicios usuarioServicios;
    @Autowired
    private OrdenTrabajoServicios ordenTrabajoServicios;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    

    // Validacion de calificación//
    public void validarCalificacion(Integer puntaje, String comentario, String clienteId, String proveedorId,
            String ordenTrabajoId)
            throws MiExcepcion {
        if (puntaje == null || puntaje < 1 || puntaje > 5) {
            throw new MiExcepcion("El puntaje debe ser mayor o igual a 1 y menor o igual a 5");
        }
        if (comentario == null || comentario.isEmpty()) {
            throw new MiExcepcion("El comentario no puede estar vacío");
        }
        if (clienteId == null) {
            throw new MiExcepcion("El cliente no puede estar vacío");
        }
        if (proveedorId == null) {
            throw new MiExcepcion("El proveedor no puede estar vacío");
        }
        if (ordenTrabajoId == null) {
            throw new MiExcepcion("La orden de trabajo no puede estar vacía");
        }
    }

    // Crear una calificación//
    @Transactional
    public void crearCalificacion(Integer puntaje, String comentario, String clienteId, String proveedorId,
            String ordenTrabajoId)
            throws MiExcepcion {

                


        try {
            ordenTrabajoServicios.calificarOrdenTrabajo(ordenTrabajoId);
            validarCalificacion(puntaje, comentario, clienteId, proveedorId, ordenTrabajoId);
            Calificacion calificacion = new Calificacion();
            calificacion.setPuntaje(puntaje);
            calificacion.setComentario(comentario);
            Usuario cliente = usuarioServicios.buscarUsuario(clienteId);
            calificacion.setCliente(cliente);
            Usuario proveedor = usuarioServicios.buscarUsuario(proveedorId);
            calificacion.setProveedor(proveedor);
            OrdenTrabajo ordenTrabajo = ordenTrabajoServicios.buscarOrdenTrabajo(ordenTrabajoId);
            calificacion.setOrdenTrabajo(ordenTrabajo);
            calificacion.setActivo(true);
            calificacion.setEstadoCalificacion(EstadoCalificacion.CALIFICADA);
            calificacionRepositorio.save(calificacion);
            

        } catch (MiExcepcion ex) {
            throw new MiExcepcion(ex.getMessage());
        }

    }

    // Modificar una calificación//
    @Transactional
    public void modificarCalificacion(String id, String comentario)
            throws MiExcepcion {
        try {
            Calificacion calificacion = calificacionRepositorio.findById(id)
                    .orElseThrow(() -> new MiExcepcion("La calificación no existe"));

            calificacion.setComentario(comentario);
            calificacionRepositorio.save(calificacion);

        } catch (MiExcepcion e) {
            throw e; // Re-lanzar la excepción específica de negocio
        } catch (Exception e) {
            throw new MiExcepcion("Error al modificar la calificación", e); // Encapsular cualquier otro error
                                                                            // inesperado
        }

    }

    // Desactivar una calificación//
    @Transactional
    public void desactivarCalificacion(String id) throws MiExcepcion {
        Calificacion calificacion = calificacionRepositorio.findById(id)
        .orElseThrow(() -> new MiExcepcion("La calificación no existe"));
        calificacion.setActivo(false);
        calificacionRepositorio.save(calificacion);
    }

    // Activar una calificación//
    @Transactional
    public void activarCalificacion(String id) throws MiExcepcion {
        Calificacion calificacion = calificacionRepositorio.findById(id)
        .orElseThrow(() -> new MiExcepcion("La calificación no existe"));
        calificacion.setActivo(true);
        calificacionRepositorio.save(calificacion);
    }

    // Listado de calificaciones//
    @Transactional(readOnly = true)
    public List<Calificacion> listarCalificacionesTodos() {
        List<Calificacion> calificacionesTodos = calificacionRepositorio.findAll();
        return calificacionesTodos;
    }

    @Transactional(readOnly = true)
    public List<Calificacion> buscarCalificacionesActivas() {
        List<Calificacion> calificacionesActivas = calificacionRepositorio.buscarCalificacionesActivas();
        return calificacionesActivas;
    }

    @Transactional(readOnly = true)
    public List<Calificacion> buscarCalificacionesInactivas() {
        List<Calificacion> calificacionesInactivas = calificacionRepositorio.buscarCalificacionesInactivas();
        return calificacionesInactivas;
    }

    @Transactional(readOnly = true)
    public List<Calificacion> buscarCalificacionesPorCliente(String clienteId) {
        Usuario cliente = usuarioRepositorio.findById(clienteId).orElse(null);
        List<Calificacion> calificaciones = calificacionRepositorio.buscarCalificacionesPorProveedor(cliente);
        return calificaciones;
    }

    @Transactional(readOnly = true)
    public List<Calificacion> buscarCalificacionesPorProveedor(String proveedorId) {
        Usuario proveedor = usuarioRepositorio.findById(proveedorId).orElse(null);
        List<Calificacion> calificaciones = calificacionRepositorio.buscarCalificacionesPorProveedor(proveedor);
        return calificaciones;
    }

    @Transactional(readOnly = true)
    public Calificacion buscarCalificacionPorId(String id) {
        return calificacionRepositorio.findById(id).orElse(null);
    }

    public Double obtenerPromedioCalificacion(Usuario proveedor) {
        Double numero = 0.0;
        return numero;
    }

    // Metodo denunciar calificacion
    @Transactional
    public void denunciarCalificacion(String id) throws MiExcepcion {
        Calificacion calificacion = calificacionRepositorio.findById(id)
                .orElseThrow(() -> new MiExcepcion("La calificación no existe"));
        calificacion.setEstadoCalificacion(EstadoCalificacion.DENUNCIADA);
        calificacionRepositorio.save(calificacion);
    }
    @Transactional
    public List<Calificacion> listarCalificacionesDenunciadas(){
        List<Calificacion> calificacionesDenunciadas = calificacionRepositorio.listarCalificacionesDenunciadas();
        return calificacionesDenunciadas;
    }

    // Metodo censurar calificacion
    @Transactional
    public void censurarCalificacion(String id, String comentario) throws MiExcepcion {
        Calificacion calificacion = calificacionRepositorio.findById(id)
                .orElseThrow(() -> new MiExcepcion("La calificación no existe"));
        calificacion.setEstadoCalificacion(EstadoCalificacion.CENSURADA);
        calificacion.setComentario(comentario);
        calificacionRepositorio.save(calificacion);
    }
    // Censurar comentarios calificacion
    @Transactional
    public void censurarComentario(String id, String comentario) throws Exception {
        Calificacion calificacion = calificacionRepositorio.findById(id).orElseThrow(() -> new Exception("Calificación no encontrada"));
        calificacion.setComentario(comentario);
        calificacion.setEstadoCalificacion(EstadoCalificacion.CENSURADA);
        calificacionRepositorio.save(calificacion);
    }
    // Eliminar comentario calificacion
    @Transactional
    public void eliminarComentarioCalificacion(String id) throws MiExcepcion {
        System.out.println("entre al servicio");
        Calificacion calificacion = calificacionRepositorio.findById(id)
                .orElseThrow(() -> new MiExcepcion("La calificación no existe"));
        String comentario = " ";
        calificacion.setComentario(comentario);
        calificacionRepositorio.save(calificacion);
    }
    

}

