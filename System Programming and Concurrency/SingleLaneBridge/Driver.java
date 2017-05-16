import java.util.Random;
import java.util.concurrent.Semaphore;


/**
 * 
 * @author Antonio David Ponce Martínez
 *
 */

public class Driver {
	public static void main(String[] args) {
		Car[] cars = new Car[10];
		for (int i = 0; i < cars.length; i++) {
			cars[i] = new Car(i, randomDirection());
			cars[i].start();
		}
	}

	private static Directions randomDirection() {
		Random rand = new Random();
		if (rand.nextInt(10) < 5) {
			return Directions.LEFT;
		} else {
			return Directions.RIGHT;
		}

	}

	static class Car extends Thread {

		private static final int MAX_CARS = 2;
		
		private static Semaphore changeDirection = new Semaphore(1, true);
		private static Semaphore mutexEnter = new Semaphore(1, true);
		private static Semaphore mutexExit = new Semaphore(1, true);
		private static Semaphore mutexLRWaiting = new Semaphore(1, true);
		private static Semaphore mutexRLWaiting = new Semaphore(1, true);
		private static Semaphore mutex2 = new Semaphore(1, true);
		private static Semaphore mutex3 = new Semaphore(1, true);
		
		private static int numberOfCarsFromRightToLeft = 0;
		private static int numberOfCarsFromLeftToRight = 0;

		private int id;
		private Directions direction;

		public Car(int id, Directions dir) {
			this.id = id;
			this.direction = dir;
		}

		public static void enterFromRightToLeft(int id) throws InterruptedException {
			
			mutexEnter.acquire();
			mutexLRWaiting.acquire();
			numberOfCarsFromRightToLeft++;
			if (numberOfCarsFromRightToLeft == 1) {
				mutexRLWaiting.acquire();
				changeDirection.acquire();
			}
			System.out.println("Car " + id + " entered the bridge");
			mutexEnter.release();
			mutexRLWaiting.release();
			mutexLRWaiting.release();
		}

		public static void exitFromRightToLeft(int id) throws InterruptedException {
			mutexExit.acquire();
			System.out.println("Car " + id + " exited the bridge");
			numberOfCarsFromRightToLeft--;
			if (numberOfCarsFromRightToLeft == 0) {
				System.out.println("Bridge empty");
				changeDirection.release();
			}
			mutexExit.release();
		}

		public static void enterFromLeftToRight(int id) throws InterruptedException {
			mutexEnter.acquire();
			mutexRLWaiting.acquire();
			numberOfCarsFromLeftToRight++;
			if (numberOfCarsFromLeftToRight == 1) {
				mutexLRWaiting.acquire();
				changeDirection.acquire();
				System.out.println("Direction changed!");
			}

			System.out.println("Car " + id + " entered the bridge");
			mutexEnter.release();
			mutexLRWaiting.release();
			mutexRLWaiting.release();
		}

		public static void exitFromLeftToRight(int id) throws InterruptedException {
			mutexExit.acquire();
			System.out.println("Car " + id + " exited the bridge");
			numberOfCarsFromLeftToRight--;
			if (numberOfCarsFromLeftToRight == 0) {
				System.out.println("Bridge empty");
				changeDirection.release();
			}
			mutexExit.release();
		}

		public void run() {
			Random rand = new Random();
			while (true) {
				try {
					sleep(rand.nextInt(500));
					System.out.println("Car " + id + " trying to cross from " + this.direction + " to "
							+ opossiteDirection(this.direction));
					if (this.direction == Directions.RIGHT) {
						enterFromRightToLeft(id);
					} else {
						enterFromLeftToRight(id);
					}

					sleep(4000);
					if (this.direction == Directions.RIGHT) {
						exitFromRightToLeft(id);
					} else {
						exitFromLeftToRight(id);
					}
					this.direction = opossiteDirection(this.direction);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}

		private Directions opossiteDirection(Directions d) {
			if (d == Directions.RIGHT) {
				return Directions.LEFT;
			} else {
				return Directions.RIGHT;
			}
		}
	}

	static enum Directions {
		LEFT, RIGHT;
	}
}
