package com.app.servicios.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.servicios.entidades.Servicio;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.ServicioRepositorio;

@Service

public class ServicioServicio {

    @Autowired
    private ServicioRepositorio servicioRepositorio;

    
public void validarServicio(String nombre) throws MiExcepcion{
    if (nombre == null || nombre.isEmpty()) {
        throw new MiExcepcion("El nombre no puede estar vacío");
    }
}

@Transactional
public void crearServicio(String nombre) throws MiExcepcion{
    validarServicio(nombre);
    Servicio servicio = new Servicio();
    servicio.setNombre(nombre);
    servicio.setActivo(true);
    servicioRepositorio.save(servicio);
}
    
@Transactional (readOnly = true)
public List<Servicio> listarServiciosTodos(){
    List<Servicio> servicios = servicioRepositorio.findAll();
    return servicios;

}

@Transactional (readOnly = true)
public List<Servicio> listarServiciosActivos(){
    List<Servicio> servicios = servicioRepositorio.buscarServiciosActivos();
    return servicios;
}

@Transactional (readOnly = true)
public List<Servicio> listarServiciosInactivos(){
    List<Servicio> servicios = servicioRepositorio.buscarServiciosInactivos();
    return servicios;
}

@Transactional (readOnly = true)
public Servicio buscarServicio(String nombre){
    return servicioRepositorio.buscarServicio(nombre);
}

@Transactional
public void borrarServicio(String id){
  Servicio servicio = new Servicio();
  servicio = servicioRepositorio.findById(id).orElse(null);
  servicio.setActivo(false);
  servicioRepositorio.save(servicio);
}

@Transactional
public void actualizarServicio(String id, String nombreNuevo) throws MiExcepcion{
    validarServicio(nombreNuevo);
        Servicio servicio = new Servicio();
        servicio = servicioRepositorio.findById(id).orElse(null);
        servicio.setNombre(nombreNuevo);
        servicio.setActivo(true);
        servicioRepositorio.save(servicio);
}
}






