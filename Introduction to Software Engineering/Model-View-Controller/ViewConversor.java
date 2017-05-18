package prMVC;

import java.awt.event.*;

public interface ViewConversor {
	public static final String TO_EUROS = "TO_EUROS";
	public static final String TO_DOLLARS = "TO_DOLLARS";

	double getEuros();
	double getDollars();

	void printEuros(double euros);
	void printDollars(double dollars);
	
	void message(String msg);
	
	void controller(ActionListener ctr);
}
