package com.app.servicios.servicios;

import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.servicios.entidades.Calificacion;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.CalificacionRepositorio;


@Service
public class CalificacionServicios {

    @Autowired
    private CalificacionRepositorio calificacionRepositorio;

    // Validacion de calificación//
    public void validarCalificacion(Integer puntaje, String comentario, Usuario cliente, Usuario proveedor)
            throws MiExcepcion {
        if (puntaje == null || puntaje < 1 || puntaje > 5) {
            throw new MiExcepcion("El puntaje debe ser mayor o igual a 1 y menor o igual a 5");
        }
        if (comentario == null || comentario.isEmpty()) {
            throw new MiExcepcion("El comentario no puede estar vacío");
        }
        if (cliente == null) {
            throw new MiExcepcion("El cliente no puede estar vacío");
        }
        if (proveedor == null) {
            throw new MiExcepcion("El proveedor no puede estar vacío");
        }
    }

    // Crear una calificación//
    @Transactional
    public void crearCalificacion(Integer puntaje, String comentario, Usuario cliente, Usuario proveedor)
            throws MiExcepcion {

        try {
            validarCalificacion(puntaje, comentario, cliente, proveedor);
            Calificacion calificacion = new Calificacion();
            calificacion.setPuntaje(puntaje);
            calificacion.setComentario(comentario);
            calificacion.setCliente(cliente);
            calificacion.setProveedor(proveedor);
            calificacion.setActivo(true);
            calificacionRepositorio.save(calificacion);

        } catch (MiExcepcion ex) {
            throw new MiExcepcion(ex.getMessage());
        }

    }

    // Modificar una calificación//
    @Transactional
    public void modificarCalificacion(String id, Integer puntaje, String comentario, boolean activo)
            throws MiExcepcion {
        Calificacion calificacion = new Calificacion();
        calificacion = calificacionRepositorio.findById(id).orElse(null);
        calificacion.setPuntaje(puntaje);
        calificacion.setComentario(comentario);
        calificacion.setActivo(activo);
        calificacionRepositorio.save(calificacion);
    }

    // Desactivar una calificación//
    @Transactional
    public void desactivarCalificacion(String id) throws MiExcepcion {
        Calificacion calificacion = new Calificacion();
        calificacion = calificacionRepositorio.findById(id).orElse(null);
        calificacion.setActivo(false);
        calificacionRepositorio.save(calificacion);
    }

    // Activar una calificación//
    @Transactional
    public void activarCalificacion(String id) throws MiExcepcion {
        Calificacion calificacion = new Calificacion();
        calificacion = calificacionRepositorio.findById(id).orElse(null);
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
    public List<Calificacion> buscarCalificacionesPorCliente(Usuario cliente) {
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        calificaciones = calificacionRepositorio.buscarCalificacionesPorCliente(cliente);
        return calificaciones;
    }

    @Transactional(readOnly = true)
    public List<Calificacion> buscarCalificacionesPorProveedoredor(Usuario proveedor) {
        List<Calificacion> calificaciones = new ArrayList<Calificacion>();
        calificaciones = calificacionRepositorio.buscarCalificacionesPorProveedoredor(proveedor);
        return calificaciones;
    }
     
    public Double obtenerPromedioCalificacion (Usuario proveedor){
        Double numero = 0.0;
        return numero;
    }

    
    



    @Transactional(readOnly = true)
    public Calificacion buscarCalificacionPorId(String id) {
        return calificacionRepositorio.findById(id).orElse(null);
    }

}
