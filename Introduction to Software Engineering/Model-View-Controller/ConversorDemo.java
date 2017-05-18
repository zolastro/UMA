package prMVC;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConversorDemo {
	public static void main(String[] args) {
		ViewConversor viewConversor = new PanelConversor();
	
		Conversor conversor = new Conversor();
		
		ActionListener ctrConversor = new CtrConversor(viewConversor, conversor);
		
		// Asignamos el controlador a la vista
		viewConversor.controller(ctrConversor);

		JFrame window = new JFrame("Conversor");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane((JPanel) viewConversor);
		window.pack();
		window.setVisible(true);
	}
}
