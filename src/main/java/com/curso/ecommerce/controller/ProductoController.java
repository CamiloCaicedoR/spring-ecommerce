package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = Logger.getLogger(ProductoController.class.getName());

    @Autowired
    private UploadFileService upload;

    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("Producto de la vista: " + producto);
        Usuario usuario = new Usuario(1, "", "", "", "", "", "", "");
        producto.setUsuario(usuario);
        // imagen
        if (producto.getId() == null) {
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();

        LOGGER.info("Producto a editar: " + producto);
        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img")  MultipartFile file) throws IOException {
        LOGGER.info("Producto a actualizar: " + producto);
        Producto p = new Producto();
        p = productoService.get(producto.getId()).get();
        if (!file.isEmpty()) { // cuando se edita tambien la imagen
            if  (!p.getImagen().equals("default.jpg")) {
                try {
                    upload.deleteImage(p.getImagen());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        } else { // editamos el prod sin cambiar la imagen
            producto.setImagen(p.getImagen());
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Producto p = new Producto();
        p = productoService.get(id).get();
        if  (!p.getImagen().equals("default.jpg")) {
            try {
                upload.deleteImage(p.getImagen());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productoService.delete(id);
        return "redirect:/productos";
    }
}
