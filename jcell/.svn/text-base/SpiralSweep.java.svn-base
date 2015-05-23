
/**
 * @author Bernabe Dorronsoro
 *
 * 
 * Performs a path through the hierarchical levels 
 *  of the population, from the lower to the upper levels,
 * 	following an spiral shape
 * 
 */


package jcell;

import java.awt.Point;

public class SpiralSweep extends CellUpdate
{
   private Point pos;
   private int level, movs;
   
   public SpiralSweep(PopGrid pop)
   {
      super(pop);
      pos = new Point(0,0);
      level = 0;
      movs = 0;
   }
   
   public Point nextCell()
   {
      Point cell = new Point(pos);
      
      if ((cell.getX() >= cell.getY()) && ((cell.getX() < pop.getDimX() - 1 - level)) && (cell.getY() == level))
      	pos.translate(1,0);
      else if ((cell.getX() == pop.getDimX() - 1 - level) && (cell.getY() < pop.getDimY() - 1 - level))
      	pos.translate(0,1);
      //else if ((cell.getY() == pop.getDimY() - 1 - level) && (cell.getX() > pop.getDimX() - 1 - cell.getY()))
      else if ((cell.getY() == pop.getDimY() - 1 - level) && (cell.getX() > level))
      	pos.translate(-1,0);
      else if ((cell.getX() == level) && (cell.getY()-1 > level))
   		pos.translate(0,-1);
      else if (((cell.getX() == level) && (cell.getY()-1 == level)))
      {
      	level++;
      	pos.translate(1,0);
      }
      
      movs++;
      if (movs == pop.getPopSize())
      {
      	level = 0;
      	pos.move(0,0);
      	movs=0;
      }

       return cell;
   }
}
