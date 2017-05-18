
public class Driver {
	public static void main(String[] args) {
		Buffer b = new Buffer(10, 3);
		Robot r = new Robot(b);
		Packager[] ps = new Packager[3];
		for(int i = 0; i < ps.length; i++){
			ps[i] = new Packager(b, i);
			ps[i].start();
		}
		r.start();
	}
}
