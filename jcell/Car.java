package jcell;

public class Car {

	private float opening;
	private float closure;
	

	public Car() {
		
		this.opening = 0;
		this.closure = 0;
	}
	
	public Car(float opening, float closure) {
		
		this.opening = opening;
		this.closure = closure;
	}
	
	
	public float getOpening() {
		
		return this.opening;
	}
	
	public float getClosure() {
		
		return this.closure;
	}
}
