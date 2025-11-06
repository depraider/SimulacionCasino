
/**
 * Implementa la estrategia de apostar a un numero fijo al azar (1-36)
 */
public class JugadorNumero extends Jugador {
  private final int miNumero;
  private static final int GANANCIA_NUMERO = 350;

  public JugadorNumero(String nombre, Banca banca, Ruleta ruleta) {
    super(nombre, banca, ruleta);
    // Elige un numero del 1 al 36 al azar y lo mantiene para toda la simulacion
    this.miNumero = random.nextInt(36) + 1;
    this.numeroApostado = miNumero;
  }

  @Override
  protected void realizarApuestaEstrategia() {
    // Apuesta fija de 10 euros a su numero
    this.apuestaActual = 10;
  }

  @Override
  protected boolean haGanado(int numeroGanador) {
    // Gana si el numero ganador es el suyo
    return numeroGanador == miNumero;
  }

  @Override
  protected int getGananciaBruta() {
    // Premio: 36 veces lo apostado, por lo que la ganancia es 35x la apuesta (360 -
    // 10)
    return apuestaActual * 36; // 360 euros de premio bruto
  }
}
