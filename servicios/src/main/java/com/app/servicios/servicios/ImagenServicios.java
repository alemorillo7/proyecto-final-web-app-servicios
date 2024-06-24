package com.app.servicios.servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.servicios.entidades.Imagen;
import com.app.servicios.excepciones.MiExcepcion;
import com.app.servicios.repositorios.ImagenRepositorio;

@Service
public class ImagenServicios {
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Transactional
    public Imagen guardarImagen(MultipartFile archivo) throws MiExcepcion {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getName());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

}

            catch (Exception e) {
                System.err.println(e.getMessage());
}
        }
        return null;
    }

    @Transactional
    public Imagen actualizarImagen (MultipartFile archivo, String idImagen) throws MiExcepcion {
        
        if (archivo != null) {
            try {

                Imagen imagen = new Imagen();

                if (idImagen != null) {
                    Optional<Imagen> imagenAnterior = imagenRepositorio.findById(idImagen);

                    if (imagenAnterior.isPresent()) {
                        imagen = imagenAnterior.get();
                        
                    }
                }
                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getName());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

}

            catch (Exception e) {
                System.err.println(e.getMessage());
}
        }
        return null;

    }


}