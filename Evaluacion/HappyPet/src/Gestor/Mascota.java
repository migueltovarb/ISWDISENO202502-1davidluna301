package Gestor;

import java.util.ArrayList;
import java.util.List;

public class Mascota {
    private String nombre;
    private String especie;
    private int edad;
    private Dueño dueño;
    private List<ControlVeterinario> controlesVeterinarios;

    public Mascota() {
        controlesVeterinarios = new ArrayList<>();
    }

    public Mascota(String nombre, String especie, int edad) {
        this();
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Dueño getDueño() {
        return dueño;
    }

    public void setDueño(Dueño dueño) {
        this.dueño = dueño;
    }

    public List<ControlVeterinario> getControlesVeterinarios() {
        return controlesVeterinarios;
    }

    public void agregarControlVeterinario(ControlVeterinario control) {
        controlesVeterinarios.add(control);
    }

    @Override
    public String toString() {
        return "Mascota: " + nombre + " | Especie: " + especie + " | Edad: " + edad + " años | Dueño: " + dueño.getNombreCompleto();
    }
}

