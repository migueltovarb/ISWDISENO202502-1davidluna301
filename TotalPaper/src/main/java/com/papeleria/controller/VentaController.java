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
    public ResponseEntity<?> crearPedido(@RequestBody PedidoDTO pedidoDTO, 
                             @RequestHeader("X-Vendedor-Codigo") String vendedorCodigo) {
        try {
            Pedido pedido = new Pedido();
            pedido.setClienteNombre(pedidoDTO.getClienteNombre());
            pedido.setVendedorCodigo(vendedorCodigo);
            
            Pedido pedidoCreado = ventaService.registrarPedido(pedido, pedidoDTO.getProductos());
            return ResponseEntity.ok(pedidoCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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