
/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 * 
 * L5 Neighborhood implementation
 * 
 */
   
package jcell.neighborhoods;

import jcell.*;
import java.awt.Point;

public class Linear5 extends Neighborhood
{
	// This constructor defines the translation sequence to apply
   //to the considered position for getting the whole neighborhood
   public Linear5(int x, int y)
   {
	   
	   if ((x==1) || (y==1)) size = 3;
	   else if ((x==2) || (y==2)) size = 4;
	   else size = 5;

   		moves = new Point[size];
   		neigh = new Point[size];

	      for (int i=0; i<size; i++)
      		{
         		moves[i] = new Point();
		         neigh[i] = new Point();
      		}


       int i=0;
	   moves[i].move(0,0); i++;
	   if (x>1) { moves[i].move(1,0); i++; } // It is not allowed the same individual to
	   if (y>1) { moves[i].move(0,1); i++; } //   be twice or more in the neighborhood
	   if (x>2) { moves[i].move(-1,0); i++; }
	   if (y>2) { moves[i].move(0,-1); i++; }
   }
}
