
/**
 * Implementa la estrategia de apostar a Par o Impar
 */
public class JugadorParImpar extends Jugador {
  private final boolean apuestaPar; // true = Par, false = Impar
  private static final int GANANCIA_PARIMPAR = 20;

  public JugadorParImpar(String nombre, Banca banca, Ruleta ruleta) {
    super(nombre, banca, ruleta);
    // Elige Par o Impar al azar y lo mantiene
    this.apuestaPar = random.nextBoolean();
    this.numeroApostado = apuestaPar ? 2 : 1; // 2 para Par, 1 para Impar
  }

  @Override
  protected void realizarApuestaEstrategia() {
    // Apuesta fija de 10 euros
    this.apuestaActual = 10;
  }

  @Override
  protected boolean haGanado(int numeroGanador) {
    // Gana si no es 0 y cumple la condición Par/Impar
    if (numeroGanador == 0)
      return false;

    boolean esPar = (numeroGanador % 2 == 0);
    return esPar == apuestaPar;
  }

  @Override
  protected int getGananciaBruta() {
    // Premio 2 veces lo apostado
    return apuestaActual * 2;
  }
}
