/**
 * @author Bernabe Dorronsoro
 *
 * Quickk sort algorithm
 * 
 */

package tools;

import jcell.*;

public class QuickSort
{

	public static int length = 0;
	/** Sort the entire vector, if it is not empty
	 * from low to high fitness value
	 */
	  public static void quickSort(Individual[] elements, int objective, int numStoredSols)
	  { 
	  	if (numStoredSols > 1)
	  		quickSort(elements, 0, numStoredSols-1, objective);
	  	length = numStoredSols;
	  }
	  
	  // For the single-objective case
	  public static void quickSort(Individual[] elements, int numStoredSols)
	  { 
	  	if (numStoredSols > 1)
	  		quickSort2(elements, 0, numStoredSols-1);
	  	length = numStoredSols;
	  }


	/**
	 * Quicksort algorithm, sorts (part of) a Population by
	 *  1.  Choose a pivot, an element used for comparison
	 *  2.  dividing into two parts:
	 *      - less than-equal pivot
	 *      - and greater than-equal to pivot.
	 *      A element that is equal to the pivot may end up in any part.
	 *  3. Sort the parts recursively until there is only one element left.
	 *
	 */
	  
	  private static void quickSort(Individual[] elements, int lowIndex, int highIndex, int obj)
	  { int lowToHighIndex;
	    int highToLowIndex;
	    int pivotIndex;
	    double pivotValue;
	    double lowToHighValue;
	    double highToLowValue;
	    Individual parking;
	    int newLowIndex;
	    int newHighIndex;
	    //int compareResult;

	    lowToHighIndex = lowIndex;
	    highToLowIndex = highIndex;
	    /** Choose a pivot, remember it's value
	     *  No special action for the pivot element itself.
	     *  It will be treated just like any other element.
	     */
	    pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
	    pivotValue = ((Double[])((Individual)elements[pivotIndex]).getFitnessValues())[obj].doubleValue();

	    /** Split the Vector in two parts.
	     *
	     *  The lower part will be lowIndex - newHighIndex,
	     *  containing elements <= pivot Value
	     *
	     *  The higher part will be newLowIndex - highIndex,
	     *  containting elements >= pivot Value
	     */
	    newLowIndex = highIndex + 1;
	    newHighIndex = lowIndex - 1;
	    // loop until low meets high
	    while ((newHighIndex + 1) < newLowIndex) // loop until partition complete
	    { // loop from low to high to find a candidate for swapping
	      lowToHighValue = ((Double[])((Individual)elements[lowToHighIndex]).getFitnessValues())[obj].doubleValue();
	      while ((lowToHighIndex < newLowIndex) && (lowToHighValue < pivotValue ))
	      { newHighIndex = lowToHighIndex; // add element to lower part
	        lowToHighIndex ++;
	        lowToHighValue = ((Double[])((Individual)elements[lowToHighIndex]).getFitnessValues())[obj].doubleValue();
	      }

	      // loop from high to low find other candidate for swapping
	      highToLowValue = ((Double[])((Individual)elements[highToLowIndex]).getFitnessValues())[obj].doubleValue();
	      while ((newHighIndex <= highToLowIndex) && (highToLowValue > pivotValue))
	      { newLowIndex = highToLowIndex; // add element to higher part
	        highToLowIndex --;
	        highToLowValue = ((Double[])((Individual)elements[highToLowIndex]).getFitnessValues())[obj].doubleValue();
	      }

	      // swap if needed
	      if (lowToHighIndex == highToLowIndex) // one last element, may go in either part
	      { newHighIndex = lowToHighIndex; // move element arbitrary to lower part
	      }
	      else if (lowToHighIndex < highToLowIndex) // not last element yet
	      { 
	      	if (lowToHighValue >= highToLowValue)
	        { 
	      	  parking = elements[lowToHighIndex];
	          elements[lowToHighIndex] = elements[highToLowIndex];
	          elements[highToLowIndex] = parking;

	          newLowIndex = highToLowIndex;
	          newHighIndex = lowToHighIndex;

	          lowToHighIndex ++;
	          highToLowIndex --;
	        }
	      }
	    }

	    // Continue recursion for parts that have more than one element
	    if (lowIndex < newHighIndex)
	    	quickSort(elements, lowIndex, newHighIndex, obj); // sort lower subpart
	    
	    if (newLowIndex < highIndex)
	     quickSort(elements, newLowIndex, highIndex, obj); // sort higher subpart
	    
	  }

	  private static void quickSort2(Individual[] elements, int lowIndex, int highIndex)
	  { int lowToHighIndex;
	    int highToLowIndex;
	    int pivotIndex;
	    double pivotValue;
	    double lowToHighValue;
	    double highToLowValue;
	    Individual parking;
	    int newLowIndex;
	    int newHighIndex;
	    //int compareResult;

	    lowToHighIndex = lowIndex;
	    highToLowIndex = highIndex;
	    /** Choose a pivot, remember it's value
	     *  No special action for the pivot element itself.
	     *  It will be treated just like any other element.
	     */
	    pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
	    pivotValue = ((Double)((Individual)elements[pivotIndex]).getFitness()).doubleValue();

	    /** Split the Vector in two parts.
	     *
	     *  The lower part will be lowIndex - newHighIndex,
	     *  containing elements <= pivot Value
	     *
	     *  The higher part will be newLowIndex - highIndex,
	     *  containting elements >= pivot Value
	     */
	    newLowIndex = highIndex + 1;
	    newHighIndex = lowIndex - 1;
	    // loop until low meets high
	    while ((newHighIndex + 1) < newLowIndex) // loop until partition complete
	    { // loop from low to high to find a candidate for swapping
	      lowToHighValue = ((Double)((Individual)elements[lowToHighIndex]).getFitness()).doubleValue();
	      while ((lowToHighIndex < newLowIndex) && (lowToHighValue < pivotValue ))
	      { newHighIndex = lowToHighIndex; // add element to lower part
	        lowToHighIndex ++;
	        lowToHighValue = ((Double)((Individual)elements[lowToHighIndex]).getFitness()).doubleValue();
	      }

	      // loop from high to low find other candidate for swapping
	      highToLowValue = ((Double)((Individual)elements[highToLowIndex]).getFitness()).doubleValue();
	      while ((newHighIndex <= highToLowIndex) && (highToLowValue > pivotValue))
	      { newLowIndex = highToLowIndex; // add element to higher part
	        highToLowIndex --;
	        highToLowValue = ((Double)((Individual)elements[highToLowIndex]).getFitness()).doubleValue();
	      }

	      // swap if needed
	      if (lowToHighIndex == highToLowIndex) // one last element, may go in either part
	      { newHighIndex = lowToHighIndex; // move element arbitrary to lower part
	      }
	      else if (lowToHighIndex < highToLowIndex) // not last element yet
	      { 
	      	if (lowToHighValue >= highToLowValue)
	        { 
	      	  parking = elements[lowToHighIndex];
	          elements[lowToHighIndex] = elements[highToLowIndex];
	          elements[highToLowIndex] = parking;

	          newLowIndex = highToLowIndex;
	          newHighIndex = lowToHighIndex;

	          lowToHighIndex ++;
	          highToLowIndex --;
	        }
	      }
	    }

	    // Continue recursion for parts that have more than one element
	    if (lowIndex < newHighIndex)
	    	quickSort2(elements, lowIndex, newHighIndex); // sort lower subpart
	    
	    if (newLowIndex < highIndex)
	     quickSort2(elements, newLowIndex, highIndex); // sort higher subpart
	    
	  }

	/**
	 * SUMit MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
	 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
	 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
	 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUMit SHALL NOT BE LIABLE FOR
	 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
	 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
	 *
	 * THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
	 * CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
	 * PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
	 * NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
	 * SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
	 * SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
	 * PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES").  SUMit
	 * SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
	 * HIGH RISK ACTIVITIES.
	 */
	  
}