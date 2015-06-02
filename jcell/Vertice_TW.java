package jcell;

public class Vertice_TW extends Vertice {

	private float begin;	// tempo em que a visita do v�rtice pode ser iniciada
	private float end;		// tempo m�ximo em que a visita do v�rtice pode ser iniciada
	private float duration;	// dura��o da visita do v�rtice
	
	
	public Vertice_TW() {
		
		super();
		
		this.begin		= 0;
		this.end		= 0;
		this.duration	= 0;
	}

	public Vertice_TW(int id, float x, float y, float score, float begin, float end, float duration) {
		
		super();
		
		this.begin 		= begin;
		this.end		= end;
		this.duration	= duration;
	}


	/************************************************/
	/********************* SETS *********************/
	/************************************************/	
	public void setBegin(float begin) {
		
		this.begin = begin;
	}
	
	public void setEnd(float end) {
		
		this.end = end;
	}
	
	public void setDuration(float duration) {
		
		this.duration = duration;
	}


	/************************************************/
	/********************* GETS *********************/
	/************************************************/
	public float getBegin() {
		
		return this.begin;
	}
	
	public float getEnd() {
		
		return this.end;
	}
	
	public float getDuration() {
		
		return this.duration;
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
	  	sb.append("score: " + this.score);
	  	sb.append(",	");
	  	sb.append("begin: " + this.begin);
	  	sb.append(",	");
	  	sb.append("end: " + this.end);
	  	sb.append(",	");
	  	sb.append("duration: " + this.duration);
	  	sb.append(")");

		return sb.toString();
	}
}
