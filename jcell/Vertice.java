package jcell;

public class Vertice {
	
	protected int id;		// id do v�rtice, relativo ao seu n�mero original
	protected float x;  	// abcissa
	protected float y;	// ordenada
	protected float score;	// pontua��o obtida pela passagem neste v�rtice
	
	
	public Vertice() {
		
		this.id		= -1;
		this.x 		= Integer.MAX_VALUE;
		this.y 		= Integer.MAX_VALUE;
		this.score 	= Integer.MIN_VALUE;
	}

	public Vertice(int id, float x, float y, float score) {
		
		this.id		= id;
		this.x		= x;
		this.y		= y;
		this.score 	= score;
	}


	/************************************************/
	/********************* SETS *********************/
	/************************************************/
	public void setID(int id) {
		
		this.id = id;
	}

	public void setX(float x) {
		
		this.x = x;
	}

	public void setY(float y) {
		
		this.y = y;
	}

	public void setScore(float score) {
		
		this.score = score;
	}


	/************************************************/
	/********************* GETS *********************/
	/************************************************/
	public int getID() {
		
		return this.id;
	}

	public float getX() {
		
		return this.x;
	}

	public float getY() {
		
		return this.y;
	}

	public float getScore() {
		
		return this.score;
	}


	/************************************************/
	/******************* M�todos ********************/
	/************************************************/

	/* Retorna uma string representativa do V�rtice que invocou este m�todo */
	public String toString() {
	  
		StringBuilder sb = new StringBuilder();

		sb.append(this.id + ":	");
		sb.append("(");
		sb.append(this.x);
	  	sb.append(",	");
	  	sb.append(this.y);
	  	sb.append(",	");
	  	sb.append(this.score);
	  	sb.append(")");

		return sb.toString();
	}
}
