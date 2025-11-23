package com.papeleria.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String codigo;
    
    private String nombre;
    private String password;
    private String rol;

    public Usuario() {}

    public Usuario(String codigo, String nombre, String password, String rol) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}