package Gestor;

import java.util.ArrayList;
import java.util.List;

public class Veterinaria {
    private List<Dueño> dueños;

    public Veterinaria() {
        dueños = new ArrayList<>();
    }

    public boolean registrarDueño(Dueño dueño) {
        if (dueño.getNombreCompleto().isEmpty() || dueño.getDocumento().isEmpty() || dueño.getTelefono().isEmpty()) {
            System.out.println("❌ Campos obligatorios vacíos.");
            return false;
        }
        for (Dueño d : dueños) {
            if (d.getDocumento().equals(dueño.getDocumento())) {
                System.out.println("❌ Ya existe un dueño con ese documento.");
                return false;
            }
        }
        dueños.add(dueño);
        System.out.println("✅ Dueño registrado correctamente.");
        return true;
    }

    public Dueño buscarDueño(String documento) {
        for (Dueño d : dueños) {
            if (d.getDocumento().equals(documento)) return d;
        }
        return null;
    }

    public boolean registrarMascota(String documentoDueño, Mascota mascota) {
        Dueño dueño = buscarDueño(documentoDueño);
        if (dueño == null) {
            System.out.println("❌ Dueño no encontrado.");
            return false;
        }
        if (mascota.getNombre().isEmpty() || mascota.getEspecie().isEmpty() || mascota.getEdad() < 0) {
            System.out.println("❌ Campos de mascota vacíos o inválidos.");
            return false;
        }
        boolean agregado = dueño.agregarMascota(mascota);
        if (agregado) System.out.println("✅ Mascota registrada correctamente.");
        return agregado;
    }

    public boolean registrarControl(String documentoDueño, String nombreMascota, ControlVeterinario control) {
        Dueño dueño = buscarDueño(documentoDueño);
        if (dueño == null) {
            System.out.println("❌ Dueño no encontrado.");
            return false;
        }
        Mascota mascota = null;
        for (Mascota m : dueño.getMascotas()) {
            if (m.getNombre().equalsIgnoreCase(nombreMascota)) {
                mascota = m;
                break;
            }
        }
        if (mascota == null) {
            System.out.println("❌ Mascota no encontrada.");
            return false;
        }
        mascota.agregarControlVeterinario(control);
        System.out.println("✅ Control veterinario registrado.");
        return true;
    }

    public void mostrarHistorial(String documentoDueño, String nombreMascota) {
        Dueño dueño = buscarDueño(documentoDueño);
        if (dueño == null) {
            System.out.println("❌ Dueño no encontrado.");
            return;
        }
        for (Mascota m : dueño.getMascotas()) {
            if (m.getNombre().equalsIgnoreCase(nombreMascota)) {
                System.out.println("\n--- Historial de " + m.getNombre() + " ---");
                for (ControlVeterinario c : m.getControlesVeterinarios()) {
                    System.out.println(c);
                }
                System.out.println("Total controles: " + m.getControlesVeterinarios().size());
                return;
            }
        }
        System.out.println("❌ Mascota no encontrada.");
    }

    public void listarDueños() {
        for (Dueño d : dueños) {
            System.out.println(d);
        }
    }
}

