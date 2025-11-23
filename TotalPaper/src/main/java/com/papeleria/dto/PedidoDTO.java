package com.papeleria.dto;

import java.util.List;

public class PedidoDTO {
    private String clienteNombre;
    private List<ProductoPedidoDTO> productos;

    public PedidoDTO() {}

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public List<ProductoPedidoDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoPedidoDTO> productos) { this.productos = productos; }

    public static class ProductoPedidoDTO {
        private String codigo;
        private int cantidad;

        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}