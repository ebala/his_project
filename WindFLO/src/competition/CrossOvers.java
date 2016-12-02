package competition;

import java.util.List;
import java.util.Random;

public class CrossOvers {
	
	private double cross_rate = 0.4;
	private Random rand = new Random();

	/**
	 * 
	 * @param poplCount
	 * @param grid
	 * @param winners
	 * @param pops
	 * @return
	 */
	public boolean[][] uniformCrossOver(int poplCount, List<double[]> grid, int[] winners, boolean[][] pops){
		// crossover
		boolean[][] children = new boolean[poplCount][grid.size()];

		for (int c = 0; c < (poplCount - winners.length); c++) {
			int s1 = rand.nextInt(winners.length);
			int s2 = rand.nextInt(winners.length - 1);
			if (s2 >= s1) {
				s2++;
			}
			int p1 = winners[s1];
			int p2 = winners[s2];
			boolean[] parent1 = pops[p1];
			boolean[] parent2 = pops[p2];

			boolean[] child = new boolean[grid.size()];
			for (int j = 0; j < child.length; j++) {
				if (rand.nextDouble() < cross_rate) {
					child[j] = parent2[j];
				} else {
					child[j] = parent1[j];
				}
			}

			children[c] = child;
		}
		
		return children;
	}
	
	/**
	 * 
	 * @param poplCount
	 * @param grid
	 * @param winners
	 * @param pops
	 * @return
	 */
	public boolean[][] threeParentCrossOver(int poplCount, List<double[]> grid, int[] winners, boolean[][] pops){
		// crossover
		boolean[][] children = new boolean[poplCount][grid.size()];

		for (int c = 0; c < (poplCount - winners.length); c++) {
			int s1 = rand.nextInt(winners.length);
			int s2 = rand.nextInt(winners.length - 1);
			int s3 = rand.nextInt(winners.length - 2);
			if (s2 >= s1) {
				s2++;
			}
			int p1 = winners[s1];
			int p2 = winners[s2];
			int p3 = winners[s3];
			
			boolean[] parent1 = pops[p1];
			boolean[] parent2 = pops[p2];
			boolean[] parent3 = pops[p3];

			boolean[] child = new boolean[grid.size()];
			for (int j = 0; j < child.length; j++) {
				if (parent1[j] == parent2[j]) {
					child[j] = parent1[j];
				} else if(parent1[j] == parent3[j]){
					child[j] = parent1[j];
				}
			}

			children[c] = child;
		}
		
		return children;
	}

}
