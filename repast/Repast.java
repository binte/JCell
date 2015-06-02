package repast;

import jcell.*;

import uchicago.src.sim.engine.*;
import uchicago.src.sim.gui.*;
import uchicago.src.sim.space.Object2DGrid;
import java.awt.Color;

import java.util.*;

public class Repast extends SimpleModel {

	/**
	 * @param args
	 */
	
	static public class Player implements Drawable{
		private Player other;
		private int strategy;
		private int now;
		private int before;
		private boolean played = false;
		private int x;
		private int y;
		
		private Color color = Color.RED;
		
		private Random rnd;
		
		public final int DEFECT = 1;
		public final int COOPERATE = 0;
		
		
		
		public Player ()
		{
			played = true;
			now = DEFECT;
		}
		
		
		public void draw (SimGraphics g)
		{
			g.drawFastRoundRect(color);
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
		
		public void setColor(Color _color)
		{
			color = _color;
		}
		
	}
	
	public Repast()
	{
		name = "Repast For cGA Takeover";
		params = new String [] {"ratio"};
	}
	
	public static final int TIT_FOR_TAT = 0; 
	public static final int ALWAYS_DEFECT = 1; 
	
	private int ratio = 25;
	private int x, y;
	private Takeover problem;
	private PopGrid pop;
	private Player pRed, pBlue, pGrey;
	
	private int longitCrom = 10; 
	private double maxFitness = Math.pow(2,longitCrom); 
	private int generationsLimit = 1000000; 
	
	private DisplaySurface pop2D;
	private Object2DDisplay obj_dis;
	private Object2DGrid obj;
	
	private Random rnd;

	public void setup() {
	    super.setup();
	    pop = new PopGrid(64,64);
	    pop.setPopSize(0);
	}
	

	public int getRatio() {
		return ratio;
	}


	public void setRatio(int _valor) {
		this.ratio = _valor;
	}


	public void buildModel() {
		pRed = new Player();
		pRed.setColor(Color.RED);
		pBlue = new Player();
		pBlue.setColor(Color.BLUE);
		pGrey = new Player();
		pGrey.setColor(Color.DARK_GRAY);
	    
	    pop2D = new DisplaySurface(this, "Population");
	    
	}
	
	public void begin(String dataFile, int genLimit)
	{
		super.begin();
		
		// set the PopSize
		switch (ratio)
		{
			case 1: x = 4; y = 1024; break;
			case 2: x = 5; y = 819; break;
			case 3: x = 6; y = 682; break;
			case 4: x = 7; y = 585; break;
			case 5: x = 8; y = 512; break;
			case 6: x = 9; y = 455; break;
			case 7: x = 11; y = 372; break;
			case 8: x = 12; y = 341; break;
			case 9: x = 13; y = 315; break;
			case 10: x = 15; y = 273; break;
			case 11: x = 16; y = 256; break;
			case 12: x = 21; y = 195; break;
			case 13: x = 22; y = 186; break;
			case 14: x = 23; y = 178; break;
			case 15: x = 31; y = 132; break;
			case 16: x = 32; y = 128; break;
			case 17: x = 33; y = 124; break;
			case 18: x = 35; y = 117; break;
			case 19: x = 39; y = 105; break;
			case 20: x = 44; y = 93; break;
			case 21: x = 45; y = 91; break;
			case 22: x = 46; y = 89; break;
			case 23: x = 62; y = 66; break;
			case 24: x = 63; y = 65; break;
			case 25: x = 64; y = 64; break;
		}
		
		// Set the problem
		problem = new Takeover(x, y);
		
		// Initialize variables
		problem.start(dataFile, this, genLimit);
		
		pop.copyPop(problem.cea.getPopulation());
		
		buildDisplay();
		
	}
	
	public void buildDisplay()
	{
		
		obj = new Object2DGrid (x,y);
		
		obj_dis = new Object2DDisplay (obj);
		
		pop2D.addDisplayable(obj_dis, "Evolution of the Population");
		
		Player p = new Player();
		p.setColor(Color.BLUE);
		Player pRed = new Player();
		pRed.setColor(Color.RED);
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
			{
				if (((Double)pop.getIndividual(i,j).getFitness()).doubleValue() == maxFitness)
					obj.putObjectAt(i,j,pRed);
				else obj.putObjectAt(i,j,p);
			}
		
		pop2D.display();
		
	}
	
	
	public void step() {
		
		
		this.problem.step();
		pop.copyPop((PopGrid)problem.cea.getPopulation());
		int size = pop.getPopSize();
		
		for (int x = 0; x < pop.getDimX(); x++)
			for (int y = 0; y < pop.getDimY(); y++)
			{
				if ((pop.getPopSize() != 0) && (((Double)pop.getIndividual(x,y).getFitness()).doubleValue() == maxFitness) && (((Player)obj.getObjectAt(x,y)).color == Color.BLUE))
					obj.putObjectAt(x,y,pGrey);
				
				else if ((pop.getPopSize() != 0) && (((Double)pop.getIndividual(x,y).getFitness()).doubleValue() == 0.0))
					obj.putObjectAt(x,y,pBlue);
				
				else if ((pop.getPopSize() != 0) && (((Double)pop.getIndividual(x,y).getFitness()).doubleValue() == maxFitness))
					obj.putObjectAt(x,y,pRed);
		    }
		    
	    pop2D.updateDisplay();
		
	  }
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
	}

}
