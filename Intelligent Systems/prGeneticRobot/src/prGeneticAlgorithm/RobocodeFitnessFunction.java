package prGeneticAlgorithm;

import java.util.Random;

import org.jgap.*;

import robocode.*;
import robocode.control.*;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;

import java.io.*;

public class RobocodeFitnessFunction extends FitnessFunction {

	private final String allyRobot;
	private final String enemyRobot;
	public static final double MAX_BOUND = 1000000000.0d;

	private static final double ZERO_DIFFERENCE_FITNESS = Math.sqrt(MAX_BOUND);

	public RobocodeFitnessFunction(String ally, String enemy) {
		this.allyRobot = ally;
		this.enemyRobot = enemy;
	}

	public double evaluate(IChromosome chromosomes) {
		try {
			return startBattle(chromosomes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private double startBattle(IChromosome chromosomes) throws FileNotFoundException {

		int NumColRows = 15;
		int TileSize = 64;
		String RobocodePath = "C:/robocode";

		int NumPixelRows = NumColRows * TileSize;
		int NumPixelCols = NumColRows * TileSize;
		int SEED = 10;

		// Battle parameters
		int numberOfRounds = 15;
		long inactivityTime = 5000;
		double gunCoolingRate = 1.0;
		int sentryBorderSize = 50;
		boolean hideEnemyNames = false;

		// Create the RobocodeEngine
		RobocodeEngine engine = new RobocodeEngine(new java.io.File(RobocodePath));
		// Run from C:/Robocode
		// Show the Robocode battle view
		engine.setVisible(false);
		// Create the battlefield
		BattlefieldSpecification battlefield = new BattlefieldSpecification(NumPixelRows, NumPixelCols);
		// 800x600
		/*
		 * Create obstacles and place them at random so that no pair of
		 * obstacles are at the same position
		 */
		RobotSpecification[] modelRobots = engine.getLocalRepository(allyRobot + ", " + enemyRobot + ", sample.SittingDuck");
		RobotSpecification[] existingRobots = new RobotSpecification[3];
		RobotSetup[] robotSetups = new RobotSetup[3];

		/*
		 * Generate the positions for the robots
		 */
		int x, y;
		Random rand = new Random(SEED);
		String parametersFile = "C:\\Users\\zolas\\OneDrive\\Documents\\GitHub\\UMA\\IntelligentSystems\\prGeneticRobot\\bin\\prGeneticRobot\\SuperTracker.data\\parameters.txt";
		try (PrintWriter pw = new PrintWriter(parametersFile)) {
//			System.out.println("Printing parameters...");
			for (int i = 0; i < chromosomes.size(); i++) {
				Double value = (Double) chromosomes.getGene(i).getAllele();
//				System.out.println(value.toString());
				pw.append(value.toString() + '\n');
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < robotSetups.length; i++) {
			x = rand.nextInt(NumColRows);
			y = rand.nextInt(NumColRows);
			existingRobots[i] = modelRobots[i];
			robotSetups[i] = new RobotSetup((double) x * TileSize + TileSize / 2, (double) y * TileSize + TileSize / 2,
					0.0);
		}
		
		
		/*
		 * Create the agent and place it in a random position without obstacle
		 */

		/* Create and run the battle */
		BattleSpecification battleSpec = new BattleSpecification(battlefield, numberOfRounds, inactivityTime,
				gunCoolingRate, sentryBorderSize, hideEnemyNames, existingRobots, robotSetups);

        engine.addBattleListener(new BattleObserver());
		// Run our specified battle and let it run till it is over
		engine.runBattle(battleSpec, true); // waits till the battle finishes
		// Cleanup our RobocodeEngine
		
	
		PrintWriter writer;
		try {
			writer = new PrintWriter(parametersFile);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		engine.close();
		int result = BattleObserver.finalScore;
		System.out.println(result);
		System.out.println("Robot ended");
		
		GeneticAlgorithm.printText(result + " ");
		return result;
	}
}

class BattleObserver extends BattleAdaptor {
	public static int finalScore;

	// Called when the battle is completed successfully with battle results
	public void onBattleCompleted(BattleCompletedEvent e) {
		System.out.println("-- Battle has completed --");
		// Print out the sorted results with the robot names
		System.out.println("Battle results:");
		BattleResults[] results = e.getIndexedResults();
		finalScore = results[0].getScore();
	}

}
