package prRobots;

import java.util.*;
import robocode.Robot;

public class DSSArobot extends Robot {

	private static final int NumColsRows = 15;
	private Spot positions[][] = new Spot[NumColsRows][NumColsRows];
	
	public void run(){
		getMatrix(); 
		List<Spot> path = AStarPathFinding();
		//TODO follow path
	}
	
	private List<Spot> AStarPathFinding(){
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
			Spot current = openList.get(bestSoFar);
			if(current.equals(end)){
				// End
			}
		}
		
		return null;
	}	
	
	private void getMatrix(){
		Random rand = new Random(5);
		int NumObstacles = 20;
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
			System.out.println(i + " " + j);
			if (!positions[i][j].isWall) {
				positions[i][j].isWall = true;
				NdxObstacle++;
			}			
		}
	}
	
	
	
}
