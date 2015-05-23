/**
 * @author Bernabe Dorronsoro
 *
 * Class for an archive filled with the crowding 
 * procedure for the multiobjective cEA
 * 
 */

package MO;

import jcell.*;
import tools.QuickSort;
//import java.io.*;

public class Crowding extends Archive
{
   
	//public int numberOfFunctions;
  
	public Crowding()
	{
		// the population size is assumed to be 100
		super();
	}
	
	public Crowding(int numberOfFunctions)
	{
		super();
		this.numberOfFunctions = numberOfFunctions;
	}
	
        
/**
 * @brief Calculates the strength of the individuals according to the SPEA2
 *        algorithm
 */
public void strengthAssignment() 
{
  // STEP 1. Initialize strength and raw fitness values
  for (int i = 0; i < getNumStoredSols(); i++) {
    getIth(i).setStrength(0) ;
  } // for
  
  // STEP 2. Obtain the strength values of the individuals in the archive and
  //         in the population
  for (int i = 0; i < (getNumStoredSols() - 1); i++) {
    int result ;
    for (int j = i + 1; j < getNumStoredSols() ; j++) {
      //result = this->getIth(i)->numberOfViolatedConstraintsTest(this->getIth(j)) ;
      result = getIth(i).numberOfViolatedConstraintsTest(getIth(j));
      
      if (result == 0)
        result = getIth(i).dominanceTest(getIth(j)) ;
      if (result == -1) {
        //cout << i << " dominates " << j << endl ;
        getIth(i).setStrength(getIth(i).getStrength() +1);
      }                               
      else if (result == 1) {
        //cout << j << " dominates " << i << endl ;
        getIth(j).setStrength(getIth(j).getStrength() +1);
      }
    } // for
  } // for
} // strengthAssignment

/**
 * @brief Calculates the raw fitness to the individuals according to their 
 *        strength values. Requires the previous invocation of 
 *        strengthAssignment()
 */
public void strengthRawFitnessAssignment() {
  // STEP 1. Initialize strength and raw fitness values
  for (int i = 0; i < getNumStoredSols(); i++) {
    getIth(i).setStrengthRawFitness(0);
  } // for
  
  // STEP 2. Obtain the strength values of the individuals in the archive and
  //         in the population
  for (int i = 0; i < (getNumStoredSols() - 1); i++) {
    int result ;
    for (int j = i + 1; j < getNumStoredSols() ; j++) {
      //result = this->getIth(i)->numberOfViolatedConstraintsTest(this->getIth(j)) ;
      result = getIth(i).numberOfViolatedConstraintsTest(getIth(j)) ;
      if (result == 0)
        result = getIth(i).dominanceTest(getIth(j)) ;
      if (result == 1)
        getIth(i).setStrengthRawFitness(getIth(i).getStrengthRawFitness() + getIth(j).getStrength());
      else if (result == -1)                                        
        getIth(j).setStrengthRawFitness(getIth(j).getStrengthRawFitness() + getIth(i).getStrength());
    } // for  
  } // for 
} // strengthRawFitnessAssignment


/**
 * @brief Calculates the crowding distance of the individuals
 */
public void crowdingDistanceAssignment() {
	  int    lastIndividual ;
	  double distance, difference       ;
	   
	  lastIndividual = getNumStoredSols() - 1 ;
	  
	  if (getNumStoredSols() <= 2)
	  {
	  	for (int i = 0; i < getNumStoredSols(); i++)
		    getIth(i).setDistance(Double.POSITIVE_INFINITY) ;
	  	return;
	  }
	  
	  for (int i = 0; i < getNumStoredSols(); i++)
	    getIth(i).setDistance(0.0) ;
	    
	  for (int i = 0 ; i < numberOfFunctions; i++) { 
	    //sortByFitness(i);
	  	QuickSort.quickSort(archive, i, getNumStoredSols());
	    difference = ((Double[])getIth(lastIndividual).getFitness())[i].doubleValue() -
		((Double[])getIth(0).getFitnessValues())[i].doubleValue() ;

	    getIth(0).setDistance(Double.POSITIVE_INFINITY) ;
	    
	    for (int j = 0; j < lastIndividual ; j++) {
	    	if (getIth(j).getDistance() != Double.POSITIVE_INFINITY)
	    	{
	    		distance = (((Double[])getIth(j+1).getFitnessValues())[i].doubleValue() -
                	((Double[])getIth(j-1).getFitnessValues())[i].doubleValue()) / difference ;
                
                distance += getIth(j).getDistance();
                
                getIth(j).setDistance(distance);
	    	}
                
	    } // for
	  } // for
	} // crowdingDistanceAssignment

/**
 * @brief Sorting the archive population in ascending order by strength raw fitness and crowding 
 */
public void sortByStrengthRawFitnessAndCrowding() {
  //IndividualCMoEA swap ;
	Individual swap ;
  
  	// bubble sort loop
	for (int i = (getNumStoredSols()-1); i > 0 ;i--)
    for (int j = 0; j < i; j++) {
      if ((getIth(j).getStrengthRawFitness() > getIth(j+1).getStrengthRawFitness()) ||
          ((getIth(j).getStrengthRawFitness() == getIth(j).getStrengthRawFitness()) && 
           (getIth(j).getDistance() < getIth(j+1).getDistance())))
      {
        swap = getIth(j+1);
        setIth(j+1, getIth(j));
        setIth(j, swap);
      } // if
    } // sort
} // sortByStrengthFitnessAndCrowding
        
/**
 * @brief Sort in ascending order by fitness value
 * @param functionIdentifier The identifier of the funcion whose fitness is
 *        used for sorting
 */
public void sortByFitness(int functionIdentifier) {
  //IndividualCMoEA swap ;
	Individual swap ;

  if ((functionIdentifier >= numberOfFunctions) ||
      (functionIdentifier < 0)) {
    System.err.println("Crowding Archive::sortByFitness->the function identifier " + functionIdentifier + " is invalid. Must be between 1 and " + numberOfFunctions + "\n");
    return;
  } // if
   
  // bubble sort loop
  for (int i = (getNumStoredSols()-1); i > 0 ;i--)
    for (int j = 0; j < i; j++) { 
      if (((Double[])getIth(j).getFitnessValues())[functionIdentifier].doubleValue() >
          ((Double[])getIth(j+1).getFitnessValues())[functionIdentifier].doubleValue()){
        swap             = getIth(j);
        this.setIth(j, getIth(j+1));
        setIth(j+1, swap);
      } // if
    } // for
} // sortByObjective
	


	//public boolean Insert(IndividualCMoEA individual) {
public boolean Insert(Individual individual) {
	int     result           ;
	boolean finish           ;
	int     counter          ;
	boolean storeNewSolution ;

	finish           = false ;
	storeNewSolution = true  ;
	counter          = 0     ;
	
	// CASE 1. The archive is empty
	//         Action: add solution to the archive

	while ((counter < getNumStoredSols()) && !finish) 
	{
		if (individual.identicalFitness(getIth(counter))) {
  		// There is a solution with the same fitness vector
	    	finish           = true  ;
  			storeNewSolution = false ;
	    } // if
	    else 
	    {
	      result = individual.numberOfViolatedConstraintsTest(getIth(counter)) ;
	      if (result == 0)
	        result = individual.dominanceTest(getIth(counter)) ;
	      if (result == 1) {
	        finish           = true  ; 
	        storeNewSolution = false ;
	        // The solution is not added to the archive
	      } // if
	      else if (result == -1) {
	        Delete(counter) ;
	      } // else if
	      else  {
	        counter ++ ;
	      } // else
	    } // else
	  } // while
	  
	  if (storeNewSolution) {
	  	Add(individual) ;
	    crowdingDistanceAssignment() ;
	    // If the archive is not full, add the solution
	    if (getNumStoredSols() > getSize())
	    {
	      // The archive is full
	      // If the solution has the smallest crowding distance, delete it, else
	      // delete the individual with smallest crowding distance
	      int worstIndividual = 0 ;
	      for (int i = 1; i <= getSize(); i++)
	        if (getIth(i).getDistance() <
	            getIth(worstIndividual).getDistance()) 
	         {
	          	worstIndividual = i ;     
	         } // if
	      if (individual.getDistance() == getIth(worstIndividual).getDistance())
	      // no sera? if (individual.getDistance() <= getIth(worstIndividual).getDistance())
	        storeNewSolution = false ;
	      
	      // Find and replace an individual of the most crowded region 
	      Delete(worstIndividual) ; 	   
	 	}
	  } // if
	  
	  return storeNewSolution ;
	} // archiveIndividual

	public Object clone()
    {
      Crowding crowding;
      
      crowding = new Crowding(this.numberOfFunctions);
      
      for (int i=0; i<this.getSize(); i++)
         	if (archive[i] == null) crowding.archive[i] = null;
      		//else crowding.archive[i] = (IndividualCMoEA) archive[i].clone();
         	else crowding.archive[i] = (Individual) archive[i].clone();
      
      crowding.setSize(this.getSize());
      crowding.setNumStoredSols(this.getNumStoredSols());
      
      return crowding;
   }
   
}
