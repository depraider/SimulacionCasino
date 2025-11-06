
/**
 * Clase que gestiona el saldo total de la Banca.
 * Es un recurso compartido y todas las operaciones
 * de modificacion de saldo deben ser sincronizadas
 */
public class Banca {
  private long saldo = 50000;

  /**
   * Intenta cobrar una apuesta a un jugador y agrega el monto al saldo de la
   * Banca
   *
   * @param monto la cantidad apostada por el jugador
   */
  public synchronized void cobrarApuesta(int monto) {
    this.saldo += monto;
  }

  /**
   * Intenta pagar un premio a un jugador, restando el monto del saldo de la
   * Banca
   * Si no tiene suficiente, paga lo que le queda
   *
   * @param premioSolicitado el premio que el jugador ha ganado.
   * @return el monto real pagado por la Banca
   */
  public synchronized int pagarPremio(int premioSolicitado) {
    if (this.saldo >= premioSolicitado) {
      this.saldo -= premioSolicitado;
      return premioSolicitado;
    } else {
      int pagoReal = (int) this.saldo;
      this.saldo = 0;
      System.out.println("¡ATENCIÓN! La Banca se queda sin fondos. Pago real: " + pagoReal);
      return pagoReal;
    }
  }

  /**
   * @return El saldo actual de la Banca
   */
  public synchronized long getSaldo() {
    return this.saldo;
  }
}
