import java.util.Random;

public class Robot extends Thread{
	
	private Buffer buffer;
	
	public Robot(Buffer b){
		buffer = b;
	}
	
	public void run(){
		Random rand = new Random();
		while(true){
			try {
				sleep(rand.nextInt(4000));
				int type = Math.round(rand.nextInt(buffer.types));
				buffer.putItem(type);
				System.out.println("Adding item of type "+ type + " ->" + buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
