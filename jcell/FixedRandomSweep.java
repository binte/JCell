/**
 * @author Sergio Romero Leiva
 *
 * 
 * Follows the Fixed Random Sweep policy for visiting the individuals 
 * 
 */

package jcell;

import java.util.Random;
import java.awt.Point;

public class FixedRandomSweep extends CellUpdate
{
   private int pos;
   private PermutationIndividual sweep;
   private Random r;
   
   public FixedRandomSweep(Random r, PopGrid pop)
   {
      super(pop);
      this.r = r;
      
      pos = 0;
      sweep = new PermutationIndividual(pop.getPopSize());
      sweep.setRandomValues(r);
   }
   
   public Point nextCell()
   {
      Point cell = pop.toGrid(sweep.getIntegerAllele(pos));
      
      pos = (pos+1) % pop.getPopSize();
      return cell;
   }
}
