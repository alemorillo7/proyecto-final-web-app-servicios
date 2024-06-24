package com.app.servicios.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.servicios.servicios.UsuarioServicios;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.repositorios.UsuarioRepositorio;

@Controller
@RequestMapping("/imagen")

public class ImagenControlador {

    @Autowired
    UsuarioServicios usuarioServicios;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    

    @RequestMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
         @SuppressWarnings("deprecation")
        Usuario usuario = usuarioRepositorio.getById(id);
        byte[] imagen = usuario.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders(); 
        headers.setContentType(MediaType.IMAGE_JPEG);


        return new ResponseEntity<> (imagen, headers, HttpStatus.OK);


    }

}
