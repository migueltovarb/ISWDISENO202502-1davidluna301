package com.papeleria.service;

import com.papeleria.model.Producto;
import com.papeleria.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> buscarProductos(String query) {
        List<Producto> porDetalle = productoRepository.findByDetalleContainingIgnoreCaseAndActivoTrue(query);
        List<Producto> porCodigo = productoRepository.findByCodigoContainingAndActivoTrue(query);
        
        porDetalle.addAll(porCodigo.stream()
                .filter(producto -> porDetalle.stream().noneMatch(p -> p.getId().equals(producto.getId())))
                .collect(Collectors.toList()));
        
        return porDetalle;
    }
}