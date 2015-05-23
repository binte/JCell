/**
 * 
 * @author Bernabe Dorronsoro 
 * January, 22th, 2006
 * 
 * Description
 * This class represents a possible grid shape for the population
 *  
 */

package adaptiveCGA;

public class Shape {
	
	private final static double radioNEWS = 0.8944; // the radius value for NEWS neighborhood
	private int x, y;
	private double radio;
	private double ratio;

	// Default constructor
	public Shape()
	{
		x=0;
		y=0;
		radio=0.0;
		ratio=0.0;
	}
	
	public Shape(int x, int y)
	{
		this.x = x;
		this.y = y;
		radio = 0.0;
		ratio = changeRatio();
	}
	
	// Sets 'x' and 'y' values for the population shape
	public void setDimensions(int x, int y)
	{
		this.x = x;
		this.y = y;
		ratio = changeRatio();
	}
	
	// returns the 'x' component of this population shape
	public int getX()
	{
		return x;
	}
	
	// returns the 'y' component of this population shape
	public int getY()
	{
		return y;
	}
	
	// Returns the ratio of the shape (with NEWS neighborhood)
	public double getRatio()
	{
		return ratio;
	}

	// Returns the ratio value for the current population
	private double changeRatio()
	{
		double radio=0.0, aux=0.0;
		double x_cent=0.0, y_cent=0.0;  // x and y values of the center of the grid
		int ind;
		
		if ((x==0) || (y==0)) return 0.0;
	
		// Get x_cent
		for (ind=1; ind<= x; ind++) x_cent = x_cent + (double)ind;
		x_cent = x_cent * y;
		x_cent = x_cent / (x*y);
		
		// Get y_cent
		for (ind=1; ind<= y; ind++) y_cent = y_cent + (double)ind;
		y_cent = y_cent * x;
		y_cent = y_cent / (x*y);
		
		for (ind=1; ind<= x; ind++) 
			radio = radio + Math.pow(((double)ind - x_cent), 2.0);
	
		aux = radio * y;
		radio = 0.0;
		
		for (ind=1; ind<= y; ind++)
			radio = radio + Math.pow(((double)ind - y_cent), 2.0);
		
		radio = radio * x;
			
		radio = Math.sqrt((radio+aux)/((double)(x*y))); 
		
		this.radio = radio;
		
		ratio = radioNEWS / radio;
	
		return ratio;
	}
}
