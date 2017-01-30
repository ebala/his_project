package competition;

import java.util.Random;

/**
 * http://cstheory.stackexchange.com/questions/14758/tournament-selection-in-genetic-algorithms
 * @author bala
 *
 */
public class SelectionMethods {
	
	private Random rand = new Random();
	
	
	public int[] tournamentSelection(double[] population, int n, int tourParents){
		
		
		
		
		return null;
		
	}
	
  /**	
    *
    * @param population - set of individuals' segments. Segment is equal to individual's fitness.
    * @param n - number of individuals to choose from population.
    * @return set of indexes, pointing to the chosen individuals in the population set
    */
   public int[] StochasticSampling(double[] population, int n) {
       // Calculate total fitness of population
       double totalFitness = 0.0;
       for (double segment : population) {
           totalFitness += segment;
       }
       // Average for the expected winners
       double meanFitness = totalFitness / n;
       
       // Generate random number between 0 and p
       double start = Math.random() * meanFitness;
       // Pick n individuals
       int[] individuals = new int[n];
       int index = 0;
       double sum = population[index];
       for (int i = 0; i < n; i++) {
           // Determine pointer to a segment in the population
           double pointer = start + i * meanFitness;
           // Find segment, which corresponds to the pointer
           if (sum >= pointer) {
               individuals[i] = index;
           } else {
               for (++index; index < population.length; index++) {
                   sum += population[index];
                   if (sum >= pointer) {
                       individuals[i] = index;
                       break;
                   }
               }
           }
       }
       // Return the set of indexes, pointing to the chosen individuals
       return individuals;
   }
   
   /**
    * 
    * @param population
    * @param n
    * @return
    */
   public int[] rankPopulation(double[] population, int n){
	  	// rank populations (tournament)
		int[] winners = new int[n];
		int[] competitors = new int[population.length];
		for (int c = 0; c < competitors.length; c++) {
			competitors[c] = c;
		}
		

		 // Implementing Fisher–Yates shuffle
		for (int c = 0; c < competitors.length; c++) {
			int index = rand.nextInt(c + 1);
			int temp = competitors[index];
			competitors[index] = competitors[c];
			competitors[c] = temp;
		}

		// what is happening here?
		for (int t = 0; t < winners.length; t++) { // 5
			int winner = -1;
			double winner_fit = Double.MAX_VALUE;
			
			int tour_size = 4;
			
			// Select 1 winner
			for (int c = 0; c < tour_size; c++) {
				int competitor = competitors[tour_size * t + c];
				if (population[competitor] < winner_fit) {
					winner = competitor;
					winner_fit = population[winner];
				}
			}
			winners[t] = winner; // 5 winners
		}
		
		return winners;
   }
   

}
