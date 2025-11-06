
/**
 * Implementa la estrategia Martingala: dobla la apuesta si pierde
 * Apuesta a un numero fijo (1-36)
 */
public class JugadorMartingala extends Jugador {
  private final int miNumero;

  public JugadorMartingala(String nombre, Banca banca, Ruleta ruleta) {
    super(nombre, banca, ruleta);
    // Elige un numero del 1 al 36 al azar y lo mantiene para toda la simulación
    this.miNumero = random.nextInt(36) + 1;
    this.numeroApostado = miNumero;
    this.apuestaActual = 10;
  }

  @Override
  protected void realizarApuestaEstrategia() {
    // La apuestaActual ya tiene el valor correcto de la ronda anterior
    // Solo necesitamos asegurar que no apuesta mas de lo que tiene
    if (this.apuestaActual > saldo) {
      // Si la apuesta duplicada excede el saldo, apostamos lo que queda
      this.apuestaActual = (int) saldo;
    }
  }

  @Override
  protected boolean haGanado(int numeroGanador) {
    // Gana si el numero ganador es el suyo
    return numeroGanador == miNumero;
  }

  @Override
  protected int getGananciaBruta() {
    // Gana 36 veces lo apostado, recuperando la apuesta actual
    return apuestaActual * 36;
  }

  /**
   * Resetea la apuesta a 10 € despues de una victoria.
   */
  public void resetApuesta() {
    this.apuestaActual = 10;
  }

  /**
   * Duplica la apuesta despues de una derrota.
   */
  public void duplicarApuesta() {
    this.apuestaActual *= 2;
  }
}
