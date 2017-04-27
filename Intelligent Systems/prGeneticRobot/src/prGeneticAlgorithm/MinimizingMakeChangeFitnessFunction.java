package prGeneticAlgorithm;

import org.jgap.*;
/**
 * This class provides an implementation of the classic "Make change" problem
 * using a genetic algorithm. The goal of the problem is to provide a
 * specified amount of change (from a cash purchase) in the fewest coins
 * possible. This example implementation uses American currency (quarters,
 * dimes, nickels, and pennies).
 *
 * This example may be seen as somewhat significant because it demonstrates
 * the use of a genetic algorithm in a less-than-optimal problem space.
 * The genetic algorithm does best when there is a smooth slope of fitness
 * over the problem space towards the optimum solution. This problem exhibits
 * a more choppy space with more local optima. However, as can be seen from
 * running this example, the genetic algorithm still will get the correct
 * answer virtually everytime.
 */
public class MinimizingMakeChangeFitnessFunction extends FitnessFunction
{
    private final int m_targetAmount;

    /**
     * Constructs this MinimizingMakeChangeFitnessFunction with the desired
     * amount of change to make.
     *
     * @param a_targetAmount The desired amount of change, in cents. This
     *                       value must be between 1 and 99 cents.
     */
    public MinimizingMakeChangeFitnessFunction( int a_targetAmount )
    {
        if( a_targetAmount < 1 || a_targetAmount > 99 )
        {
            throw new IllegalArgumentException(
                "Change amount must be between 1 and 99 cents." );
        }

        m_targetAmount = a_targetAmount;
    }


    /**
     * Determine the fitness of the given Chromosome instance. The higher the
     * return value, the more fit the instance. This method should always
     * return the same fitness value for two equivalent Chromosome instances.
     *
     * @param a_subject: The Chromosome instance to evaluate.
     *
     * @return A positive integer reflecting the fitness rating of the given
     *         Chromosome.
     */
    public double evaluate( IChromosome a_subject )
    {
        // The fitness value measures both how close the value is to the
        // target amount supplied by the user and the total number of coins
        // represented by the solution. We do this in two steps: first,
        // we consider only the represented amount of change vs. the target
        // amount of change and calculate higher fitness values for amounts
        // closer to the target, and lower fitness values for amounts further
        // away from the target. If the amount equals the target, then we go
        // to step 2, which adjusts the fitness to a higher value for
        // solutions representing fewer total coins, and lower fitness
        // values for solutions representing a larger total number of coins.
        // ------------------------------------------------------------------
        int changeAmount = amountOfChange( a_subject );
        int totalCoins = getTotalNumberOfCoins( a_subject );
        int changeDifference = Math.abs( m_targetAmount - changeAmount );

        // Step 1: Determine the distance of the amount represented by the
        // solution from the target amount. Since we know the maximum amount
        // of change is 99 cents, we'll subtract from that the difference
        // between the solution amount and the target amount. That will give
        // the desired effect of returning higher values for amounts close
        // to the target amount and lower values for amounts further away
        // from the target amount.
        // ------------------------------------------------------------------
        double fitness = ( 99 - changeDifference );

        // Step 2: If the solution amount equals the target amount, then
        // we add additional fitness points for solutions representing fewer
        // total coins.
        // -----------------------------------------------------------------
        if( changeAmount == m_targetAmount )
        {
            fitness += 100 - ( 10 * totalCoins );
        }

        return fitness;
    }


    /**
     * Calculates the total amount of change (in cents) represented by
     * the given chromosome and returns that amount.
     *
     * @param a_potentialSolution The potential solution to evaluate.
     * @return The total amount of change (in cents) represented by the
     *         given solution.
     */
    public static int amountOfChange( IChromosome a_potentialSolution )
    {
        int numQuarters = getNumberOfCoinsAtGene( a_potentialSolution, 0 );
        int numDimes = getNumberOfCoinsAtGene( a_potentialSolution, 1 );
        int numNickels = getNumberOfCoinsAtGene( a_potentialSolution, 2 );
        int numPennies = getNumberOfCoinsAtGene( a_potentialSolution, 3 );

        return ( numQuarters * 25 ) + ( numDimes * 10 ) + ( numNickels * 5 ) +
               numPennies;
    }


    /**
     * Retrieves the number of coins represented by the given potential
     * solution at the given gene position.
     *
     * @param a_potentialSolution The potential solution to evaluate.
     * @param a_position The gene position to evaluate.
     * @return the number of coins represented by the potential solution
     *         at the given gene position.
     */
    public static int getNumberOfCoinsAtGene( IChromosome a_potentialSolution,
                                              int a_position )
    {
        Integer numCoins =
          (Integer) a_potentialSolution.getGene(a_position).getAllele();

        return numCoins.intValue();
    }


    /**
     * Returns the total number of coins represented by all of the genes in
     * the given chromosome.
     *
     * @param a_potentialsolution The potential solution to evaluate.
     * @return The total number of coins represented by the given Chromosome.
     */
    public static int getTotalNumberOfCoins( IChromosome a_potentialsolution )
    {
        int totalCoins = 0;

        int numberOfGenes = a_potentialsolution.size();
        for( int i = 0; i < numberOfGenes; i++ )
        {
            totalCoins += getNumberOfCoinsAtGene( a_potentialsolution, i );
        }

        return totalCoins;
    }
}
