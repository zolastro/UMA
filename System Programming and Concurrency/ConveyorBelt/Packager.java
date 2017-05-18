import java.util.Random;

public class Packager extends Thread{
	private Buffer buffer;
	private int type;
	
	public Packager(Buffer b, int t){
		buffer = b;
		type = t;
	}
	
	public void run(){
		Random rand = new Random();
		while(true){
			try {
				sleep(rand.nextInt(5000));
				System.out.println("Packaging item of type "+ type + "-->" + buffer);
				buffer.getItem(type);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
