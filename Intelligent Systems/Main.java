import java.util.Random;

import robocode.control.*;
import java.util.Arrays;
import robocode.control.events.*; 

public class Main {
	private static final int NumColRows = 15;
	private static final int TileSize = 64;
	private static final int NumObstacles = 20;
	
	public static void main(String[] args)
	{
		/*** VARIABLES ***/
		// Field parameters
		boolean[][] positions = new boolean[NumColRows][NumColRows];
		int NumPixelRows=NumColRows*TileSize;
		int NumPixelCols=NumColRows*TileSize;
		int NdxObstacle = 0;
		
		// Battle parameters
		int numberOfRounds = 5;
		long inactivityTime = 10000000;
		double gunCoolingRate = 1.0;
		int sentryBorderSize = 50;
		boolean hideEnemyNames = false;
		
		//Create the RobocodeEngine
		RobocodeEngine engine =
				new RobocodeEngine(new java.io.File("C:/Robocode"));
		// Run from C:/Robocode
		// Show the Robocode battle view
		engine.setVisible(true);
		// Create the battlefield
		BattlefieldSpecification battlefield =
				new BattlefieldSpecification(NumPixelRows, NumPixelCols);
		// 800x600
		/*
		 * Create obstacles and place them at random so that no pair of obstacles
		 * are at the same position
		 */
		RobotSpecification[] modelRobots =
				engine.getLocalRepository
				("sample.SittingDuck,searchpractice.RouteBot*");
		RobotSpecification[] existingRobots =
				new RobotSpecification[NumObstacles+1];
		RobotSetup[] robotSetups = new RobotSetup[NumObstacles+1];
		Random rand = new Random(5);
		
		/* 
		 * Generate the positions for the robots 
		 */
		int i, j;
		while (NdxObstacle<NumObstacles) {
			i = rand.nextInt(NumColRows);
			j = rand.nextInt(NumColRows);
			System.out.println(i + " " + j);
			if (!positions[i][j]) {
				positions[i][j] = true;
				existingRobots[NdxObstacle]=modelRobots[0];
				robotSetups[NdxObstacle]=new RobotSetup((double)i*TileSize+TileSize/2,(double)j*TileSize+TileSize/2,0.0);
				NdxObstacle++;
			}			
		}
		System.out.println(Arrays.deepToString(positions));
		
		/*
		 * Create the agent and place it in a random position without obstacle
		 */
		existingRobots[NumObstacles]=modelRobots[0]; 
		double InitialAgentRow= 0;
		double InitialAgentCol= 0;
		robotSetups[NumObstacles]=new RobotSetup(InitialAgentRow,
				InitialAgentCol,0.0);
		/* Create and run the battle */
		BattleSpecification battleSpec =
				new BattleSpecification(battlefield,
						numberOfRounds,
						inactivityTime,
						gunCoolingRate,
						sentryBorderSize,
						hideEnemyNames,
						existingRobots,
						robotSetups);
		// Run our specified battle and let it run till it is over
		engine.runBattle(battleSpec, true); // waits till the battle finishes
		// Cleanup our RobocodeEngine
		engine.close();
		// Make sure that the Java VM is shut down properly
		System.exit(0);
	} 
}
