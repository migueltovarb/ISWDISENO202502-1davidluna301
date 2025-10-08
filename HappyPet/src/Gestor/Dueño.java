package Gestor;

import java.util.ArrayList;
import java.util.List;

public class Dueño {
    private String nombreCompleto;
    private String documento;
    private String telefono;
    private List<Mascota> mascotas;

    public Dueño() {
        mascotas = new ArrayList<>();
    }

    public Dueño(String nombreCompleto, String documento, String telefono) {
        this();
        this.nombreCompleto = nombreCompleto;
        this.documento = documento;
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public boolean agregarMascota(Mascota mascota) {
        for (Mascota m : mascotas) {
            if (m.getNombre().equalsIgnoreCase(mascota.getNombre())) {
                System.out.println("❌ Ya existe una mascota con ese nombre para este dueño.");
                return false;
            }
        }
        mascota.setDueño(this);
        mascotas.add(mascota);
        return true;
    }

    @Override
    public String toString() {
        return "Dueño: " + nombreCompleto + " | Documento: " + documento + " | Teléfono: " + telefono;
    }
}

