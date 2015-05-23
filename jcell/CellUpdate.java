/**
 * @author Sergio Romero Leiva
 * 
 * 
 * Base class for the asynchronous update policies
 * 
 */
   
package jcell;

import java.awt.Point;

public abstract class CellUpdate
{
   protected PopGrid pop; // Population
   
   public CellUpdate(PopGrid pop)
   {
      this.pop = pop;
   }
   
   // Returns the next cell for applying the breeding loop to it
   public abstract Point nextCell();
}
