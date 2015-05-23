/**
 * @author Bernabe Dorronsoro
 *
 * Some functions for computing statistics 
 * 
 */

package tools;

import java.util.Vector;

public class Stats
{ 

/** 
* Calculates the standard deviation of an array 
* of numbers. 
* see http://davidmlane.com/hyperstat/A16252.html 
* 
* @param data Numbers to compute the standard deviation of. 
* Array must contain two or more numbers. 
* @return standard deviation estimate of population 
* ( to get estimate of sample, use n instead of n-1 in last line ) 
*/ 
	public static double sdFast ( Vector data ) 
	{ 
		// sd is sqrt of sum of (values-mean) squared divided by n - 1 
		// Calculate the mean 
		double mean = 0; 
		final int n = data.size(); 
		if ( n < 2 ) 
			return Double.NaN; 
		for ( int i=0 ; i<n ; i++ ) 
		{ 
			mean += ((Double)data.get( i )).doubleValue() ; 
		} 
		mean /= n ; 
		
		// calculate the sum of squares 
		double sum = 0; 
		for ( int i=0 ; i<n ; i++ ) 
		{ 
			final double v = ((Double)data.get( i )).doubleValue() - mean; 
			sum += v * v; 
		} 
		return Math.sqrt (sum / ( n - 1 )); 
	} 
	/** 
	* Calculates the standard deviation of an array 
	* of numbers. 
	* see Knuth's The Art Of Computer Programming 
	* Volume II: Seminumerical Algorithms 
	* This algorithm is slower, but more resistant to error propagation. 
	* 
	* @param data Numbers to compute the standard deviation of. 
	* Array must contain two or more numbers. 
	* @return standard deviation estimate of population 
	* ( to get estimate of sample, use n instead of n-1 in last line ) 
	*/ 
	public static double sdKnuth ( Vector data ) 
	{ 
		final int n = data.size(); 
		double aux;

		if ( n < 2 ) 
			return Double.NaN; 
		
		double avg = ((Double)data.get(0)).doubleValue(); 
		double sum = 0; 
		for ( int i = 1 ; i < n ; i++ ) 
		{ 
			aux = ((Double)data.get( i )).doubleValue();
			double newavg = avg + (aux - avg) / (i + 1) ; 
			sum += (aux - avg) * (aux - newavg); 
			avg = newavg ; 
		} 
		return Math.sqrt (sum / ( n - 1 )); 
	} 

	public static double average ( Vector data)
	{
		final int n = data.size();
		double avg = ((Double)data.get(0)).doubleValue();

		for (int i = 1 ; i < n ; i++)
		{
			avg += ((Double)data.get( i )).doubleValue();
		}

		return avg / n;
	}

	public static double bestValue( Vector data)
	{
		final int n = data.size();
        double best = ((Double)data.get(0)).doubleValue();
		double aux;

		for (int i = 1 ; i < n ; i++)
		{   // the best value is the lower one
			aux = ((Double)data.get( i )).doubleValue();
			if (best > aux) best = aux;
		}

		return best;  
	}
}
