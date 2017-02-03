package competition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import evaluation.WindFarmLayoutEvaluator;

public class GA {
	private WindFarmLayoutEvaluator wfle;
	private boolean[][] pops;
	private double[] fits;
	private Integer[] turbines;
	private Random rand = new Random();
	private int num_pop;
	private int tour_size;
	private double mut_rate;
	private double cross_rate;
	private List<double[]> grid = new ArrayList<double[]>();

	SelectionMethods sms;
	CrossOvers co;

	private int eliteCount = 5;

	public GA(WindFarmLayoutEvaluator evaluator) {
		wfle = evaluator;
		num_pop = 20;
		tour_size = 4;
		mut_rate = 0.05;
		cross_rate = 0.40;
		sms = new SelectionMethods();
		co = new CrossOvers();
	}

	private void evaluate(int iteration) {
		int gridSize = grid.size();
		turbines = new Integer[num_pop];
		double[][] fitLayout = null;
		double minfit = Double.MAX_VALUE;
		int tCount = 0;

		for (int p = 0; p < num_pop; p++) {
			int nturbines = 0;
			for (int i = 0; i < grid.size(); i++) {
				if (pops[p][i]) {
					nturbines++;
				}
			}
			turbines[p] = nturbines;

			// we get nturbines count from previous loop
			// formulate new layout where turbines are true
			double[][] layout = new double[nturbines][2];
			int l_i = 0;
			for (int i = 0; i < grid.size(); i++) {
				if (pops[p][i]) {
					layout[l_i][0] = grid.get(i)[0];
					layout[l_i][1] = grid.get(i)[1];
					l_i++;
				}
			}

			double coe = 0;
			if (iteration > 1 && p >= (num_pop - eliteCount)) {
				coe = fits[p];
			} else if (wfle.checkConstraint(layout)) {
				// XXX: Following lines cost one evaluation in competition mode!
				wfle.evaluate(layout);
				coe = wfle.getEnergyCost();
			} else {
				coe = Double.MAX_VALUE;
			}

			if (p == 0) {
				minfit = coe;
			}

			// Out.createCSVFile(p + "_layout_data.csv");
			// Out.writeCSVData(layout);

			fits[p] = coe;
			if (fits[p] < minfit) {
				minfit = fits[p];
				tCount = turbines[p];
				fitLayout = layout;
			}
			Out.writeOut(p, "| TCOUNT : " + turbines[p] + "/" + gridSize + " | FIT => " + coe * 10000 + "  |", false);
		}

		Out.writeFitness(minfit);
		Out.createCSVFile(iteration + 1 + "_data.csv");
		Out.writeCSVData(fitLayout);
		Out.writeOut(iteration, " Turbines =>  " + tCount + " |  Minimal fitness in population => " + (minfit * 10000 ) ,
				true);
		System.out.println(tCount + " <-- Minimal fitness in population: " + minfit);
	}

	public void run() {
		// set up grid
		// centers must be > 8*R apart
		// double interval = 8.001 * wfle.getTurbineRadius();
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

		// mTournaments Selection
		// Bala : true for all farms around edges x=0 & y=0
		// for (int p = 0; p < num_pop; p++) {
		// for (int i = 0; i < grid.size(); i++) {
		// pops[p][i] = rand.nextBoolean();
		// }
		// }

		// mTournaments Selection
		// Bala : true for all farms around edges x=0 & y=0

		/*
		 * for (int p = 0; p < num_pop; p++) { float edgeCondition =
		 * rand.nextFloat(); for (int i = 0; i < grid.size(); i++) { if
		 * (edgeCondition < 0.40) { populateEdges(p, i, 0.75); } else if
		 * (edgeCondition < 0.50) { populateEdges(p, i, 0.60); } else {
		 * pops[p][i] = rand.nextBoolean(); } } }
		 */

		double xPt = grid.get(0)[0];
		int row = 1;
		float constant = 0.00f;
		for (int p = 0; p < num_pop; p++) {

			if (p <= 8) {
				for (int i = 0; i < grid.size(); i++) {

					double nXPt = grid.get(i)[0];

					if (xPt == nXPt) {
						pops[p][i] = rand.nextBoolean(); // set random
						float r = rand.nextFloat();
						if (p % 2 == 0) { // Alternative rows
							pops[p][i] = false; // default false
							if ((row + p) % 2 == 0 && rand.nextFloat() < 0.90) {
								pops[p][i] = true;
							}
						} else { // alternative layout
							pops[p][i] = true; // default false
							if ((row + p) % 2 == 0 && rand.nextFloat() < 0.90) {
								pops[p][i] = false;
							}
						}
					} else {
						xPt = grid.get(i)[0];
						row++;
					}
				}
			} else {
//				constant += (0.75 / (num_pop-6));
				constant += (0.75 / (num_pop-8));
				// float edgeCondition = rand.nextFloat();
				for (int i = 0; i < grid.size(); i++) {
					if (rand.nextFloat() < constant) { // .40is good
						pops[p][i] = true;
					} else {
						pops[p][i] = rand.nextBoolean();
					}

				}
			}
		}

		/*
		 * float constant = 0.10f; for (int p = 0; p < num_pop; p++) { if(p>=5){
		 * constant += 0.75/(num_pop-2); } System.out.println(constant); //float
		 * edgeCondition = rand.nextFloat(); for (int i = 0; i < grid.size();
		 * i++) { if(rand.nextFloat() < constant){ // .40is good pops[p][i] =
		 * true; } else{ pops[p][i] = rand.nextBoolean(); }
		 * 
		 * } }
		 */

		// best for now
		/*
		 * for (int p = 0; p < num_pop; p++) { //float edgeCondition =
		 * rand.nextFloat(); for (int i = 0; i < grid.size(); i++) {
		 * if(rand.nextFloat() < 0.20){ // .40is good pops[p][i] = true; } else{
		 * pops[p][i] = rand.nextBoolean(); }
		 * 
		 * } }
		 */

		// evaluate initial populations (uses num_pop evals)
		evaluate(0);

		// GA
		for (int i = 0; i < (2000 / (num_pop - 5)); i++) { // 133

			// rank populations (tournament)
			int[] winners = new int[num_pop / 2];
			int[] competitors = new int[num_pop];

			for (int c = 0; c < competitors.length; c++) {
				competitors[c] = c;
			}

			// shuffling the indices
			for (int c = 0; c < competitors.length; c++) {
				int index = rand.nextInt(c + 1);
				int temp = competitors[index];
				competitors[index] = competitors[c];
				competitors[c] = temp;
			}

			// List<Integer> tList = Arrays.asList(turbines);
			boolean[][] eliteParents = new boolean[eliteCount][grid.size()];
			double[] eliteFit = new double[eliteCount];
			List<Double> winnerList = new ArrayList<>();
			for (int t = 0; t < winners.length; t++) {
				int winner = -1;
				double winner_fit = Double.MAX_VALUE;
				int jIndex = -1;

				for (int j = 0; j < competitors.length; j++) {
					int competitor = competitors[j];
					if (competitor != -1 && fits[competitor] < winner_fit && !winnerList.contains(winner_fit)) {
						winner = competitor;
						winner_fit = fits[winner];
						jIndex = j;
					}
				}
				competitors[jIndex] = -1;
				winners[t] = winner;
				winnerList.add(winner_fit);
				// Short listing elite parents for next iteration
				if (t < eliteCount) {
					eliteParents[t] = pops[winner];
					eliteFit[t] = fits[winner];
				}
			}

			boolean[][] children = null;
			// children = co.twoPointCrossOver(num_pop, grid, winners, pops);
			children = co.threeParentCrossOver(num_pop, grid, winners, pops);

			// mutate
			for (int c = 0; c < (num_pop - eliteCount); c++) {
				for (int j = 0; j < children[c].length; j++) {
					if (rand.nextDouble() < mut_rate) {
						children[c][j] = !children[c][j];
						// children[c][j] = true;
					}
				}
			}

			// elitism
			for (int c = 0; c < eliteParents.length; c++) {
				children[num_pop - eliteCount + c] = eliteParents[c];
				fits[num_pop - eliteCount + c] = eliteFit[c];
			}

			pops = children;

			// evaluate
			evaluate(i + 1);
		}
	}

	private void populateEdges(int p, int i, double probability) {
		double xPt = grid.get(i)[0];
		double yPt = grid.get(i)[1];

		// System.out.println(xPt +";"+ yPt);

		// Along x or y ==0 and for 75% probability
		if ((xPt == 0.0 || yPt == 0.0) && rand.nextFloat() < probability) {
			pops[p][i] = true;
		}
		// Along x or y ==max and for 75% probability
		else if ((xPt == grid.get(grid.size() - 1)[0] || yPt == grid.get(grid.size() - 1)[1])
				&& rand.nextFloat() < probability) {
			pops[p][i] = true;
		} else
			pops[p][i] = rand.nextBoolean();
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
