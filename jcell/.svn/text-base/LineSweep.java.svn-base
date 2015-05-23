/**
 * @author Sergio Romero Leiva
 *
 * 
 * Follows the Line Sweep policy for visiting the individuals, i.e., 
 * individuals are visited in the order they are stored in the pop. 
 * 
 */
   
package jcell;

import java.awt.Point;

public class LineSweep extends CellUpdate
{
   private int pos;
   
   public LineSweep(PopGrid pop)
   {
      super(pop);
      pos = 0;
   }
   
   public Point nextCell()
   {
      Point cell = pop.toGrid(pos);
      
      pos = (pos+1) % pop.getPopSize();
      return cell;
   }
}
