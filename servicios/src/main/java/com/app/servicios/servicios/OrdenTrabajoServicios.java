package com.app.servicios.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.servicios.entidades.OrdenTrabajo;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.OrdenTrabajoRepositorio;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenTrabajoServicios {


    @Autowired
    private OrdenTrabajoRepositorio ordenTrabajoRepositorio;
      
    // Método para crear una nueva orden de trabajo
    @Transactional
    public void crearOrdenTrabajo(Integer nroOrden, String descripcion) throws MiExcepcion {
        validarOrdenTrabajo(nroOrden, descripcion);
        
        OrdenTrabajo ordenTrabajo = new OrdenTrabajo();
        ordenTrabajo.setNroOrden(nroOrden);
        ordenTrabajo.setDescripcion(descripcion);
        ordenTrabajo.setEstado("Pendiente");
        
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }
    
    // Método para modificar una orden de trabajo existente
    @Transactional
    public void modificarOrdenTrabajo(String id, Integer nroOrden, String descripcion) throws MiExcepcion {
        validarOrdenTrabajo(nroOrden, descripcion);
        
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElseThrow(() -> new MiExcepcion("Orden de trabajo no encontrada"));
        ordenTrabajo.setNroOrden(nroOrden);
        ordenTrabajo.setDescripcion(descripcion);
        
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }
     
    // Método para cambiar el estado de una orden de trabajo
    @Transactional
    public void cambiarEstadoOrdenTrabajo(String id, String nuevoEstado) throws MiExcepcion {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepositorio.findById(id).orElseThrow(() -> new MiExcepcion("Orden de trabajo no encontrada"));
        ordenTrabajo.setEstado(nuevoEstado);
        ordenTrabajoRepositorio.save(ordenTrabajo);
    }

    // Método para listar todas las órdenes de trabajo
    @Transactional (readOnly = true)
    public List<OrdenTrabajo> listarTodasOrdenesTrabajo() {
        return ordenTrabajoRepositorio.findAll();
    }
     
    // Método para buscar una orden de trabajo por su ID
    @Transactional (readOnly = true)
    public OrdenTrabajo buscarOrdenTrabajoPorId(String id) {
        return ordenTrabajoRepositorio.findById(id).orElse(null);
    }
     
    // Método para buscar órdenes de trabajo por estado
    @Transactional (readOnly = true)
    public List<OrdenTrabajo> buscarOrdenesTrabajoPorEstado(String estado) {
        return ordenTrabajoRepositorio.buscarOrdenesTrabajoPorEstado(estado);
    }
     
    // Método privado para validar los datos de una orden de trabajo
    private void validarOrdenTrabajo(Integer nroOrden, String descripcion) throws MiExcepcion {
        if (nroOrden == null || nroOrden <= 0) {
            throw new MiExcepcion("El número de orden debe ser un valor positivo");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new MiExcepcion("La descripción no puede estar vacía");
        }
    }

}
