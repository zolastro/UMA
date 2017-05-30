package neural;

import java.util.Arrays;

import java.awt.Color;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.imageio.ImageIO;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.ml.train.MLTrain;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;

import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import robocode.BattleResults;
import robocode.control.*;
import robocode.control.events.*;

public class BattlefieldParameterEvaluator {

	// Minimum allowable battlefield size is 400
	final static int MAXBATTLEFIELDSIZE = 2000;

	// Minimum allowable gun cooling rate is 0.1
	final static double MAXGUNCOOLINGRATE = 10;
	final static int NUMBATTLEFIELDSIZES = 601;
	final static int NUMCOOLINGRATES = 501;
	final static int NUMSAMPLES = 100;
	// Number of inputs for the multilayer perceptron (size of the input
	// vectors)
	final static int NUM_NN_INPUTS = 2;

	// Number of hidden neurons of the neural network

	final static int NUM_NN_HIDDEN_UNITS = 20;

	// Number of epochs for training
	final static int NUM_OF_ROUNDS = 10;
	static double  maxScore = 0;
	static int ndxBattle;

	static double[] finalScore1;
	static double[] finalScore2;

	static PrintWriter pwScore1;
	static PrintWriter pwScore2;
	static PrintWriter pwSize;
	static PrintWriter pwCoolingRate;
	static PrintWriter errorTrain;
	static PrintWriter errorValidation;
	
	public static void main(String[] args) throws FileNotFoundException {
		pwScore1 = new PrintWriter("score1.txt");
		pwScore2 = new PrintWriter("score2.txt");
		pwSize = new PrintWriter("size.txt");
		pwCoolingRate = new PrintWriter("coolingRate.txt");
		errorTrain = new PrintWriter("errorTrain.txt");
		errorValidation = new PrintWriter("errorValidation.txt");
		
		double[] battlefieldSize = new double[NUMSAMPLES];
		double[] gunCoolingRate = new double[NUMSAMPLES];

		finalScore1 = new double[NUMSAMPLES];

		finalScore2 = new double[NUMSAMPLES];
		Random rng = new Random(15L);

		// Disable log messages from Robocode
		RobocodeEngine.setLogMessagesEnabled(false);

		// Create the RobocodeEngine

		// Run from C:/Robocode

		RobocodeEngine engine = new RobocodeEngine(new java.io.File("../robocode"));

		// Add our own battle listener to the RobocodeEngine
		engine.addBattleListener(new BattleObserver());

		// Show the Robocode battle view

		engine.setVisible(false);

		// Setup the battle specification

		// Setup battle parameters

		long inactivityTime = 1_000;
		int sentryBorderSize = 50;

		boolean hideEnemyNames = false;

		// Get the robots and set up their initial states
		RobotSpecification[] competingRobots = engine.getLocalRepository("sample.RamFire,sample.TrackFire");
		RobotSetup[] robotSetups = new RobotSetup[2];

		for (ndxBattle = 0; ndxBattle < NUMSAMPLES; ndxBattle++)

		{

			// Choose the battlefield size and gun cooling rate
			battlefieldSize[ndxBattle] = MAXBATTLEFIELDSIZE * (0.1 + 0.9 * rng.nextDouble());
			gunCoolingRate[ndxBattle] = MAXGUNCOOLINGRATE * (0.1 + 0.9 * rng.nextDouble());
			pwSize.append(battlefieldSize[ndxBattle]+ " ");
			pwCoolingRate.append(gunCoolingRate[ndxBattle]+ " ");
			
			// Create the battlefield

			int size = (int) Math.max(400,  battlefieldSize[ndxBattle]);
			BattlefieldSpecification battlefield = new BattlefieldSpecification(size,
					size);

			// Set the robot positions

			robotSetups[0] = new RobotSetup(battlefieldSize[ndxBattle] / 2.0, battlefieldSize[ndxBattle] / 3.0, 0.0);

			robotSetups[1] = new RobotSetup(battlefieldSize[ndxBattle] / 2.0, 2.0 * battlefieldSize[ndxBattle] / 3.0,
					0.0);

			// Prepare the battle specification
			BattleSpecification battleSpec = new BattleSpecification(battlefield, NUM_OF_ROUNDS, inactivityTime,
					gunCoolingRate[ndxBattle], sentryBorderSize, hideEnemyNames, competingRobots, robotSetups);

			// Run our specified battle and let it run till it is over
			engine.runBattle(battleSpec, true); // waits till the battle
												// finishes

		}

		// Cleanup our RobocodeEngine
		engine.close();

		System.out.println(Arrays.toString(battlefieldSize));

		System.out.println(Arrays.toString(gunCoolingRate));
		System.out.println(Arrays.toString(finalScore1));

		System.out.println(Arrays.toString(finalScore2));

		// Create the training dataset for the neural network
		double[][] RawInputs = new double[NUMSAMPLES][NUM_NN_INPUTS];
		double[][] RawOutputs = new double[NUMSAMPLES][1];
		double[][] trainingInputs = new double[NUMSAMPLES*2 / 3][NUM_NN_INPUTS];
		double[][] trainingOutputs = new double[NUMSAMPLES*2 / 3][1];
		double[][] validationInputs = new double[NUMSAMPLES / 3][NUM_NN_INPUTS];
		double[][] validationOutputs = new double[NUMSAMPLES / 3][1];

		for (int NdxSample = 0; NdxSample < NUMSAMPLES; NdxSample++)

		{

			// IMPORTANT: normalize the inputs and the outputs to
			// the interval [0,1]

			RawInputs[NdxSample][0] = battlefieldSize[NdxSample] / MAXBATTLEFIELDSIZE;
			RawInputs[NdxSample][1] = gunCoolingRate[NdxSample] / MAXGUNCOOLINGRATE;
			maxScore = maxOfArray(finalScore1);
			RawOutputs[NdxSample][0] = finalScore1[NdxSample] / (maxScore);
			pwScore1.append(RawOutputs[NdxSample][0] + " ");

		}
		//66% training, 33% validation
		trainingInputs = Arrays.copyOfRange(RawInputs, 0, NUMSAMPLES *2/ 3);
		trainingOutputs = Arrays.copyOfRange(RawOutputs, 0, NUMSAMPLES*2 / 3);
		validationInputs = Arrays.copyOfRange(RawInputs, NUMSAMPLES*2 / 3, NUMSAMPLES);
		validationOutputs = Arrays.copyOfRange(RawOutputs, NUMSAMPLES*2 / 3, NUMSAMPLES);

		// Create and train the neural network
		MLDataSet trainingSet = new BasicMLDataSet(trainingInputs, trainingOutputs);
		MLDataSet validationSet = new BasicMLDataSet(validationInputs, validationOutputs);

		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 2)); // Input
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, NUM_NN_HIDDEN_UNITS)); // Hidden
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1)); // Output
		network.getStructure().finalizeStructure();
		network.reset();

		System.out.println("Training network...");
		MLTrain train = new ResilientPropagation(network, trainingSet);
		
		NetworkTrainer networkTrainer = new NetworkTrainer(validationSet, network, 40_000, 2_000);
		
		do {
			train.iteration();
			errorTrain.append(Math.log(network.calculateError(trainingSet)) + " ");
			errorValidation.append(Math.log(network.calculateError(validationSet)) + " ");
		} while (!networkTrainer.hasFinished(network)); // testear
		
		network = networkTrainer.getBestNetwork();
		
		
		
		System.out.println("Training completed.");
		System.out.println("Best epoch: " + NetworkTrainer.bestEpoch);
		
		double e = network.calculateError(validationSet); // cambiar de
														// trainingSet a
														// validationSet
		System.out.println("Network trained to error: " + e);

		System.out.println("Testing network...");

		// Generate test samples to build an output image

		int[] outputRGBint = new int[NUMBATTLEFIELDSIZES * NUMCOOLINGRATES];
		Color myColor;

		double myValue = 0;
		double[][] myTestData = new double[NUMBATTLEFIELDSIZES * NUMCOOLINGRATES][NUM_NN_INPUTS];
		for (int ndxBattleSize = 0; ndxBattleSize < NUMBATTLEFIELDSIZES; ndxBattleSize++) {
			for (int ndxCooling = 0; ndxCooling < NUMCOOLINGRATES; ndxCooling++)

			{
				myTestData[ndxCooling + ndxBattleSize * NUMCOOLINGRATES][0] = 0.1
						+ 0.9 * ((double) ndxBattleSize) / NUMBATTLEFIELDSIZES;

				myTestData[ndxCooling + ndxBattleSize * NUMCOOLINGRATES][1] = 0.1
						+ 0.9 * ((double) ndxCooling) / NUMCOOLINGRATES;

			}
		}

		// Simulate the neural network with the test samples and fill a matrix
		for (int ndxBattleSize = 0; ndxBattleSize < NUMBATTLEFIELDSIZES; ndxBattleSize++) {
			for (int ndxCooling = 0; ndxCooling < NUMCOOLINGRATES; ndxCooling++)

			{
				double[] myResult = new double[1];
				network.compute(myTestData[ndxCooling+ndxBattleSize*NUMCOOLINGRATES], myResult);
				myValue = ClipColor(myResult[0]);

				myColor = new Color((float) myValue, (float) myValue, (float) myValue);
				outputRGBint[ndxCooling + ndxBattleSize * NUMCOOLINGRATES] = myColor.getRGB();

			}
		}
		System.out.println("Testing completed.");

		// Plot the training samples

		for (int ndxSample = 0; ndxSample < NUMSAMPLES / 2; ndxSample++) {

			myValue = ClipColor(finalScore1[ndxSample] /maxScore);
			myColor = new Color((float) myValue,

					(float) myValue, (float) myValue);

			int myPixelIndex = (int) (Math
					.round(NUMCOOLINGRATES * ((gunCoolingRate[ndxSample] / MAXGUNCOOLINGRATE) - 0.1) / 0.9)
					+ Math.round(NUMBATTLEFIELDSIZES * ((battlefieldSize[ndxSample] / MAXBATTLEFIELDSIZE) - 0.1) / 0.9)
							* NUMCOOLINGRATES);

			if ((myPixelIndex >= 0) && (myPixelIndex < NUMCOOLINGRATES * NUMBATTLEFIELDSIZES))

			{
				outputRGBint[myPixelIndex] = myColor.getRGB();
			}
		}

		BufferedImage img = new BufferedImage(NUMCOOLINGRATES, NUMBATTLEFIELDSIZES, BufferedImage.TYPE_INT_RGB);

		img.setRGB(0, 0, NUMCOOLINGRATES, NUMBATTLEFIELDSIZES, outputRGBint, 0, NUMCOOLINGRATES);

		File f = new File("hello.png");
		try {

			ImageIO.write(img, "png", f);
		} catch (IOException ioE) {

			// TODO Autoâ€�generated catchblock
			ioE.printStackTrace();

		}

		System.out.println("Image generated.");

		// Make sure that the Java VM is shut down properly
		pwScore1.close();
		pwScore2.close();
		pwSize.close();
		pwCoolingRate.close();
		
		System.exit(0);

	}

	/*
	 * 
	 * Clip a color value (double precision) to lie in the valid range [0,1]
	 */

	private static double maxOfArray(double[] arr) {
		double ans = arr[0];
		for (int i = 0; i < arr.length; i++) {
			ans = Math.max(ans, arr[i]);
		}
		return ans;
	}

	public static double ClipColor(double value) {

		if (value < 0.0) {
			value = 0.0;
		}
		if (value > 1.0) {
			value = 1.0;
		}

		return value;

	}

	//

	// Our private battle listener for handling the battle event we are
	// interested in.

	//
	static class BattleObserver extends BattleAdaptor {

		// Called when the battle is completed successfully with battle results
		public void onBattleCompleted(BattleCompletedEvent e) {
			System.out.println("â€�â€� Battle has completed â€�â€�");

			// Get the indexed battle results
			BattleResults[] results = e.getIndexedResults();

			// Print out the indexed results with the robot names
			System.out.println("Battle results:");
			for (BattleResults result : results) {
				System.out.println(" " + result.getTeamLeaderName() +

						": " + result.getScore());
			}

			// Store the scores of the robots

			BattlefieldParameterEvaluator.finalScore1[ndxBattle] = Math.pow(results[0].getScore(), 2);
			BattlefieldParameterEvaluator.finalScore2[ndxBattle] = Math.pow(results[1].getScore(), 2);
			pwScore2.append(BattlefieldParameterEvaluator.finalScore2[ndxBattle] + " ");
		}
		
		// Called when the game sends out an information message during the
		// battle
		public void onBattleMessage(BattleMessageEvent e) {
			System.out.println("Msg> " + e.getMessage());
		}

		// Called when the game sends out an error message during the battle
		public void onBattleError(BattleErrorEvent e) {
			System.out.println("Err> " + e.getError());
		}
	}

}
