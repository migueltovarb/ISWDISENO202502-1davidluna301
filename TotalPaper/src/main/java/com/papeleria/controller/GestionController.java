// controller/GestionController.java
package com.papeleria.controller;

import com.papeleria.dto.CredencialesDTO;
import com.papeleria.dto.ProductoDTO;
import com.papeleria.dto.UsuarioDTO;
import com.papeleria.model.Producto;
import com.papeleria.model.Usuario;
import com.papeleria.service.GestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gestion")
@CrossOrigin(origins = "*")
public class GestionController {

    @Autowired
    private GestionService gestionService;

    @PostMapping("/usuario")
    public Usuario crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setCodigo(usuarioDTO.getCodigo());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setRol(usuarioDTO.getRol());
        
        return gestionService.crearUsuario(usuario);
    }

    @DeleteMapping("/usuario/{codigo}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String codigo) {
        gestionService.eliminarUsuario(codigo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/producto")
    public Producto crearProducto(@RequestBody ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setCodigo(productoDTO.getCodigo());
        producto.setDetalle(productoDTO.getDetalle());
        producto.setPrecio(productoDTO.getPrecio());
        
        return gestionService.crearProducto(producto);
    }

    @DeleteMapping("/producto/{codigo}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigo) {
        gestionService.eliminarProducto(codigo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDTO credenciales) {
        try {
            Usuario usuario = gestionService.autenticar(credenciales.getCodigo(), credenciales.getPassword());
            
            Map<String, String> response = new HashMap<>();
            response.put("rol", usuario.getRol());
            response.put("nombre", usuario.getNombre());
            response.put("codigo", usuario.getCodigo());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}