
/**
 * @author Sergio Romero Leiva
 * 
 * 
 * C13 Neighborhood implementation
 * 
 */

package jcell.neighborhoods;

import jcell.*;

public class Compact13 extends Neighborhood
{
   // This constructor defines the translation sequence to apply
   //to the considered position for getting the whole neighborhood
   public Compact13()
   {
      super(13); // Calls the base constructor to set the size
      moves[0].move(0,0);
      moves[1].move(1,0);
      moves[2].move(0,1);
      moves[3].move(-1,0);
      moves[4].move(0,-1);
      moves[5].move(1,1);
      moves[6].move(-1,1);
      moves[7].move(-1,-1);
      moves[8].move(1,-1);
      moves[9].move(0,2);
      moves[10].move(-2,0);
      moves[11].move(0,-2);
      moves[12].move(2,0);
   }
}
