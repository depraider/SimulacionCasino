import java.util.Random;

/**
 * Clase abstracta base para todos los hilos jugadores
 * Define la logica comun de gestion de saldo y sincronizacion con la Ruleta
 */
public abstract class Jugador implements Runnable {

  protected static final int SALDO_INICIAL = 1000;
  protected long saldo;
  protected final String nombre;
  protected final Banca banca;
  protected final Ruleta ruleta;
  protected int apuestaActual; // Monto de la apuesta para la ronda actual
  protected int numeroApostado; // Numero o estrategia apostada (1-36, Par/Impar)

  // Objeto Random para las decisiones aleatorias de los jugadores
  protected static final Random random = new Random();

  public Jugador(String nombre, Banca banca, Ruleta ruleta) {
    this.nombre = nombre;
    this.saldo = SALDO_INICIAL;
    this.banca = banca;
    this.ruleta = ruleta;
    this.apuestaActual = 10;
  }

  // Metodo abstracto que implementara la logica de cada estrategia (martingala,
  // numero, etc.)
  protected abstract void realizarApuestaEstrategia();

  // Metodo abstracto para determinar si el jugador gana segun el numero ganador
  protected abstract boolean haGanado(int numeroGanador);

  // Metodo abstracto para devolver la ganancia bruta al ganar
  protected abstract int getGananciaBruta();

  @Override
  public void run() {
    while (saldo > 0 && banca.getSaldo() > 0) {

      // Fase de Apuesta y Preparacion
      try {
        // La sincronizacion es crucial:
        // Aseguramos que solo un jugador a la vez manipule su estado
        // y se prepare para la siguiente ronda.
        synchronized (ruleta) {

          // Logica de la estrategia (doblar apuesta, elegir numero, etc.)
          realizarApuestaEstrategia();

          // Si tiene saldo, resta la apuesta y notifica a la banca
          if (saldo >= apuestaActual) {
            saldo -= apuestaActual;
            banca.cobrarApuesta(apuestaActual);

            System.out.printf("[%s] apuesta %d € al número/estrategia %d. Saldo restante: %d.\n",
                nombre, apuestaActual, numeroApostado, saldo);

            // Espera a que la Ruleta anuncie el resultado (el monitor)
            ruleta.wait();

          } else {
            System.out.printf("[%s] Sin suficiente saldo para apostar %d. Termina el juego.\n", nombre, apuestaActual);
            break; // Sale del bucle si no hay saldo
          }
        }

        // 2. Fase de Comprobacion y Pago
        int numeroGanador = ruleta.getUltimoNumeroGanador();

        if (numeroGanador != -1) {
          if (numeroGanador == 0) {
            // El 0 hace perder a todo el mundo.
            System.out.printf("[%s] Pierde. Salió el 0. \n", nombre);
            // No hace falta actualizar la banca, ya ha cobrado la apuesta.

          } else if (haGanado(numeroGanador)) {
            int premioBruto = getGananciaBruta();
            int premioNeto = premioBruto + apuestaActual; // Sumamos la apuesta que recupera

            // La banca paga, el metodo ya gestiona si tiene o no suficiente
            int pagoReal = banca.pagarPremio(premioBruto);

            // Actualiza el saldo del jugador (Premio real + su apuesta que recupera)
            saldo += pagoReal + apuestaActual;

            System.out.printf("[%s] Salió el %d. Recibe %d €. Saldo: %d.\n",
                nombre, numeroGanador, pagoReal + apuestaActual, saldo);

            // Para Martingala: Reinicia la apuesta si gana.
            if (this instanceof JugadorMartingala) {
              ((JugadorMartingala) this).resetApuesta();
            }

          } else {
            // Perdedor (no sale el 0, pero tampoco acierta su apuesta)
            System.out.printf("[%s] Pierde. Salió el %d. \n", nombre, numeroGanador);
            // No hace falta actualizar la banca, ya ha cobrado la apuesta.

            // Para Martingala: Duplica la apuesta si pierde.
            if (this instanceof JugadorMartingala) {
              ((JugadorMartingala) this).duplicarApuesta();
            }
          }
        }

      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    System.out.printf("[%s] HA TERMINADO EL JUEGO. Saldo final: %d €.\n", nombre, saldo);
  }

  public long getSaldo() {
    return saldo;
  }
}
