import java.util.Random;

import robocode.control.*;
import robocode.control.events.*; 

public class Main {
	private static final int NumObstacles = 20;

	public static void main(String[] args)
	 {
	 // Create the RobocodeEngine
	 RobocodeEngine engine =
	 new RobocodeEngine(new java.io.File("C:/Robocode"));
	 // Run from C:/Robocode
	 // Show the Robocode battle view
	 engine.setVisible(true);
	 // Create the battlefield
	 int NumPixelRows=15*64;
	 int NumPixelCols=15*64;
	 BattlefieldSpecification battlefield =
	 new BattlefieldSpecification(NumPixelRows, NumPixelCols);
	 // 800x600
	 // Setup battle parameters
	 int numberOfRounds = 5;
	 long inactivityTime = 10000000;
	 double gunCoolingRate = 1.0;
	 int sentryBorderSize = 50;
	 boolean hideEnemyNames = false;
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
	 for(int NdxObstacle=0;NdxObstacle<NumObstacles;NdxObstacle++)
	 {
	 double InitialObstacleRow= Math.round((rand.nextInt(NumPixelRows))%64);
	 double InitialObstacleCol= Math.round((rand.nextInt(NumPixelCols))%64);
	 System.out.println();
	 existingRobots[NdxObstacle]=modelRobots[0];
	 robotSetups[NdxObstacle]=new RobotSetup(InitialObstacleRow,
	 InitialObstacleCol,0.0);
	 
	 }
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
