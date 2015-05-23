/* ---------------------------------------------------------------
   Fichero: GenerationListener.java
   Autor:   Sergio Romero Leiva
   Descripcion
   Interfaz que implementara nuestra aplicacion para obtener datos
   de cada generacion
   ---------------------------------------------------------------*/

/**
 * @author Sergio Romero Leiva
 *
 * 
 * Interface that allows us monitoring the search after each generation 
 * 
 */

package jcell;

public interface GenerationListener
{
   public void generation(EvolutionaryAlg EA);
}
