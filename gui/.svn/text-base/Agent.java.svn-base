/*$$
 * Copyright (c) 1999, Trustees of the University of Chicago
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Chicago nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Hashtable;

import HcGA.Hierarchy;
import jcell.Individual;

import uchicago.src.reflector.BooleanPropertyDescriptor;
import uchicago.src.reflector.DescriptorContainer;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
//import uchicago.src.sim.space.Diffuse2D;
import uchicago.src.sim.space.Multi2DGrid;
//import uchicago.src.sim.util.SimUtilities;
//import cern.jet.random.Uniform;

/**
 * The agent for the Food Bugs simulation. This pretty much follows the
 * Swarm code.
 *
 * @author Swarm Project and Nick Collier
 * @version $Revision: 1.2 $ $Date: 2005/11/16 19:02:15 $
 * @see uchicago.src.repastdemos.Agents.AgentsModel
 */

public class Agent implements Drawable, DescriptorContainer {

  private int x, y;
  
  private double value;
  private Individual individual;

  private FoodSpace space;
  private Multi2DGrid world;
  private Dimension worldSize;

  private int xSize;
  private int ySize;
  
  //private double display.getMaxValue();
  
  

  private Hashtable descriptors = new Hashtable();

  //the display
  private CGADisplay display;

  public Agent(FoodSpace space, Multi2DGrid world, int x,
    int y, CGADisplay display)
  {
    this.x = x;
    this.y = y;
	
	

	this.space = space;
    this.world = world;
    worldSize = world.getSize();
    xSize = worldSize.width;
    ySize = worldSize.height;

    BooleanPropertyDescriptor bd = new BooleanPropertyDescriptor("BDExample", false);
    descriptors.put("BDExample", bd);
	
	this.display = display;

  }

  public void setXY(int x, int y) {
    this.x = x;
    this.y = y;
    world.putObjectAt(x, y, this);
  }

  public void step() {
     
	 
    
    Point p = space.findExtreme(x, y);


        //while ((world.getObjectAt(p.x, p.y) != null)) {
        //}


      //space.addFood(x, y, outputFood);
      world.putObjectAt(x, y, null);
      x = p.x;
      y = p.y;
      world.putObjectAt(x, y, this);
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



  // DescriptorContainer interface
  public Hashtable getParameterDescriptors() {
    return descriptors;
  }


  
  public void draw(SimGraphics g) {
	if (CGADisplay.isSet(display.getDisplayProperties(), CGADisplay.DISPLAY_VALUE))
		drawValue(g);
	else if (CGADisplay.isSet(display.getDisplayProperties(), CGADisplay.DISPLAY_BESTDISTANCE))
		drawDistance(g);
	else {
		System.err.println("select proper display characteristic (Agent.java)");
		System.exit(-1);
	}
  }
  
  
  public void drawValue(SimGraphics g) {
	
	//boolean best = (getValue()>display.getMaxValue());
	boolean best = display.isBest(individual);
	float value = (float) (getValue()%display.getMaxValue());
	float val = (float) ((value%display.getMaxValue())/display.getMaxValue());

	Color col=Color.black;
	if (value >= 0.90*display.getMaxValue()) {
		val = 0.6f* (float) ((value-0.9*display.getMaxValue())/(0.1*display.getMaxValue())) + 0.4f;
		col = new Color(val, 0.0f, 0.0f);
	}
	else if (value >= 0.80*display.getMaxValue()) {
		val = 0.6f* (float) ((value-0.8*display.getMaxValue())/(0.2*display.getMaxValue())) + 0.4f;
		col = new Color(0.0f, val, 0.0f);
	}
	else
		col = new Color(0.3f*val, 0.0f, val);
    
	g.setFont(new Font("Arial", 16, 12));
	
	char moveChar;
	if (individual != null) {
		switch (individual.getLastMove()) {
		case Hierarchy.MOVE_LEFT:
			moveChar = (char) 8592;
			break;
		case Hierarchy.MOVE_UP:
			moveChar = (char) 8593;
			break;
		case Hierarchy.MOVE_RIGHT:
			moveChar = (char) 8594;
			break;
		case Hierarchy.MOVE_DOWN:
			moveChar = (char) 8595;
			break;
		default:
			moveChar = (char) ' ';
			break;
		}
	}
	else
		moveChar = (char) ' ';
	
	if (CGADisplay.isSet(display.getDisplayProperties(), CGADisplay.NO_TEXT)) {
		if (best)
			g.drawStringInRect(col, Color.RED, Character.toString((char) 215));
		else
			g.drawFastRoundRect(col);
	}
	else {
		if (best)
			g.drawStringInRect(col, Color.RED, moveChar + " " +  Integer.toString((int)(getValue()-display.getMaxValue())));
		else
			g.drawStringInRect(col, Color.BLACK, moveChar + " " + Integer.toString((int)getValue()));
			//g.drawFastRoundRect(col);
	}
  }

  public void drawDistance(SimGraphics g) {
		//float val = (float) (2*Math.exp(1.0f * (float) (getValue()/display.getMaxValue()))/Math.exp(2));
		boolean best = display.isBest(individual);
		
		
		Individual [] iv = new Individual[2];
		iv[0] = display.getBestIndividualReference();
		iv[1] = individual;
		float value = (float) Hierarchy.hammingDistance(iv);
		float val = (float) ((value%display.getMaxValue())/display.getMaxValue());
		
		
		

		Color col=Color.black;
		/*if (value >= 0.90*display.getMaxValue()) {
			val = 0.6f* (float) ((value-0.9*display.getMaxValue())/(0.1*display.getMaxValue())) + 0.4f;
			col = new Color(val, 0.0f, 0.0f);
		}
		else if (value >= 0.80*display.getMaxValue()) {
			val = 0.6f* (float) ((value-0.8*display.getMaxValue())/(0.2*display.getMaxValue())) + 0.4f;
			col = new Color(0.0f, val, 0.0f);
		}
		else*/
		col = new Color(0.3f*val, val, 0.0f);
	    
		g.setFont(new Font("Arial", 16, 12));
		
		char moveChar;
		if (individual != null) {
			switch (individual.getLastMove()) {
			case Hierarchy.MOVE_LEFT:
				moveChar = (char) 8592;
				break;
			case Hierarchy.MOVE_UP:
				moveChar = (char) 8593;
				break;
			case Hierarchy.MOVE_RIGHT:
				moveChar = (char) 8594;
				break;
			case Hierarchy.MOVE_DOWN:
				moveChar = (char) 8595;
				break;
			default:
				moveChar = (char) ' ';
				break;
			}
		}
		else
			moveChar = (char) ' ';
		
		if (CGADisplay.isSet(display.getDisplayProperties(), CGADisplay.NO_TEXT)) {
			if (best)
				g.drawStringInRect(col, Color.RED, Character.toString((char) 215));
			else
				g.drawFastRoundRect(col);
		}
		else {
			if (best)
				g.drawStringInRect(col, Color.RED, moveChar + " " +  Integer.toString((int)(getValue()-display.getMaxValue())));
			else
				g.drawStringInRect(col, Color.BLACK, moveChar + " " + Integer.toString((int)getValue()));
				//g.drawFastRoundRect(col);
		}
	  }
  

public double getValue() {
	if (individual != null)
		return ((Double)individual.getFitness()).doubleValue();
	else
		return 0.0;
}

public void setValue(double value) {
	this.value = value;
}

public Individual getIndividual() {
	return individual;
}

public void setIndividual(Individual individual) {
	this.individual = individual;
}
}
