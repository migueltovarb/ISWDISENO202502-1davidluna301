package Gestor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Veterinaria sistema = new Veterinaria();
        int opcion;

        //SIMULACIÓN DE DATOS INICIALES
        
        Dueño d1 = new Dueño("Juan Pérez", "1001", "555-1234");
        Dueño d2 = new Dueño("María López", "1002", "555-5678");
        sistema.registrarDueño(d1);
        sistema.registrarDueño(d2);

        Mascota m1 = new Mascota("Firulais", "Perro", 5);
        Mascota m2 = new Mascota("Michi", "Gato", 3);
        Mascota m3 = new Mascota("Rocky", "Perro", 2);
        sistema.registrarMascota("1001", m1);
        sistema.registrarMascota("1001", m3);
        sistema.registrarMascota("1002", m2);

        sistema.registrarControl("1001", "Firulais", new ControlVeterinario("2025-01-15", "VACUNACION", "Vacuna antirrábica"));
        sistema.registrarControl("1001", "Rocky", new ControlVeterinario("2025-02-10", "BAÑO", "Baño completo y corte de uñas"));
        sistema.registrarControl("1002", "Michi", new ControlVeterinario("2025-03-05", "DESPARACITACION", "Control interno y externo"));

        do {
            System.out.println("\n===== 🏥 SISTEMA DE SEGUIMIENTO VETERINARIO =====");
            System.out.println("1. Registrar dueño");
            System.out.println("2. Registrar mascota");
            System.out.println("3. Registrar control veterinario");
            System.out.println("4. Consultar historial de mascota");
            System.out.println("5. Listar dueños");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    registrarDueño(sc, sistema);
                    break;
                case 2:
                    registrarMascota(sc, sistema);
                    break;
                case 3:
                    registrarControl(sc, sistema);
                    break;
                case 4:
                    consultarHistorial(sc, sistema);
                    break;
                case 5:
                    sistema.listarDueños();
                    break;
                case 0:
                    System.out.println("👋 Saliendo del sistema...");
                    break;
                default:
                    System.out.println("❌ Opción inválida.");
            }
        } while (opcion != 0);

        sc.close();
    }

    private static void registrarDueño(Scanner sc, Veterinaria sistema) {
        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine();
        System.out.print("Documento: ");
        String doc = sc.nextLine();
        System.out.print("Teléfono: ");
        String tel = sc.nextLine();
        sistema.registrarDueño(new Dueño(nombre, doc, tel));
    }

    private static void registrarMascota(Scanner sc, Veterinaria sistema) {
        System.out.print("Documento del dueño: ");
        String docDueño = sc.nextLine();
        System.out.print("Nombre de la mascota: ");
        String nombreMascota = sc.nextLine();
        System.out.print("Especie: ");
        String especie = sc.nextLine();
        System.out.print("Edad: ");
        int edad = sc.nextInt();
        sc.nextLine();
        sistema.registrarMascota(docDueño, new Mascota(nombreMascota, especie, edad));
    }

    private static void registrarControl(Scanner sc, Veterinaria sistema) {
        System.out.print("Documento del dueño: ");
        String docD = sc.nextLine();
        System.out.print("Nombre de la mascota: ");
        String nomM = sc.nextLine();
        System.out.print("Fecha del control (AAAA-MM-DD): ");
        String fecha = sc.nextLine();

        System.out.println("Seleccione tipo de control:");
        System.out.println("1. VACUNACION");
        System.out.println("2. CHEQUEO");
        System.out.println("3. DESPARACITACION");
        System.out.println("4. CORTE DE PELO");
        System.out.println("5. BAÑO");
        System.out.print("Opción: ");
        int tipoNum = sc.nextInt();
        sc.nextLine();

        String tipoControl = switch (tipoNum) {
            case 1 -> "VACUNACION";
            case 2 -> "CHEQUEO";
            case 3 -> "DESPARACITACION";
            case 4 -> "CORTE DE PELO";
            case 5 -> "BAÑO";
            default -> "OTRO";
        };

        System.out.print("Observaciones: ");
        String obs = sc.nextLine();

        sistema.registrarControl(docD, nomM, new ControlVeterinario(fecha, tipoControl, obs));
    }

    private static void consultarHistorial(Scanner sc, Veterinaria sistema) {
        System.out.print("Documento del dueño: ");
        String docQ = sc.nextLine();
        System.out.print("Nombre de la mascota: ");
        String nomQ = sc.nextLine();
        sistema.mostrarHistorial(docQ, nomQ);
    }
}
