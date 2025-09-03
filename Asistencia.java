import java.util.Scanner;

public class Asistencia {
    public static final int DIAS_SEMANA = 5;
    public static final int NUM_ESTUDIANTES = 4;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[][] asistencia = new char[NUM_ESTUDIANTES][DIAS_SEMANA];
        String[] nombresEstudiantes = new String[NUM_ESTUDIANTES];
        int opcion; /*valor para determinar la accion en el menu*/
        
        /*Se registra el nombre de 4 estudiantes*/
        
        System.out.println("--- Registro de Nombres de Estudiantes ---");
        for(int i = 0; i < NUM_ESTUDIANTES; i++) {
            System.out.print("Ingrese el nombre del estudiante " + (i + 1) + ": ");
            nombresEstudiantes[i] = scanner.nextLine();
        }
        
        do {
        	System.out.println("");
            System.out.println("--- Menú de Control de Asistencia ---");
            System.out.println("1.       Registrar asistencia");
            System.out.println("2.     Ver asistencia individual");
            System.out.println("3.        Ver resumen general");
            System.out.println("4.                Salir");
            System.out.println("-------------------------------------");
            System.out.println("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); 
            
            switch(opcion) {
                case 1:
                  registrarAsistencia(asistencia, nombresEstudiantes, scanner);
                  break;
                case 2:
                    verAsistenciaIndividual(asistencia, nombresEstudiantes, scanner);
                    break;
                case 3:
                    verResumenGeneral(asistencia, nombresEstudiantes);
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while(opcion != 4);
        
        scanner.close();
    }
    
    /*Resgistro de la asistencia de cada estudiante para los 5 dias*/
    
    public static void registrarAsistencia(char[][] asistencia, String[] nombres, Scanner scanner) {
        System.out.println("--- Registro de Asistencia ---");
        for(int i = 0; i < NUM_ESTUDIANTES; i++) {
            System.out.println("Estudiante: " + nombres[i]);
            for(int j = 0; j < DIAS_SEMANA; j++) {
                char valor;
                do {
                    System.out.print("Día " + (j + 1) + " (P/A): ");
                    valor = scanner.next().toUpperCase().charAt(0);
                    if(valor != 'P' && valor != 'A') {
                        System.out.println("Error: Ingrese solo 'P' o 'A'.");
                    }
                } while(valor != 'P' && valor != 'A');
                asistencia[i][j] = valor;
            }
        }
        System.out.println("Asistencia registrada exitosamente.");
    }
    
    public static void verAsistenciaIndividual(char[][] asistencia, String[] nombres, Scanner scanner) {
        System.out.println("--- Lista de Estudiantes ---");
        for(int i = 0; i < NUM_ESTUDIANTES; i++) {
            System.out.println((i + 1) + ". " + nombres[i]);
        }
        
        System.out.print("Seleccione el número de estudiante (1-" + NUM_ESTUDIANTES + "): ");
        int estudiante = scanner.nextInt() - 1;
        
        if(estudiante < 0 || estudiante >= NUM_ESTUDIANTES) {
            System.out.println("Número de estudiante inválido.");
            return;
        }
        
        System.out.println("Asistencia de " + nombres[estudiante] + ":");
        for(int j = 0; j < DIAS_SEMANA; j++) {
            System.out.println("Día " + (j + 1) + ": " + asistencia[estudiante][j]);
        }
        
        int totalAsistencias = 0;
        for(int j = 0; j < DIAS_SEMANA; j++) {
            if(asistencia[estudiante][j] == 'P') {
                totalAsistencias++;
            }
        }
        System.out.println("Total de asistencias: " + totalAsistencias + "/" + DIAS_SEMANA);
    }
    
    /*Se suman puntos en asistencias y ausenias por dia para el resumen*/
    
    public static void verResumenGeneral(char[][] asistencia, String[] nombres) {
        int[] asistenciasPorEstudiante = new int[NUM_ESTUDIANTES];
        int[] ausenciasPorDia = new int[DIAS_SEMANA];
        
        for(int i = 0; i < NUM_ESTUDIANTES; i++) {
            for(int j = 0; j < DIAS_SEMANA; j++) {
                if(asistencia[i][j] == 'P') {
                    asistenciasPorEstudiante[i]++;
                } else {
                    ausenciasPorDia[j]++;
                }
            }
        }
        
        System.out.println("--- Total de Asistencias por Estudiante ---");
        for(int i = 0; i < NUM_ESTUDIANTES; i++) {
            System.out.println(nombres[i] + ": " + asistenciasPorEstudiante[i] + "/" + DIAS_SEMANA);
        }
        
        System.out.println("--- Estudiantes con Asistencia Perfecta ---");
        boolean hayPerfectos = false;
        for(int i = 0; i < NUM_ESTUDIANTES; i++) {
            if(asistenciasPorEstudiante[i] == DIAS_SEMANA) {
                System.out.println(nombres[i]);
                hayPerfectos = true;
            }
        }
        if(!hayPerfectos) System.out.println("No hay estudiantes con asistencia perfecta.");
        
        System.out.println("--- Días con Mayor Número de Ausencias ---");
        int maxAusencias = 0;
        for(int j = 0; j < DIAS_SEMANA; j++) {
            if(ausenciasPorDia[j] > maxAusencias) {
                maxAusencias = ausenciasPorDia[j];
            }
        }
        
        /*se compara las ausencias por dia con el maximo de ausencias para determinar el dia
         * con mas ausencias*/
        if(maxAusencias == 0) {
            System.out.println("No hubo ausencias en ningún día.");
        } else {
            for(int j = 0; j < DIAS_SEMANA; j++) {
                if(ausenciasPorDia[j] == maxAusencias) {
                    System.out.println("Día " + (j + 1) + " con " + maxAusencias + " ausencias");
                }
            }
        }
    }
}

/*David Alejandro Luna Martínez*/