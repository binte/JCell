/**
 * 
 * @author Bernabe Dorronsoro 
 * January, 22th, 2006
 * 
 * Description
 * Manages the list of all possible population shapes for the adaptive algorithm
 * 
 * For details on how does this method work, consult
 * E. Alba and B. Dorronsoro, The Exploration 
 *  
 */

package adaptiveCGA;


import jcell.*;
import java.util.Vector;

public class Shapes {
	
	private static int popSize;
	private static int index;
	private Vector shapesList;
	
	// Default constructor
	public Shapes()
	{
		setPopSize(0);
		index = 0;
	}
	
	// Creates a list 'shapesList' containing all the possible
	//  grid shapes for a population made up of 'size' individuals
	public Shapes(PopGrid pop)
	{
		setPopSize(pop.getPopSize());
		shapesList = new Vector();
		genShapes();
		setIndex(new Shape(pop.getDimX(),pop.getDimY()));
	}
	
	// assigns the size of the population
	private void setPopSize(int size)
	{
		popSize = size;
	}
	
	// returns the size of the population
	private int getPopSize()
	{
		return popSize;
	}
	
	// returns the first number greater than denom that divides num
	// if that number doesn't exist, the function returns 0
	private int nextDiv(int denom, int num)
	{
		int i;
		
		i= denom+1;
		while (i<=num)
		{
			if ((num%i) == 0) return i;
			i++;
		}
		return -1;
	}
	
	// Generates all the possible shapes of the grid with the same number of individuals
	// returns 1 if the execution was successfully
	public int genShapes()
	{
	 	int new_x=0, new_y=0;
	 	Shape sh;
	
		if (popSize<=0) return -1;
		
		new_x = nextDiv(new_x, popSize);	
		
		while (Math.pow((double)new_x,2.0)<=(double)popSize)  // insert from the narrowest -linear- shape until square shape
		{
			if (new_x>0) 
			{
				if (!append(new Shape(new_x,popSize/new_x))) return -1;  // Insert the shape (square is the last inserted)
				new_x = nextDiv(new_x, popSize);
			}
					
			else return -1;
		}
		return 1;
	}

	// Inserts a new population's shape at the end of the list of pop shapes
	public boolean append(Shape sh)
	{
		return shapesList.add(sh);
	}
	
	// returns the next narrower pop shape. If the current grid shape is the linear one, returns itself
	public Shape moveRect()
	{
		if (index > 0)
		{
			index--;
			Shape sh = (Shape)shapesList.get(index);
			return (Shape) shapesList.get(index);
		}
		
		else return current();
	}
	
	// Returns the population shape pointed by the iterator (the current pop shape)
	public Shape current()
	{
		return (Shape) shapesList.get(index);
		
	}
	
	// returns the next more square grid shape. If the current grid shape is already Square, returns itself
	public Shape moveSq()
	{
		if (index < shapesList.size()-1) 
		{
			index++;
			return (Shape) shapesList.get(index);
		}
		else return current();
	}
	
	// return the shape located at the ith place
	public Shape get(int i)
	{
		return (Shape) shapesList.get(i);
	}
	
	// Moves the index to the ith position
	public void setIndex(int i)
	{
		index = i;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	// Moves the index to the position pointing to the shape 'sh'
	// if 'sh' is not into shapesList, returns -1
	public int setIndex(Shape sh)
	{
		index = 0;
		while ((index < shapesList.size()) && (!equal(sh,(Shape)shapesList.get(index)))) index++;
		if (index == shapesList.size()) return -1;
		else return 1;
	}
	
	// Number of shapes in the list
	public int getSize()
	{
		return shapesList.size();
	}
	
	public boolean equal(Shape sh1, Shape sh2)
	{
		return ((sh1.getX() == sh2.getX()) & (sh1.getY() == sh2.getY()));
	}

}
