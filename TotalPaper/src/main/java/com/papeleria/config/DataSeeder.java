package com.papeleria.config;

import com.papeleria.service.GestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private GestionService gestionService;

    @Override
    public void run(String... args) throws Exception {
        gestionService.cargarDatosIniciales();
    }
}