# Simulación de Estrategias de Ruleta Francesa

## Descripción del Proyecto

Este proyecto es una simulación de casino **multihilo** desarrollada en Java. El objetivo es simular el comportamiento y los posibles beneficios de diversas estrategias de juego en la **ruleta francesa** (37 números: 0-36).

La simulación se basa en la concurrencia:
* Un hilo actúa como el **Crupier/Ruleta**.
* Doce hilos representan a diferentes **Jugadores** (cuatro por estrategia).

---

## Conceptos de Concurrencia Implementados

* **Hilos (Threads/Runnable):** Las clases `Ruleta` y `Jugador` (y sus subclases) implementan `Runnable` para su ejecución concurrente.
* **Sincronización (`synchronized`):**
    * **Recurso Compartido (`Banca`):** Sus métodos de pago y cobro están protegidos con `synchronized` para garantizar la atomicidad de las transacciones.
    * **Coordinación (`wait()`/`notifyAll()`):** La `Ruleta` actúa como objeto monitor. Los jugadores usan `wait()` después de apostar y esperan el `notifyAll()` del Crupier que anuncia el número ganador, asegurando la sincronía entre rondas.

---

## Estructura del Código

El proyecto está diseñado con **modularidad** y herencia, utilizando las siguientes clases:

| Clase | Responsabilidad Principal | Tipo de Hilo |
| :--- | :--- | :--- |
| **`MainSimulacion`** | Configuración e inicio de la simulación. | No |
| **`Banca`** | Gestiona el saldo y las transacciones (Sincronizada). | Recurso Compartido |
| **`Ruleta`** | Hilo Crupier. Simula el giro cada 3000 ms y notifica el resultado. | `Runnable` (Monitor) |
| **`Jugador`** | Clase base abstracta. Define el ciclo de apuesta/espera y gestión de saldo. | `Runnable` (Abstracta) |
| **`JugadorNumero`** | Apuesta 10€ a un número fijo (1-36). Ganancia 36:1. | Extiende `Jugador` |
| **`JugadorParImpar`** | Apuesta 10€ a Par o Impar. Ganancia 2:1. | Extiende `Jugador` |
| **`JugadorMartingala`** | Dobla la apuesta si pierde y la resetea si gana. | Extiende `Jugador` |

---

## Cómo Ejecutar el Proyecto

1.  **Requisitos:** Necesitas tener instalado **Java JDK** (versión 8 o superior).
2.  **Compilación:** Desde el directorio que contiene los archivos `.java`, compila las clases:

    ```bash
    javac *.java
    ```

3.  **Ejecución:** Ejecuta la clase principal `MainSimulacion`:

    ```bash
    java MainSimulacion
    ```

El programa ejecutará la simulación mostrando en consola el flujo de apuestas, los resultados de la ruleta y los saldos finales de todos los participantes.

