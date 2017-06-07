package prBus;

public class Driver {
	private static final int NUMBER_OF_PASSENGERS = 16;
	public static void main(String[] args) {
		Bus bus = new Bus(NUMBER_OF_PASSENGERS/2);
		for(int i = 0; i < NUMBER_OF_PASSENGERS/2; i++){
			new Passenger(bus, Type.TOURIST, i).start();
			new Passenger(bus, Type.BUSINESS, i + (NUMBER_OF_PASSENGERS/2)).start();
		}
	}
}
