package com.papeleria.controller;

import com.papeleria.dto.*;
import com.papeleria.model.*;
import com.papeleria.service.GestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/gestion")
@CrossOrigin(origins = "*")
public class GestionController {

    @Autowired
    private GestionService gestionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDTO credenciales) {
        try {
            Usuario usuario = gestionService.autenticar(credenciales.getCodigo(), credenciales.getPassword());
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuario")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setCodigo(usuarioDTO.getCodigo());
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setPassword(usuarioDTO.getPassword());
            usuario.setRol(usuarioDTO.getRol());
            
            Usuario usuarioCreado = gestionService.crearUsuario(usuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usuario/{codigo}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String codigo) {
        try {
            gestionService.eliminarUsuario(codigo);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuarios")
    public List<Usuario> getUsuarios() {
        return gestionService.listarTodosUsuarios();
    }

    @GetMapping("/vendedores")
    public List<Usuario> getVendedores() {
        return gestionService.listarVendedores();
    }

    @PostMapping("/producto")
    public ResponseEntity<?> crearProducto(@RequestBody ProductoDTO productoDTO) {
        try {
            Producto producto = new Producto();
            producto.setCodigo(productoDTO.getCodigo());
            producto.setDetalle(productoDTO.getDetalle());
            producto.setPrecio(productoDTO.getPrecio());
            
            Producto productoCreado = gestionService.crearProducto(producto);
            return ResponseEntity.ok(productoCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/producto/{codigo}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigo) {
        try {
            gestionService.eliminarProducto(codigo);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/productos")
    public List<Producto> getProductos() {
        return gestionService.listarProductos();
    }

    @GetMapping("/productos/todos")
    public List<Producto> getTodosProductos() {
        return gestionService.listarTodosProductos();
    }
}