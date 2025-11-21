// dto/UsuarioDTO.java
package com.papeleria.dto;

public class UsuarioDTO {
    private String codigo;
    private String nombre;
    private String password;
    private String rol;

    // Constructores
    public UsuarioDTO() {}

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}