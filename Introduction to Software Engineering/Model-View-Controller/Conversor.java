package prMVC;

public class Conversor {
	
	private final double FACTOR = 0.901302833;
	
	private double euros = 0;
	private double dollars = 0;
	
	public void setEuros(double amount){
		this.euros = amount;
		this.dollars = amount*(1/FACTOR);
	}
	
	public void setDollars(double amount){
		this.dollars = amount;
		this.euros = amount*FACTOR;
	}
	
	public double getEuros(){
		return this.euros;
	}
	
	public double getDollars(){
		return this.dollars;
	}
}