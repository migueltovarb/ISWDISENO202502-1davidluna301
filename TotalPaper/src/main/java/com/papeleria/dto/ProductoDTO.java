// dto/ProductoDTO.java
package com.papeleria.dto;

import java.math.BigDecimal;

public class ProductoDTO {
    private String codigo;
    private String detalle;
    private BigDecimal precio;

    // Constructores
    public ProductoDTO() {}

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}