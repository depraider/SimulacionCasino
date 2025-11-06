
/**
 * Clase que simula el giro de la ruleta e implementa Runnable
 * Actúa como monitor para sincronizar a los Jugadores
 */
public class Ruleta implements Runnable {
  private int ultimoNumeroGanador = -1;
  private final Banca banca;
  private volatile boolean simulacionActiva = true;

  public Ruleta(Banca banca) {
    this.banca = banca;
  }

  @Override
  public void run() {
    while (simulacionActiva) {
      try {
        // Fase de apuestas. Los jugadores ya están apostando y esperando
        System.out.println("Comienza una nueva rodna");

        // Esperar 3 segudnos
        // Esto permite que todos los hilos jugadores entren en wait() antes de que se
        // notifique
        Thread.sleep(3000);

        // Sacamos número al azar del 0 al 36
        this.ultimoNumeroGanador = (int) (Math.random() * 37);
        System.out.println("Crupier saca el número: " + ultimoNumeroGanador);

        // Notificar a los jugadores para que comprueben el resultado
        synchronized (this) {
          this.notifyAll();
        }

        // Pausa corta para asegurar que la mayoría de los jugadores hayan procesado el
        // resultado
        // antes de que el bucle comience a esperar el próximo giro
        Thread.sleep(100);
      } catch (InterruptedException e) {
        // Manejo de la interrupción del hilo
        Thread.currentThread().interrupt();
        simulacionActiva = false;
        System.out.println("La ruleta ha parado su actividad");
      }
    }
  }

  /**
   * @return El número que ha salido en el último giro de la ruleta
   */
  public synchronized int getUltimoNumeroGanador() {
    return ultimoNumeroGanador;
  }

  /**
   * Detiene el bucle principal de la simulación
   */
  public void detenerSimulacion() {
    this.simulacionActiva = false;
  }
}
