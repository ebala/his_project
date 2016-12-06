package competition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import evaluation.WindFarmLayoutEvaluator;

public class GA {
	private WindFarmLayoutEvaluator wfle;
	private boolean[][] pops;
	private double[] fits;
	private Random rand = new Random();
	private int num_pop;
	private int tour_size;
	private double mut_rate;
	private double cross_rate;
	private List<double[]> grid = new ArrayList<double[]>();

	SelectionMethods sms;
	CrossOvers co;

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
		double minfit = Double.MAX_VALUE;
		int tCount = 0;
		int[] turbines = new int[num_pop];
		
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

			double coe;
			if (wfle.checkConstraint(layout)) {
				// XXX: Following lines cost one evaluation in competition mode!
				wfle.evaluate(layout);
				coe = wfle.getEnergyCost();
			} else {
				coe = Double.MAX_VALUE;
			}
			
			if(p==0){
				minfit = coe;
			}
//			System.out.println(turbines[p] + " <= coe => " + minfit);
//			Out.writeOut(iteration/10, tCount, minfit);
			
			fits[p] = coe;
			if (fits[p] < minfit) {
				minfit = fits[p];
				tCount = turbines[p];
			}
			Out.writeOut(p, "| TCOUNT : " +turbines[p] + " | FIT => " + minfit + "  |", false);
		}
		Out.writeOut(iteration, " Turbines =>  "+tCount + " |  Minimal fitness in population => " + minfit , true);
		System.out.println(tCount + " <-- Minimal fitness in population: " + minfit);
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

		// mTournaments Selection
		// Bala : true for all farms around edges x=0 & y=0
		for (int p = 0; p < num_pop; p++) {
			int tNrs = 0;
//			int[] turbines = new int[grid.size()];
			for (int i = 0; i < grid.size(); i++) {
				// System.out.println("X - Axis --> " + grid.get(i)[0]);
				// System.out.println("Y - Axis --> " + grid.get(i)[1]);
				/*double v =((double) (p+1)/num_pop) * rand.nextDouble();
//				System.out.println("     v   ===>  " +v);
				if(v > 0.5){
					pops[p][i] = true;
					tNrs++;
				}else{
					pops[p][i] = rand.nextBoolean();
					if(pops[p][i])
						tNrs++;
				}*/
				//int pos = getRandomWithExclusion(rand, 0, grid.size()-1, Arrays.copyOf(turbines, i));
//				int pos = rand.nextInt(grid.size());
//				turbines[i]= pos;
//				
//				if(rand.nextFloat() > 0.25){
//					pops[p][pos] = true;
//					tNrs++;
//				}else{
//					pops[p][pos] = false;
//				}
					pops[p][i] = rand.nextBoolean();
//				}
			}
//			System.out.println("T's set ==> " + tNrs);
//			System.out.println(Arrays.toString(turbines));
		}

		// evaluate initial populations (uses num_pop evals)
		evaluate(0);

		// GA
		for (int i = 0; i < (2000 / num_pop); i++) {

			// rank populations (tournament)
			int[] winners = new int[num_pop / tour_size];
			int[] competitors = new int[num_pop];
			for (int c = 0; c < competitors.length; c++) {
				competitors[c] = c;
			}

			// Shuffle competitors
	/*		for (int c = 0; c < competitors.length; c++) {
				int index = rand.nextInt(c + 1);
				int temp = competitors[index];
				competitors[index] = competitors[c];
				competitors[c] = temp;
			}

			// Selecting best from 0-4 | 4-8 | 8- 12 | 12 -16 and so on
			for (int t = 0; t < winners.length; t++) {
				int winner = -1;
				double winner_fit = Double.MAX_VALUE;
				for (int c = 0; c < tour_size; c++) {
					int competitor = competitors[tour_size * t + c];
					if (fits[competitor] < winner_fit) {
						winner = competitor;
						winner_fit = fits[winner];
					}
				}
				winners[t] = winner;
			}*/
			

			
			for (int t = 0; t < winners.length; t++) {
				int winner = -1;
				int index =0, indSel=0;
				double winner_fit = Double.MAX_VALUE;
				
				for (int j = 0; j < competitors.length; j++) {
					int competitor = competitors[j];
					if(competitor == -1)
						continue;
					if (fits[competitor] < winner_fit) {
						winner = competitor;
						winner_fit = fits[winner];
					}
				}
				competitors[winner] = -1;
				winners[t] = winner;
				
				/*for (int c = 0; c < num_pop; c++) {
					int competitor = competitors[c];
					if (fits[competitor] < winner_fit) {
						winner = competitor;
						winner_fit = fits[winner];
					}
				}*/
			}
			

			// int[] winners = sms.StochasticSampling(fits, 10);

			// System.out.println(Arrays.toString(winners));

			boolean[][] children = co.threeParentCrossOver(num_pop, grid, winners, pops);
			// crossover
			/*
			 * boolean[][] children = new boolean[num_pop][grid.size()];
			 * 
			 * for (int c = 0; c < (num_pop - winners.length); c++) { int s1 =
			 * getRandomWithExclusion(rand, 0, winners.length-1); int s2 =
			 * getRandomWithExclusion(rand, 0, winners.length-1, s1); int s3 =
			 * getRandomWithExclusion(rand, 0, winners.length-1, s1,s2);
			 */
			// if (s2 >= s1) {
			// s2++;
			// }
			// if(s3 >= s2){
			// s3++;
			// }

			/*
			 * int p1 = winners[s1]; int p2 = winners[s2]; int p3 = winners[s3];
			 * boolean[] parent1 = pops[p1]; boolean[] parent2 = pops[p2];
			 * boolean[] parent3 = pops[p3];
			 * 
			 * boolean[] child = new boolean[grid.size()]; for (int j = 0; j <
			 * child.length; j++) {
			 * 
			 * //Three Parents /*if(parent1[j] == parent2[j] == parent3[j]){
			 * child[j] = parent1[j]; }else if(parent1[j] == parent2[j] ||
			 * parent1[j] == parent3[j]){ child[j] = parent1[j]; }else
			 * if(parent2[j] == parent3[j]){ child[j] =parent2[j]; }else{
			 * child[j] = rand.nextBoolean(); }
			 */

			// modification start
			// if (parent2[j] && parent1[j]) {
			// child[j] = true;
			// } else { // modification end

			/*
			 * if (rand.nextDouble() < cross_rate) { child[j] = parent2[j]; }
			 * else { child[j] = parent1[j]; }
			 */

			// uniform
			/*
			 * if (rand.nextBoolean()) { child[j] = parent2[j]; } else {
			 * child[j] = parent1[j]; }
			 * 
			 * // } }
			 * 
			 * children[c] = child; }
			 */

			// mutate
			int m =0;
			for (int c = 0; c < (num_pop ); c++) {
				/*int mCount = (int) (mut_rate * children[c].length);
				
				int[] mutPoints = new int[mCount];
				
				for (int j = 0; j < mutPoints.length; j++) {
					int r = getRandomWithExclusion(rand, 0, children[c].length-1, Arrays.copyOf(mutPoints, j));
					mutPoints[j]=r;
					children[c][r] = !children[c][r];
					m++;
				}
				
				System.out.println(c + " : Mutation --> " + m);*/
				for (int j = 0; j < children[c].length; j++) {
					if (rand.nextDouble() < mut_rate) {
//						children[c][j] = !children[c][j];
						children[c][j] = true;
					}
				}
			}
			
//			System.out.println("Mutation --> " + m);

			// elitism
			for (int c = 0; c < winners.length; c++) {
				children[num_pop - winners.length + c] = pops[winners[c]];
			}

			pops = children;

			// evaluate
			evaluate(i);
		}
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
