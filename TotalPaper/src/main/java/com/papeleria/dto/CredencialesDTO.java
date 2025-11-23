package com.papeleria.dto;

public class CredencialesDTO {
    private String codigo;
    private String password;

    public CredencialesDTO() {}
    public CredencialesDTO(String codigo, String password) {
        this.codigo = codigo;
        this.password = password;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}