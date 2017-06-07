package prBus;

import java.util.concurrent.locks.*;

public class Bus {

	private static final int RIDE_TIME = 3000;

	private int maxNumberOfPassengers;
	private int numberOfPassengers = 0;
	private int numberOfBusinessWaiting;

	public Bus(int maxNumberOfPassengers) {
		this.maxNumberOfPassengers = maxNumberOfPassengers;
		this.numberOfBusinessWaiting = maxNumberOfPassengers;
	}

	public synchronized void waitForTour(Passenger passenger) throws InterruptedException {
		while (hasToWait(passenger)) {
			wait();
		}
		numberOfPassengers++;
		System.out.println(passenger + " has taken sit");
		if (passenger.isOfType(Type.BUSINESS)) {
			numberOfBusinessWaiting--;
		}
		if (numberOfPassengers == maxNumberOfPassengers) {
			busRunning();
		}
		wait();
	}

	private synchronized void busRunning() throws InterruptedException {
		System.out.println("Taking a ride...");
		Thread.sleep(RIDE_TIME);
		numberOfPassengers = 0;
		notifyAll();
	}

	public boolean hasToWait(Passenger passenger) {
		return (passenger.isOfType(Type.TOURIST) && numberOfBusinessWaiting > 0)
				|| numberOfPassengers >= maxNumberOfPassengers;
	}

	public void businessPassengerWaiting() {
		this.numberOfBusinessWaiting++;
	}
}
