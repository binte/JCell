/**
 * @author Bernabe Dorronsoro
 *
 * Asynchronous updating of the hierarchy using a fized random sweep
 */
   
package HcGA;   

import jcell.*;

import java.awt.Point;

public class LineCenterLRSweep extends CellUpdate

{
	int counter=0;
   private int pos;
   private PermutationIndividual sweep;
   //alternate the direction with which the central column is compared
   //only required for odd number of columns
   boolean correctionStep=false;
   
   
   public LineCenterLRSweep(PopGrid pop)
   {
      super(pop);
	  pos=0;
	  counter=0;
   }
   
   public Point nextCell()
   {
	   
	   //required for odd number of columns (DimX)
	   if (pop.getDimX()%2 == 1)
		   correctionStep = (counter/pop.getPopSize()==1);
	   
	   int cellPos = 0;
	   if (pos%pop.getDimX() < (pop.getDimX()/2.0 - (correctionStep?1:0)))
		   //left half
		   cellPos = (pos/pop.getDimX())*pop.getDimX() + (int)Math.ceil(pop.getDimX()/2.0) - pos%pop.getDimX() - (correctionStep?2:1);
	   else
		   cellPos = pos/pop.getDimX()*pop.getDimX() + pos%pop.getDimX();
	   
	  
      Point cell = pop.toGrid(cellPos);
      
      pos = (pos+1) % pop.getPopSize();
	   counter = (counter+1) % (2*pop.getPopSize());
      return cell;
   }
}
