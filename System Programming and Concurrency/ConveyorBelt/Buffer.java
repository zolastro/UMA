import java.util.concurrent.Semaphore;

public class Buffer {
	public int types;
	
	Integer[] buffer;
	
	Semaphore[] thereIsItem;
	Semaphore thereIsSpace;
	
	public Buffer(int size, int t){
		types = t;
		buffer = new Integer[size];
		thereIsSpace = new Semaphore(size, true);
		thereIsItem = new Semaphore[types];
		for(int i = 0; i < types; i++){
			thereIsItem[i] = new Semaphore(0, true);
		}
	
	}
	
	public int getItem(int type) throws InterruptedException{
		thereIsItem[type].acquire();
		int itemIndex = findItem(type);
		int ans = buffer[itemIndex];
		buffer[itemIndex] = null;
		thereIsSpace.release();
		return ans;
	}
	
	private int findItem(int type) {
		int i = 0;
		try{
		while( buffer[i] == null || buffer[i] != type){
			i++;
		}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("ERROR: serching for " + type + " when there isn't "
					+ "any item of this kind.");
		}
		return i;
	}

	public void putItem(int type) throws InterruptedException{
		thereIsSpace.acquire();
		buffer[nextFreePosition()] = type;
		thereIsItem[type].release();
	}
	
	private int nextFreePosition(){
		int i = 0;
		try{
		while(buffer[i] != null){
			i++;
		}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("ERROR: buffer is full when it shouldn't.");
		}
		return i;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < buffer.length; i++){
			sb.append("|" + ((buffer[i] != null)? buffer[i]:"NULL"));
		}
		return sb.toString();
	}
}
