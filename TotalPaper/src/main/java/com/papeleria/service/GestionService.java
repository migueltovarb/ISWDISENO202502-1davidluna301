package com.papeleria.service;

import com.papeleria.model.Usuario;
import com.papeleria.model.Producto;
import com.papeleria.repository.UsuarioRepository;
import com.papeleria.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GestionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void cargarDatosIniciales() {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario("ADMIN001", "Administrador Principal", 
                    passwordEncoder.encode("admin123"), "ADMINISTRADOR");
            usuarioRepository.save(admin);
            
            Usuario vendedor1 = new Usuario("VEND001", "Juan Pérez", 
                    passwordEncoder.encode("vend123"), "VENDEDOR");
            usuarioRepository.save(vendedor1);
            
            Usuario vendedor2 = new Usuario("VEND002", "María García", 
                    passwordEncoder.encode("vend123"), "VENDEDOR");
            usuarioRepository.save(vendedor2);
        }
        
        if (productoRepository.count() == 0) {
            Producto[] productos = {
                new Producto("PROD001", "Cuaderno Universitario 100 hojas", new java.math.BigDecimal("5.50")),
                new Producto("PROD002", "Lápiz HB Paquete x10", new java.math.BigDecimal("3.00")),
                new Producto("PROD003", "Bolígrafo Azul Paquete x12", new java.math.BigDecimal("4.50")),
                new Producto("PROD004", "Resaltador Amarillo", new java.math.BigDecimal("1.20")),
                new Producto("PROD005", "Goma de Borrar", new java.math.BigDecimal("0.80")),
                new Producto("PROD006", "Tajador con depósito", new java.math.BigDecimal("2.50")),
                new Producto("PROD007", "Regla 30 cm", new java.math.BigDecimal("1.50")),
                new Producto("PROD008", "Tijeras Escolares", new java.math.BigDecimal("3.80")),
                new Producto("PROD009", "Pegamento en Barra", new java.math.BigDecimal("2.20")),
                new Producto("PROD010", "Carpeta de Cartón", new java.math.BigDecimal("4.00"))
            };
            
            for (Producto producto : productos) {
                productoRepository.save(producto);
            }
        }
    }

    public Usuario autenticar(String codigo, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCodigo(codigo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return usuario;
            }
        }
        throw new IllegalArgumentException("Credenciales inválidas");
    }

    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCodigo(usuario.getCodigo())) {
            throw new IllegalArgumentException("El código de usuario ya existe");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(String codigo) {
        Usuario usuario = usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    public List<Usuario> listarVendedores() {
        return usuarioRepository.findByRol("VENDEDOR");
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Producto crearProducto(Producto producto) {
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("El código de producto ya existe");
        }
        return productoRepository.save(producto);
    }

    public void eliminarProducto(String codigo) {
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarTodosProductos() {
        return productoRepository.findAll();
    }
}