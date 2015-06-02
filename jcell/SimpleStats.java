/* -----------------------------------------------------------
   Fichero: SimpleStats.java
   Autor:   Sergio Romero Leiva
   Descripcion
   Calcula valor y posicion de individuos con fitness maximo y
   minimo y la media del fitness de la poblacion
   -----------------------------------------------------------*/

/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 * 
 * It computes some sttistics of the population, such as the mean fitness value, 
 * and the maximum and minimum fitness values
 * 
 */


package jcell;

public class SimpleStats implements Statistic
{
   public static final int MAX_FIT_VALUE = 0; 
   public static final int MIN_FIT_VALUE = 1;
   public static final int MAX_FIT_POS   = 2;
   public static final int MIN_FIT_POS   = 3;
   public static final int AVG_FIT       = 4;
   
   private double maxFitValue;
   private double minFitValue;
   private double avgFit;
   private int maxFitPos;
   private int minFitPos;
   
   public Object getStat(int keyName)
   {
      switch (keyName)
      {
         case MAX_FIT_VALUE: return new Double(maxFitValue);
         case MIN_FIT_VALUE: return new Double(minFitValue);
         case MAX_FIT_POS:   return new Integer(maxFitPos);
         case MIN_FIT_POS:   return new Integer(minFitPos);
         case AVG_FIT:       return new Double(avgFit);
         default:            return null;
      }
   }
   
   public void calculate(Population pop)
   {
	   
      double auxFit, sumFit;
      int popSize = pop.getPopSize();
      
      maxFitPos = 0;
      minFitPos = 0;
      maxFitValue = ((Double)pop.getIndividual(0).getFitness()).doubleValue();
      minFitValue = ((Double)pop.getIndividual(0).getFitness()).doubleValue();
      sumFit = 0.0;
      
      for (int i=0; i<popSize; i++)
      {
         auxFit = ((Double)pop.getIndividual(i).getFitness()).doubleValue();
         sumFit += auxFit;
         if (auxFit > maxFitValue)
         {
            maxFitValue = auxFit;
            maxFitPos = i;
         }
         if (auxFit < minFitValue)
         {
            minFitValue = auxFit;
            minFitPos = i;
         }
      }
      avgFit = sumFit / popSize;
   }
   
   public String toString()
   {
      return ""+maxFitValue+" "+avgFit;
   }
   
   public void recordCharacteristic(Integer characteristic) 
   {
	  if (!characteristics.containsKey(characteristic)) {
		  //create the vector for this characteristic
		  //for recording all data
		  //characteristics.put(characteristic, new ArrayList());
		  characteristics.put(characteristic, new Double(0.0));
	  }
	  
	  Double characteristicValue = (Double) characteristics.get(characteristic);
	  
	  /*if (characteristicList.size() <= step)
		  characteristicList.add(new Double(1));
	  else {*/
	  double val = characteristicValue.doubleValue();
	  val++;
	  characteristicValue = new Double(val);
	  characteristics.put(characteristic, characteristicValue);
	  //}
   }
}
