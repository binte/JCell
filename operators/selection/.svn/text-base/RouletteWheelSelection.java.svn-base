
/**
 * @author Sergio Romero
 *
 * Roulette wheel selection operator. 
 * 
 */


package operators.selection;

import jcell.*;

import java.util.Random;

public class RouletteWheelSelection implements Operator
{
   private Random r;
   
   public RouletteWheelSelection(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
	   Individual iv[] = (Individual[])o;
      double sumFit, acFit, randValue;
      int i;
      
      sumFit = 0.0;
      for (i=0; i<iv.length; i++)
         sumFit += ((Double)iv[i].getFitness()).doubleValue(); //Fitness total
      
      i = 0;
      if (Target.maximize)
      {
      	acFit = ((Double)iv[i].getFitness()).doubleValue() / sumFit;
        randValue = r.nextDouble();
        while ((acFit < randValue) && (i<iv.length))
        {
           i++;
           acFit += ((Double)iv[i].getFitness()).doubleValue() / sumFit;
        }
      }
      else
      {
      	acFit = 1.0 - (((Double)iv[i].getFitness()).doubleValue() / sumFit);
        randValue = r.nextDouble();
        while ((acFit > randValue) && (i<iv.length))
        {
           i++;
           acFit -= ((Double)iv[i].getFitness()).doubleValue() / sumFit;
        }
      }      
      
      if (i==iv.length) return new Integer(r.nextInt(iv.length));
      return new Integer(i);
   }
}
