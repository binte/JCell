
/**
 * @author Bernabe Dorronsoro
 *
 * Asynchronous updating of the hierarchy using a fized random sweep
 */
   
   
package HcGA; 

import jcell.*;

//import java.util.Random;
import java.awt.Point;

public class LineCenterUDSweep extends CellUpdate
{
   private int pos;
   private PermutationIndividual sweep;
   int counter=0;
   //alternate the direction with which the central column is compared
   //only required for odd number of columns
   boolean correctionStep=false;
   
   
   public LineCenterUDSweep(PopGrid pop)
   {
      super(pop);
	  pos=0;
	  counter=0;
	}
   
   
   public Point nextCell()
   {
	   
	   //required for odd number of rows (DimY)
	   if (pop.getDimY()%2 == 1)
		   correctionStep = (counter/pop.getPopSize()==1);
	   
	   int cellPos = 0;
	   if (pos%pop.getDimY() < (pop.getDimY()/2.0 - (correctionStep?1:0))) {
		   //left half
		   int rowIndex = (pos%pop.getDimY())*pop.getDimX();
		   cellPos = (((int)Math.ceil(pop.getDimY()/2.0)) - pos%pop.getDimY() - (correctionStep?2:1))*pop.getDimX() + pos/pop.getDimY() ;
	   }
	   else
		   //row number times columns              + offset for each column
		   cellPos = (pos%pop.getDimY())*pop.getDimX() + pos/pop.getDimY();
	   
	  
      Point cell = pop.toGrid(cellPos);
      
      pos = (pos+1) % pop.getPopSize();
	   counter = (counter+1) % (2*pop.getPopSize());
      return cell;
   }
   
   public Point nextCell_old()
   {
	   int cellPos = 0;
	   //upper half
	   Point cell = new Point();
	   int yhalf = (pop.getDimY()/2);
	   cell.x = (pos/yhalf)%pop.getDimX();
	   if (pos/pop.getDimX() < yhalf) {
		   //cellPos = (pos/(pop.getDimY()/2))*(pop.getDimY()/2) + pop.getDimX()/2 - pos%pop.getDimX()-1;
		   cell.y = (yhalf-pos%yhalf-1);
	   }
	   else {
		   //cellPos = pos/pop.getDimX()*pop.getDimX() + pos%pop.getDimX();
		   cell.y = (pos%yhalf)+yhalf;
	   }
	   
	  
      //Point cell = pop.toGrid(cellPos);
      
      pos = (pos+1) % pop.getPopSize();
      return cell;
   }
}
