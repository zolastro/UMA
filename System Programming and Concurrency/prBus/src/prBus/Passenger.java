package prBus;

import java.util.Random;

public class Passenger extends Thread {

	private static final int MAX_SLEEP_TIME = 5000;

	private Bus bus;
	private Type type;
	private int id;

	public Passenger(Bus bus, Type type, int id) {
		this.bus = bus;
		this.type = type;
		this.id = id;
	}

	public void run() {
		Random rand = new Random();
		while (true) {
			try {
				bus.waitForTour(this);
				sleep(rand.nextInt(MAX_SLEEP_TIME));
				if (isOfType(Type.BUSINESS)) {
					bus.businessPassengerWaiting();
				}
			} catch (InterruptedException e) {

			}
		}
	}

	public boolean isOfType(Type type) {
		return this.type == type;
	}

	public int getID() {
		return this.id;
	}

	public Type getType() {
		return this.type;
	}
	
	public String toString(){
		return "Passenger " + getId() + " of type " +  getType();
	}
}
