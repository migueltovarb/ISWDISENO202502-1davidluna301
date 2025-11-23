package com.papeleria.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.math.BigDecimal;

@Document(collection = "productos")
public class Producto {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String codigo;
    
    private String detalle;
    private BigDecimal precio;
    private boolean activo = true;

    public Producto() {}

    public Producto(String codigo, String detalle, BigDecimal precio) {
        this.codigo = codigo;
        this.detalle = detalle;
        this.precio = precio;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}