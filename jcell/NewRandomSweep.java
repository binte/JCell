
/**
 * @author Sergio Romero Leiva
 *
 * 
 * Follows the New Random Sweep policy for visiting the individuals
 * It is like fixed random sweep, but the sequence of visits is randomly generated every generation 
 * 
 */


package jcell;

import java.util.Random;
import java.awt.Point;

public class NewRandomSweep extends CellUpdate
{
   private int pos;
   private PermutationIndividual sweep;
   private Random r;
   
   public NewRandomSweep(Random r, PopGrid pop)
   {
      super(pop);
      
      this.r = r;      
      pos = 0;
      sweep = new PermutationIndividual(pop.getPopSize());
   }
   
   public Point nextCell()
   {
      Point cell;
      
      if (pos == 0)
      {
         for (int i=0; i<sweep.getLength(); i++)
            sweep.setIntegerAllele(i,i);
         sweep.setRandomValues(r);
      }
      
      cell = pop.toGrid(sweep.getIntegerAllele(pos));
      pos = (pos+1) % pop.getPopSize();
      
      return cell;
   }
}
