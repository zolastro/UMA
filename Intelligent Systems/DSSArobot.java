package prRobots;

import java.util.*;
import robocode.Robot;

public class DSSArobot extends Robot {

	private static final int NumColsRows = 15;
	private static final int SEED = 27;
	private static final int NumObstacles = 50;
	private Spot positions[][] = new Spot[NumColsRows][NumColsRows];
	
	public static void main(String[] args) {
		DSSArobot r = new DSSArobot();
	}
	
	public DSSArobot(){
		run();
	}
	
	public void run(){
		getMatrix(); 
		List<Spot> path = AStarPathFinding();
		System.out.println(path);
		//TODO follow path
	}
	
	private List<Spot> AStarPathFinding(){
		List<Spot> path = new ArrayList<>();
		Spot start = positions[0][0];
		start.isWall = false;
		Spot end = positions[NumColsRows-1][NumColsRows-1];
		end.isWall = false;
		
		List<Spot> openList = new ArrayList<>();
		List<Spot> closeList = new ArrayList<>();
		openList.add(start);
		
		while(openList.size() > 0){ 
			//Index of the current best spot
			int bestSoFar = 0;
			for(int i = 0; i < openList.size(); i++){
				if((openList.get(i).g + openList.get(i).h) < 
						(openList.get(bestSoFar).g + openList.get(bestSoFar).h)){
					bestSoFar = i;
				}
			}
			Spot bestSpot = openList.get(bestSoFar);
			if(bestSpot.equals(end)){
				// We've finished!
				path.add(bestSpot);
				while(bestSpot.previous != null){
					path.add(bestSpot.previous);
					bestSpot = bestSpot.previous;
				}
				path = reverse(path);
				return path;
			}
			openList.remove(bestSpot);
			closeList.add(bestSpot);
			List<Spot> neighbors = bestSpot.neigbors;
			
			for(int i = 0; i < neighbors.size(); i++){
				if(!closeList.contains(neighbors.get(i)) && !neighbors.get(i).isWall){
					int tempG = bestSpot.g + 1;		// Always cost 1 to move to the next position
					boolean isNewPath = false;
					if(openList.contains(neighbors.get(i))){
						if(tempG < neighbors.get(i).g){
							neighbors.get(i).g = tempG;		// New best path!
							isNewPath = true;
						}
					}else{
						neighbors.get(i).g = tempG;
						isNewPath = true;
						openList.add(neighbors.get(i));
					}
					if(isNewPath){
						//Update the g and h values
						neighbors.get(i).h = heuristic(neighbors.get(i), end);
						neighbors.get(i).previous = bestSpot;
					}
				}
			}	
		}
		return null;
	}	
	
	private List<Spot> reverse(List<Spot> original){
		List<Spot> ans = new ArrayList<>();
		for(int i = original.size()-1; i >= 0; i--){
			ans.add(original.get(i));
		}
		return ans;
	}
 	
	private int heuristic(Spot origin, Spot target){
		return Math.abs(origin.x - target.x) + Math.abs(origin.y - target.y);
	}
	
	private void getMatrix(){
		Random rand = new Random(SEED);
		
		int NdxObstacle = 0;
		
		for (int i = 0; i < NumColsRows; i++) {
			for (int j = 0; j < NumColsRows; j++) {
				positions[i][j] = new Spot(i, j);
			}
		}
		
		int i, j;
		while (NdxObstacle<NumObstacles) {
			i = rand.nextInt(NumColsRows-1);
			j = rand.nextInt(NumColsRows-1);
			if (!positions[i][j].isWall) {
				positions[i][j].isWall = true;
				NdxObstacle++;
			}			
		}
		
		for (int m = 0; m < NumColsRows; m++) {
			for (int n = 0; n < NumColsRows; n++) {
				positions[m][n].addNeighbors(positions, NumColsRows-1, NumColsRows-1);
			}
		}
		showMap();
	}
	
	
	private void showMap(){
		for (int i = 0; i < NumColsRows; i++) {
			for (int j = 0; j < NumColsRows; j++) {
				System.out.print((positions[i][j].isWall)? "*":"-");
			}
			System.out.println();
		}
	}
	
	
}
