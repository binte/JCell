/**
 * @author Sergio Romero Leiva
 * 
 * Follows the Uniform Choice policy for visiting the individuals, i.e., 
 * the next individual to visit is randomly chosen with uniform probability 
 * 
 */

package jcell;

import java.util.Random;
import java.awt.Point;

public class UniformChoice extends CellUpdate
{
   private Random r;
   
   public UniformChoice(Random r, PopGrid pop)
   {
      super(pop);
      this.r = r;
   }
   
   public Point nextCell()
   {
      return pop.toGrid(r.nextInt(pop.getPopSize()));
   }
}
