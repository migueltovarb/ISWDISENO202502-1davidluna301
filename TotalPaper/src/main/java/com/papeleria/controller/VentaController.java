// controller/VentaController.java
package com.papeleria.controller;

import com.papeleria.dto.PedidoDTO;
import com.papeleria.model.Pedido;
import com.papeleria.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/venta")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/pedido")
    public Pedido crearPedido(@RequestBody PedidoDTO pedidoDTO, 
                             @RequestHeader("X-Vendedor-Codigo") String vendedorCodigo) {
        
        // Convertir DTO a entidad
        Pedido pedido = new Pedido();
        pedido.setClienteNombre(pedidoDTO.getClienteNombre());
        pedido.setVendedorCodigo(vendedorCodigo);
        
        // Convertir productos del DTO a la entidad
        List<Pedido.ProductoPedido> productos = pedidoDTO.getProductos().stream()
                .map(productoDTO -> new Pedido.ProductoPedido(
                    productoDTO.getCodigo(),
                    productoDTO.getDetalle(),
                    productoDTO.getPrecio(),
                    productoDTO.getCantidad()
                ))
                .toList();
        
        pedido.setProductos(productos);
        
        return ventaService.registrarPedido(pedido);
    }

    @GetMapping("/pedidos")
    public List<Pedido> getPedidos() {
        return ventaService.listarPedidos();
    }

    @GetMapping("/exportar/xlsx")
    public ResponseEntity<ByteArrayResource> exportarPedidosXLSX() {
        return ventaService.generarXLSX();
    }
}