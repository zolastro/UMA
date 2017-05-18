package prMVC;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;

public class PanelConversor extends JPanel implements ViewConversor {

	private JTextField amountEurosJTF;
	private JTextField amountDollarsJTF;
	private JLabel eurosJL;
	private JLabel dollarsJL;
	private JLabel messageJL;
	private JButton toEurosJB;
	private JButton toDollarsJB;

	public PanelConversor() {
		amountEurosJTF = new JTextField(10);
		amountDollarsJTF = new JTextField(10);
		eurosJL = new JLabel("Euros: ");
		dollarsJL = new JLabel("Dollars: ");
		messageJL = new JLabel(" ");
		toEurosJB = new JButton("To euros");
		toDollarsJB = new JButton("To dollars");

		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new GridLayout(2, 2));
		centralPanel.add(dollarsJL);
		centralPanel.add(amountDollarsJTF);
		centralPanel.add(eurosJL);
		centralPanel.add(amountEurosJTF);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2, 1));
		buttons.add(toEurosJB);
		buttons.add(toDollarsJB);

		this.setLayout(new BorderLayout());
		this.add(centralPanel, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.EAST);
		this.add(messageJL, BorderLayout.SOUTH);

		this.printEuros(0);
		this.printDollars(0);
	}

	@Override
	public double getEuros() {
		try{
			return Double.parseDouble(amountEurosJTF.getText());
		}catch(Exception e){
			message("Invalid input. Please, enter a correct number");
		}
		return 0;
	}

	public double getDollars() {
		try{
			double ans = Double.parseDouble(amountDollarsJTF.getText());
			return ans;
		}catch(Exception e){
			message("Invalid input. Please, enter a correct number");
			return 0;
		}
	}

	@Override
	public void message(String msg) {
		messageJL.setText(msg);
	}

	@Override
	public void printEuros(double euros) {
		amountEurosJTF.setText(String.format("%12.2f", euros));
	}

	@Override
	public void printDollars(double dollars) {
		amountDollarsJTF.setText(String.format("%12.2f", dollars));
	}
	
	@Override
	public void controller(ActionListener ctr) {
		toEurosJB.addActionListener(ctr);
		toEurosJB.setActionCommand(TO_EUROS);
		toDollarsJB.addActionListener(ctr);
		toDollarsJB.setActionCommand(TO_DOLLARS);
	}

}
