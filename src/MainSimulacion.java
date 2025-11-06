import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que configura e inicia la simulacion del casino
 */
public class MainSimulacion {
  private static final int NUM_JUGADORES_POR_TIPO = 4;
  private static final int DURACION_SIMULACION_SEGUNDOS = 30; // Limite de tiempo para la simulación

  public static void main(String[] args) throws InterruptedException {
    Banca banca = new Banca();
    Ruleta ruletaCrupier = new Ruleta(banca);
    Thread hiloCrupier = new Thread(ruletaCrupier);

    List<Jugador> jugadores = new ArrayList<>();
    List<Thread> hilosJugadores = new ArrayList<>();

    // Crear 4 hilos JugadorNumero
    for (int i = 1; i <= NUM_JUGADORES_POR_TIPO; i++) {
      JugadorNumero j = new JugadorNumero("Numero-" + i, banca, ruletaCrupier);
      jugadores.add(j);
      hilosJugadores.add(new Thread(j));
    }

    // Crear 4 hilos JugadorParImpar
    for (int i = 1; i <= NUM_JUGADORES_POR_TIPO; i++) {
      JugadorParImpar j = new JugadorParImpar("ParImpar-" + i, banca, ruletaCrupier);
      jugadores.add(j);
      hilosJugadores.add(new Thread(j));
    }

    // Crear 4 hilos JugadorMartingala
    for (int i = 1; i <= NUM_JUGADORES_POR_TIPO; i++) {
      JugadorMartingala j = new JugadorMartingala("Martingala-" + i, banca, ruletaCrupier);
      jugadores.add(j);
      hilosJugadores.add(new Thread(j));
    }

    System.out.println("Iniciando simulación de casino");
    System.out.println("Banca inicial: " + banca.getSaldo() + " €");
    System.out.println("Jugadores inicial: 1000 € cada uno");

    // Iniciar todos los hilos
    hiloCrupier.start();
    for (Thread hilo : hilosJugadores) {
      hilo.start();
    }

    // Usamos un sleep en el hilo principal para limitar la duracion
    Thread.sleep(DURACION_SIMULACION_SEGUNDOS * 1000);

    // Finalizar la simulacion y esperar a que todos terminen
    ruletaCrupier.detenerSimulacion();
    hiloCrupier.interrupt();

    System.out.println("\n Tiempo de simulacion agotado. esperando cierre de jugadores");

    // Esperar a que todos los hilos terminen
    for (Thread hilo : hilosJugadores) {
      hilo.join(100);
    }

    // Mostrar resultados finales
    System.out.println("\n Resultados finales de la simulacion");
    System.out.println("Saldo final de la Banca: " + banca.getSaldo() + " €");

    long saldoTotalJugadores = 0;
    for (Jugador j : jugadores) {
      System.out.printf("[%s] Saldo final: %d €\n", j.nombre, j.getSaldo());
      saldoTotalJugadores += j.getSaldo();
    }
    System.out.println("Saldo total final de Jugadores: " + saldoTotalJugadores + " €");
  }
}
