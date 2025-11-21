// service/GestionService.java
package com.papeleria.service;

import com.papeleria.model.Usuario;
import com.papeleria.model.Producto;
import com.papeleria.repository.UsuarioRepository;
import com.papeleria.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class GestionService {

    private static final Logger logger = LoggerFactory.getLogger(GestionService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void cargarDatosIniciales() {
        try {
            logger.info("Verificando conexión a la base de datos...");
            
            // Test de conexión contando documentos
            long userCount = usuarioRepository.count();
            long productCount = productoRepository.count();
            
            logger.info("Conexión exitosa. Usuarios: {}, Productos: {}", userCount, productCount);

            // Crear usuarios solo si no existen
            if (userCount == 0) {
                logger.info("Creando usuarios iniciales...");
                
                Usuario admin = new Usuario("ADMIN001", "Administrador Principal", 
                        passwordEncoder.encode("admin123"), "ADMINISTRADOR");
                usuarioRepository.save(admin);
                
                Usuario vendedor1 = new Usuario("VEND001", "Juan Pérez", 
                        passwordEncoder.encode("vend123"), "VENDEDOR");
                usuarioRepository.save(vendedor1);
                
                Usuario vendedor2 = new Usuario("VEND002", "María García", 
                        passwordEncoder.encode("vend123"), "VENDEDOR");
                usuarioRepository.save(vendedor2);
                
                logger.info("✅ 3 usuarios creados exitosamente");
            } else {
                logger.info("✅ Usuarios ya existen en la base de datos");
            }

            // Crear productos solo si no existen
            if (productCount == 0) {
                logger.info("Creando productos de papelería...");
                
                Producto[] productos = {
                    new Producto("PROD001", "Cuaderno Universitario 100 hojas", new BigDecimal("5.50")),
                    new Producto("PROD002", "Lápiz HB Paquete x10", new BigDecimal("3.00")),
                    new Producto("PROD003", "Bolígrafo Azul Paquete x12", new BigDecimal("4.50")),
                    new Producto("PROD004", "Resaltador Amarillo", new BigDecimal("1.20")),
                    new Producto("PROD005", "Goma de Borrar", new BigDecimal("0.80")),
                    new Producto("PROD006", "Tajador con depósito", new BigDecimal("2.50")),
                    new Producto("PROD007", "Regla 30 cm", new BigDecimal("1.50")),
                    new Producto("PROD008", "Tijeras Escolares", new BigDecimal("3.80")),
                    new Producto("PROD009", "Pegamento en Barra", new BigDecimal("2.20")),
                    new Producto("PROD010", "Carpeta de Cartón", new BigDecimal("4.00")),
                    new Producto("PROD011", "Marcadores Permanentes x8", new BigDecimal("6.50")),
                    new Producto("PROD012", "Corrector Líquido", new BigDecimal("2.80")),
                    new Producto("PROD013", "Cinta Adhesiva", new BigDecimal("1.80")),
                    new Producto("PROD014", "Sobre Manila Carta", new BigDecimal("0.50")),
                    new Producto("PROD015", "Calculadora Científica", new BigDecimal("25.00"))
                };
                
                for (Producto producto : productos) {
                    productoRepository.save(producto);
                    logger.debug("Producto creado: {}", producto.getCodigo());
                }
                
                logger.info("✅ {} productos creados exitosamente", productos.length);
            } else {
                logger.info("✅ Productos ya existen en la base de datos");
            }
            
        } catch (DataAccessException e) {
            logger.error("❌ Error de acceso a datos: {}", e.getMessage());
            throw new RuntimeException("No se pudo conectar a MongoDB Atlas. Verifica la configuración.");
        } catch (Exception e) {
            logger.error("❌ Error inesperado: {}", e.getMessage());
            throw new RuntimeException("Error al cargar datos iniciales: " + e.getMessage());
        }
    }

    // Resto de los métodos se mantienen igual...
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
        
        if ("ADMIN001".equals(codigo)) {
            throw new IllegalArgumentException("No se puede eliminar el administrador principal");
        }
        
        usuarioRepository.delete(usuario);
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
}