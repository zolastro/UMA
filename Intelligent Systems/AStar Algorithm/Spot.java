package prRobots;

import java.util.*;

public class Spot {
	public int x;
	public int y;
	public int g;
	public int h;
	public boolean isWall;
	public Spot previous;
	public List<Spot> neigbors;

	public Spot(int i, int j) {
		x = i;
		y = j;
		g = 0;
		h = 0;
		isWall = false;
	}

	public void addNeighbors(Spot[][] grid, int xLim, int yLim) {
		neigbors = new ArrayList<>();
		if (x < xLim) {
			neigbors.add(grid[x + 1][y]);
		}
		if (x > 0) {
			neigbors.add(grid[x -1][y]);
		}
		if (y < yLim) {
			neigbors.add(grid[x][y+1]);
		}
		if (y > 0) {
			neigbors.add(grid[x][y-1]);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Spot) {
			Spot spt = (Spot) obj;
			return spt.x == this.x && spt.y == this.y;
		}
		return false;
	}
	
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
