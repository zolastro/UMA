package prRobots;

import java.util.*;
import robocode.Robot;
import robocode.StatusEvent;

public class DSSArobot extends Robot {

	private static final int NumColsRows = 15;
	private static final int SEED = 53;
	private static final int NumObstacles = 60;
	private Spot positions[][] = new Spot[NumColsRows][NumColsRows];
	private int turn;
	private Spot start;
	private Spot end;
	private List<Spot> path;
	
	public DSSArobot(){
		getMatrix();
		this.turn = 1;
		this.path = null;
		this.path = AStarPathFinding();			
		run();
	}
	
	public void run(){
		System.out.println(path);
	}
	
	public void onStatus(StatusEvent e) {
		if(path.get(turn).y>path.get(turn-1).y){
			move('N');
		}else if(path.get(turn).y<path.get(turn-1).y){
			move('S');
		}else if(path.get(turn).x>path.get(turn-1).x){
			move('E');
		}else if(path.get(turn).x<path.get(turn-1).x){
			move('W');
		}
		turn++;
	}
	
	private void move(char c){
		double d = this.getHeading();
		if(c == 'N'){
			if(d > 180){
				turnRight(360-d);
			}else {
				turnLeft(d);
			}
			ahead(64);
		}else if(c == 'S'){
			turnRight(180-d);
			ahead(64);
		}else if(c == 'E'){
			if(d > 90){
				turnLeft(d-90);
			}else if (d < 90) {
				turnRight(90-d);
			}
			ahead(64);
		}else{
			if(d>270){
				turnLeft(d-270);	
			}else if(d<270){
				turnRight(270-d);
			}
			ahead(64);
		}
	}
	
	private List<Spot> AStarPathFinding(){
		List<Spot> path = new ArrayList<>();
		start.isWall = false;
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
			i = rand.nextInt(NumColsRows);
			j = rand.nextInt(NumColsRows);
			if (!positions[i][j].isWall && !(i==0 && j==0)) {
				System.out.println(i + " " + j);
				positions[i][j].isWall = true;
				NdxObstacle++;
			}			
		}
		
		for (int m = 0; m < NumColsRows; m++) {
			for (int n = 0; n < NumColsRows; n++) {
				positions[m][n].addNeighbors(positions, NumColsRows-1, NumColsRows-1);
			}
		}
		
		start = positions[rand.nextInt(NumColsRows)][rand.nextInt(NumColsRows)];
		end = positions[rand.nextInt(NumColsRows)][rand.nextInt(NumColsRows)];
		
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
