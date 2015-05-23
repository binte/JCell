
/**
 * @author Sergio Romero Leiva
 *
 * Common interface for the statistical classes
 * 
 */


package jcell;

import java.util.HashMap;
import java.util.Map;

public interface Statistic
{
   public void calculate(Population pop); //Compute the statistical measures for 'pop'
   public Object getStat(int keyName); //Return a given statistic, specified by 'keyname'
   
   public void recordCharacteristic(Integer characteristic);
   
   Map characteristics = new HashMap();
}
