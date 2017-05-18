package prMVC;

import java.awt.event.*;

public class CtrConversor implements ActionListener {

	private ViewConversor viewConversor;
	private Conversor conversor;

	public CtrConversor(ViewConversor vc, Conversor c) {
		viewConversor = vc;
		conversor = c;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		viewConversor.message(" ");

		switch (command) {
		case ViewConversor.TO_DOLLARS: {
			double amount = viewConversor.getEuros();
			conversor.setEuros(amount);
			viewConversor.printDollars(conversor.getDollars());
			viewConversor.printEuros(conversor.getEuros());
			break;
		}

		case ViewConversor.TO_EUROS: {
			double amount = viewConversor.getDollars();
			conversor.setDollars(amount);
			viewConversor.printEuros(conversor.getEuros());
			viewConversor.printDollars(conversor.getDollars());
			break;
		}

		default:
			viewConversor.message("There was an error.");
			break;
		}
	}

}
