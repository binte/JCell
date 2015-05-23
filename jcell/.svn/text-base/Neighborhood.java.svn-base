/**
 * @author Bernabe Dorronsoro
 *
 * 
 * Class for implementing neighborhoods 
 * 
 */

package jcell;

import java.awt.Point;

public class Neighborhood
{
   protected Point moves[]; // Movements from the central point to get the neighbors
   protected Point neigh[]; // Neighbors of a given individual
   protected int size; // Neighborhood size

   public Neighborhood()
   {
      size = 0;
   }

   public Neighborhood(int size)
   {
      this.size = size;
      moves = new Point[size];
      neigh = new Point[size];

      for (int i=0; i<size; i++)
      {
         moves[i] = new Point();
         neigh[i] = new Point();
      }
   }

	public void lowSize(int size)
	{
		if (size<this.size)
		{
			this.size = size;
			moves = new Point[size];
			neigh = new Point[size];

			for (int i=0; i<size; i++)
			{
				moves[i] = new Point();
				neigh[i] = new Point();
			}
		
		}
		System.gc();
	}

   public int getNeighSize()
   {
      return size;
   }
   
   public Point[] getNeighbors(Point cell)
   {
      for (int i=0; i<size; i++)
      {
         neigh[i].setLocation(cell); //Asigna punto
         neigh[i].translate(moves[i].x,moves[i].y); //Mueve punto
      }
      
      return neigh;
   }
}
