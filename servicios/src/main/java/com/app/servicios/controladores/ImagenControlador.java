package com.app.servicios.controladores;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.servicios.servicios.ImagenServicios;
import com.app.servicios.servicios.UsuarioServicios;

import jakarta.servlet.http.HttpSession;

// import jakarta.servlet.http.HttpSession;

// import com.app.servicios.entidades.Imagen;
import com.app.servicios.entidades.Usuario;
import com.app.servicios.repositorios.UsuarioRepositorio;

@Controller
@RequestMapping("/imagen")

public class ImagenControlador {

    @Autowired
    UsuarioServicios usuarioServicios;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    ImagenServicios imagenServicios;
    

    @RequestMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
        Usuario usuario =usuarioRepositorio.findById(id).orElse(null);
        if (usuario != null && usuario.getImagen() != null) {
            byte[] imagen = usuario.getImagen().getContenido();
            HttpHeaders headers = new HttpHeaders(); 
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<> (imagen, headers, HttpStatus.OK);
        } else {
            try {
                ClassPathResource imgFile = new ClassPathResource("static/imagenes/default-profile.jpg");
                byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        

    }

    // @GetMapping("/modificar/{id}")
    // public String mostrarFormularioImagen(@PathVariable String id, ModelMap modelo) {
    //     modelo.put("id", id);
    //     return "formularioImagen.html";
    // }

    // @PostMapping("/modificado/{id}")
    // public String modificarImagen(@PathVariable String id,
    //                                 @RequestParam MultipartFile archivo,
    //                                 ModelMap modelo) {
    //     try {
    //         usuarioServicios.actualizarImagenUsuario(id, archivo);
    //         Usuario usuario = usuarioRepositorio.findById(id).orElse(null);
    //         if (usuario != null) {
    //             usuario.setImagen(imagen);
    //             usuarioRepositorio.save(usuario);
    //         }
    //         return "redirect:/inicio";
    //     } catch (Exception ex) {
    //         modelo.put("error", ex.getMessage());
    //         return "redirect:/imagen/modificar/" + id;
    //     }
    // }
    @PostMapping("/cambiarImagen")
    public String cambiarImagen(@RequestParam String id,
                                @RequestParam MultipartFile archivo,
                                ModelMap modelo,
                                HttpSession session
                                ) {

        Usuario usuario = usuarioRepositorio.findById(id).orElse(null);
        System.out.println("Usuario: " + usuario.getApellido());
        try {

                usuarioServicios.actualizarImagenUsuario(id, archivo);
            
            session.setAttribute("usuariosession", usuario);
            modelo.put("exito", "Imagen modificada exitosamente");

            return "redirect:/perfil";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/perfil";
        }
    }
}

