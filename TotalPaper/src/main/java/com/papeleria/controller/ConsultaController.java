package com.papeleria.controller;

import com.papeleria.model.Producto;
import com.papeleria.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/consulta")
@CrossOrigin(origins = "*")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/productos")
    public List<Producto> getProductos() {
        return consultaService.listarProductos();
    }

    @GetMapping("/productos/buscar")
    public List<Producto> getProductosByQuery(@RequestParam String query) {
        return consultaService.buscarProductos(query);
    }
}