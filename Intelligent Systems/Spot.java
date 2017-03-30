package prRobots;

public class Spot {
	public int x;
	public int y;
	public int g;
	public int h;
	public boolean isWall;
	
	public Spot previous;
	
	
	public Spot(int i, int j){
		x = i;
		y = j;
		g = 0;
		h = 0;
		isWall = false;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Spot){
			Spot spt = (Spot) obj;
			return spt.x == this.x && spt.y == this.y;
		}
		return false;
	}
}
