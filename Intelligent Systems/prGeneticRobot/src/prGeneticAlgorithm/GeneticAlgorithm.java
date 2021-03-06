package prGeneticAlgorithm;

import java.io.PrintWriter;

import org.jgap.*;
import org.jgap.impl.*;

import net.sf.robocode.battle.IBattleManagerBase;

public class GeneticAlgorithm {
	static PrintWriter PW;
	public static void main(String[] args) throws Exception {
		final int MAX_ALLOWED_EVOLUTIONS = 100;
		PW = new PrintWriter("allScores.txt");
		// Start with a DefaultConfiguration, which comes setup with the
		// most common settings.
		// -------------------------------------------------------------
		Configuration conf = new DefaultConfiguration();

		// Set the fitness function we want to use, which is our
		// MinimizingMakeChangeFitnessFunction that we created earlier.
		// We construct it with the target amount of change provided
		// by the user.
		// ------------------------------------------------------------
		
		FitnessFunction myFunc = new RobocodeFitnessFunction("prGeneticRobot.SuperTracker*", "prGeneticRobot.SuperRamFire*");

		conf.setFitnessFunction(myFunc);

		// Now we need to tell the Configuration object how we want our
		// Chromosomes to be setup. We do that by actually creating a
		// sample Chromosome and then setting it on the Configuration
		// object. As mentioned earlier, we want our Chromosomes to
		// each have four genes, one for each of the coin types. We
		// want the values of those genes to be integers, which represent
		// how many coins of that type we have. We therefore use the
		// IntegerGene class to represent each of the genes. That class
		// also lets us specify a lower and upper bound, which we set
		// to sensible values for each coin type.
		// --------------------------------------------------------------
		Gene[] sampleGenes = new Gene[4];

		// Distance limit to consider that the enemy is close
		sampleGenes[0] = new DoubleGene(conf, 0, 300);
		// Probability to change the speed
		sampleGenes[1] = new DoubleGene(conf, 0, 1);
		// Range of possible robot speeds
		sampleGenes[2] = new DoubleGene(conf, 0, 24);
		// Minimum robot speed
		sampleGenes[3] = new DoubleGene(conf, 0, 24);

		Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);

		conf.setSampleChromosome(sampleChromosome);
		// Finally, we need to tell the Configuration object how many
		// Chromosomes we want in our population. The more Chromosomes,
		// the larger the number of potential solutions (which is good
		// for finding the answer), but the longer it will take to evolve
		// the population each round. We'll set the population size to
		// 500 here.
		// --------------------------------------------------------------
		conf.setPopulationSize(10);
		Genotype population = Genotype.randomInitialGenotype( conf );

		PrintWriter pwConf = new PrintWriter("bestConf.txt");
		PrintWriter pwScor = new PrintWriter("bestScores.txt");
		System.out.println("Number of evolutions: " + MAX_ALLOWED_EVOLUTIONS);
		for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
			System.out.println("Generation: " + i);

			pwConf.append("Generation " + i + " \n:");
			population.evolve();
			IChromosome bestTank = population.getFittestChromosome();
			
			Double bestScore = bestTank.getFitnessValue();
			
			pwScor.append(bestScore.toString() + " ");
			for(Gene g:bestTank.getGenes()){
				pwConf.append(g.getAllele().toString() + ", ");
			}
			printText("\n");
		}

		pwConf.close();
		pwScor.close();
		PW.close();
	}

	static void printText(String t){
		PW.append(t);
	}
	
}
