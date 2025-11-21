// dto/PedidoDTO.java
package com.papeleria.dto;

import java.util.List;
import java.math.BigDecimal;

public class PedidoDTO {
    private String clienteNombre;
    private List<ProductoPedidoDTO> productos;

    // Constructores
    public PedidoDTO() {}

    // Getters y Setters
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    
    public List<ProductoPedidoDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoPedidoDTO> productos) { this.productos = productos; }

    public static class ProductoPedidoDTO {
        private String codigo;
        private String detalle;
        private BigDecimal precio;
        private int cantidad;

        // Getters y Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getDetalle() { return detalle; }
        public void setDetalle(String detalle) { this.detalle = detalle; }
        
        public BigDecimal getPrecio() { return precio; }
        public void setPrecio(BigDecimal precio) { this.precio = precio; }
        
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}