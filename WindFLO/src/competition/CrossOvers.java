package competition;

import java.util.Arrays;
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
	public boolean[][] uniformCrossOver(int poplCount, List<double[]> grid, int[] winners, boolean[][] pops) {
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
	public boolean[][] threeParentCrossOver(int poplCount, List<double[]> grid, int[] winners, boolean[][] pops) {
		// crossover
		boolean[][] children = new boolean[poplCount][grid.size()];

		for (int c = 0; c < (poplCount /*- winners.length*/); c++) {
			int s1 = getRandomWithExclusion(rand, 0, winners.length - 1);
			int s2 = getRandomWithExclusion(rand, 0, winners.length - 1, s1);
			int s3 = getRandomWithExclusion(rand, 0, winners.length - 1, s1, s2);

			int p1 = winners[s1];
			int p2 = winners[s2];
			int p3 = winners[s3];

			boolean[] parent1 = pops[p1];
			boolean[] parent2 = pops[p2];
			boolean[] parent3 = pops[p3];

			boolean[] child = new boolean[grid.size()];

			for (int j = 0; j < child.length; j++) {

				// Three Parents
				if (parent1[j] == parent2[j] || parent1[j] == parent3[j]) {
					child[j] = parent1[j];
				} else if (parent2[j] == parent3[j]) {
					child[j] = parent2[j];
				} else {
					child[j] = rand.nextBoolean();
				}

				children[c] = child;
			}
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
	public boolean[][] twoPointCrossOver(int poplCount, List<double[]> grid, int[] winners, boolean[][] pops) {

		boolean[][] children = new boolean[poplCount][grid.size()];

		int child = -1;
		for (int c = 0; c < poplCount; c++) {
			int s1 = getRandomWithExclusion(rand, 0, winners.length - 1);
			int s2 = getRandomWithExclusion(rand, 0, winners.length - 1, s1);

			boolean[] parent1 = pops[winners[s1]];
			boolean[] parent2 = pops[winners[s2]];
			boolean[] c1 = parent1;
			boolean[] c2 = parent2;

			int point1 = getRandomWithExclusion(rand, 0, parent1.length - 1);
			int point2 = getRandomWithExclusion(rand, 0, parent1.length - 1, point1);

			int diff = Math.abs(point2 - point1);

			if (point1 > point2)
				point1 = point2;

			boolean[] sa1 = Arrays.copyOfRange(parent1, point1, point1 + diff);
			boolean[] sa2 = Arrays.copyOfRange(parent2, point1, point1 + diff);

			for (int i = 0; i < sa1.length; i++) {
				c1[i + point1] = sa2[i];
				c2[i + point1] = sa1[i];
			}

			child++;
			if (child < poplCount)
				children[child] = c1;
			child++;
			if (child < poplCount)
				children[child] = c2;

			if (child == poplCount) {
				break;
			}

		}

		return children;
	}

	public int getRandomWithExclusion(Random rnd, int start, int end, int... exclude) {
		int random = start + rnd.nextInt(end - start + 1 - exclude.length);
		for (int ex : exclude) {
			if (random < ex) {
				break;
			}
			random++;
		}
		return random;
	}

}
