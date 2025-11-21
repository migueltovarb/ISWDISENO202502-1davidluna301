// config/DataSeeder.java
package com.papeleria.config;

import com.papeleria.service.GestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private GestionService gestionService;

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("=== INICIANDO CARGA DE DATOS INICIALES ===");
            logger.info("Conectando a MongoDB Atlas...");
            
            gestionService.cargarDatosIniciales();
            
            logger.info("=== DATOS INICIALES CARGADOS EXITOSAMENTE ===");
            logger.info("Puedes iniciar sesión con:");
            logger.info("Administrador - Código: ADMIN001, Contraseña: admin123");
            logger.info("Vendedor 1 - Código: VEND001, Contraseña: vend123");
            logger.info("Vendedor 2 - Código: VEND002, Contraseña: vend123");
            
        } catch (DataAccessException e) {
            logger.error("ERROR DE CONEXIÓN A MONGODB: {}", e.getMessage());
            logger.error("Verifica:");
            logger.error("1. Tu conexión a Internet");
            logger.error("2. La cadena de conexión en application.properties");
            logger.error("3. Que tu IP esté en la whitelist de MongoDB Atlas");
            logger.error("4. Las credenciales de la base de datos");
        } catch (Exception e) {
            logger.error("ERROR INESPERADO: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}