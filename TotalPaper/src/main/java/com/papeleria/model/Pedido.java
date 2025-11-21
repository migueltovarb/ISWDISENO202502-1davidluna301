// model/Pedido.java
package com.papeleria.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal; // Importación faltante

@Document(collection = "pedidos")
public class Pedido {
    @Id
    private String id;
    
    private String clienteNombre;
    private String vendedorCodigo;
    private LocalDateTime fecha;
    private String estado; // PENDIENTE, COMPLETADO, CANCELADO
    private List<ProductoPedido> productos;

    // Constructores
    public Pedido() {
        this.fecha = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    
    public String getVendedorCodigo() { return vendedorCodigo; }
    public void setVendedorCodigo(String vendedorCodigo) { this.vendedorCodigo = vendedorCodigo; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<ProductoPedido> getProductos() { return productos; }
    public void setProductos(List<ProductoPedido> productos) { this.productos = productos; }

    // Clase interna para productos del pedido
    public static class ProductoPedido {
        private String codigo;
        private String detalle;
        private BigDecimal precio;
        private int cantidad;

        // Constructores
        public ProductoPedido() {}

        public ProductoPedido(String codigo, String detalle, BigDecimal precio, int cantidad) {
            this.codigo = codigo;
            this.detalle = detalle;
            this.precio = precio;
            this.cantidad = cantidad;
        }

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