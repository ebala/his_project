package competition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import evaluation.WindFarmLayoutEvaluator;

public class GA1 {
	private WindFarmLayoutEvaluator wfle;
	private boolean[][] pops;
	private double[] fits;
	private Random rand = new Random();
	private int num_pop;
	private int tour_size;
	private double mut_rate;
	private double cross_rate;
	private List<double[]> grid = new ArrayList<double[]>();

	public GA1(WindFarmLayoutEvaluator evaluator) {
		wfle = evaluator;
		num_pop = 20;
		tour_size = 4;
		mut_rate = 0.05;
		cross_rate = 0.40;
	}

	private void evaluate(int iteration) {
		double minfit = Double.MAX_VALUE;
		int tCount = 0;
		
		for (int p = 0; p < num_pop; p++) {
			int nturbines = 0;
			for (int i = 0; i < grid.size(); i++) {
				if (pops[p][i]) {
					nturbines++;
				}
			}
			tCount = nturbines;

			double[][] layout = new double[nturbines][2];
			int l_i = 0;
			for (int i = 0; i < grid.size(); i++) {
				if (pops[p][i]) {
					layout[l_i][0] = grid.get(i)[0];
					layout[l_i][1] = grid.get(i)[1];
					l_i++;
				}
			}

			double coe;
			if (wfle.checkConstraint(layout)) {
				// XXX: Following lines cost one evaluation in competition mode!
				wfle.evaluate(layout);
				coe = wfle.getEnergyCost();
			} else {
				coe = Double.MAX_VALUE;
			}
			fits[p] = coe;
			if (fits[p] < minfit) {
				minfit = fits[p];
			}
		}
//		Out.writeOut(iteration, tCount, minfit);
		System.out.println(iteration + ". Turbine Count => " + tCount +
				"  | Minimal fitness in population: " + minfit * 10000);
	}

	
	
	public void run() {
		// set up grid
		// centers must be > 8*R apart
		double interval = 8.001 * wfle.getTurbineRadius();

		for (double x = 0.0; x < wfle.getFarmWidth(); x += interval) {
			for (double y = 0.0; y < wfle.getFarmHeight(); y += interval) {
				boolean valid = true;
				for (int o = 0; o < wfle.getObstacles().length; o++) {
					double[] obs = wfle.getObstacles()[o];
					if (x > obs[0] && y > obs[1] && x < obs[2] && y < obs[3]) {
						valid = false;
					}
				}

				if (valid) {
					double[] point = { x, y };
					grid.add(point);
				}
			}
		}

		// initialize populations
		pops = new boolean[num_pop][grid.size()];
		fits = new double[num_pop];

		for (int p = 0; p < num_pop; p++) {
			for (int i = 0; i < grid.size(); i++) {
				pops[p][i] = rand.nextBoolean();
			}
		}

		// evaluate initial populations (uses num_pop evals)
		evaluate(0);

		// GA
		for (int i = 0; i < (2000 / num_pop); i++) {

		
			SelectionMethods sms = new SelectionMethods();
			
			int[] winners = sms.StochasticSampling(fits, 5);
			//int[] winners = sms.rankPopulation(fits, 5);

			// crossover
			boolean[][] children = new boolean[num_pop][grid.size()];

			// Why reduce to 15 ?
			for (int c = 0; c < (num_pop - winners.length); c++) {
			//for (int c = 0; c < num_pop; c++) {
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

			// mutate
			for (int c = 0; c < (num_pop - winners.length); c++) { // 15
				for (int j = 0; j < children[c].length; j++) { // 1029
					if (rand.nextDouble() < mut_rate) {
						children[c][j] = !children[c][j];
					}
				}
			}

			// After 15 new breeds, we retain the winner 5 breeds to make 20 new population
			// elitism
			for (int c = 0; c < winners.length; c++) {
				children[num_pop - winners.length + c] = pops[winners[c]];
			}

			pops = children;

			// evaluate
			evaluate(i);
		}
	}
	
	
}
