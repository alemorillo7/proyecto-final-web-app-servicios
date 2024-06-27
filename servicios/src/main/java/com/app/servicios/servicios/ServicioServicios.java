package com.app.servicios.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.servicios.entidades.Servicio;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.ServicioRepositorio;



@Service
public class ServicioServicios {
    
    @Autowired
    private ServicioRepositorio servicioRepositorio;

    //Validacion del nombre del servicio//
    public void validarServicio(String nombre) throws MiExcepcion {
        
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("El nombre no puede estar vacío");
            
        } 
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////

    //Creacion de Servicio//
    @Transactional
    public void crearServicio(String nombre) throws MiExcepcion {
        
        try {
            validarServicio(nombre);
            Servicio servicio = new Servicio();
            servicio.setNombre(nombre);
            servicio.setActivo(true);
            servicioRepositorio.save(servicio);
            
        } catch (MiExcepcion ex) {
            throw new MiExcepcion(ex.getMessage());
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////


    //Eliminacion de Servicio//
    @Transactional
    public void desactivarServicio(String id) throws MiExcepcion {
        
        Servicio servicio = new Servicio();
        servicio = servicioRepositorio.findById(id).orElse(null);
        servicio.setActivo(false);
        servicioRepositorio.save(servicio);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////


    //Listado de Servicios//

    @Transactional (readOnly = true)
    public List<Servicio> listarServiciosTodos() {
        List<Servicio> serviciosTodos = servicioRepositorio.findAll();
        return serviciosTodos;
    }


    @Transactional (readOnly = true)
    public List<Servicio> listarServiciosActivos() {
        List<Servicio> serviciosActivos = servicioRepositorio.buscarServiciosActivos();
        return serviciosActivos;
    }


    @Transactional (readOnly = true)
    public List<Servicio> listarServiciosInactivos() {
        List<Servicio> serviciosInactivos = servicioRepositorio.buscarServiciosInactivos();
        return serviciosInactivos;
    }

    @Transactional (readOnly = true)
    public List<Servicio> listarServiciosPorUsuario(String usuarioId) {
        return servicioRepositorio.buscarServiciosPorIdUsuario(usuarioId);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////


    //Busqueda de Servicio//
    @Transactional (readOnly = true)
    public Servicio buscarServicioPorNombre(String nombre) {
        Servicio servicio = new Servicio();
        servicio = servicioRepositorio.buscarServicioPorNombre(nombre);
        return servicio;
    }



    @Transactional (readOnly = true)
    public Servicio buscarServicioPorId(String id) {
        Servicio servicio = new Servicio();
        servicio = servicioRepositorio.findById(id).orElse(null);
        return servicio;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////

//Modificar servicio

@Transactional
public void modificarServicio(String id, String nombreNuevo) throws MiExcepcion {
    
    Servicio servicio = new Servicio();
    servicio = servicioRepositorio.findById(id).orElse(null);
    servicio.setNombre(nombreNuevo);
    servicio.setActivo(true);
    servicioRepositorio.save(servicio);

}
}
