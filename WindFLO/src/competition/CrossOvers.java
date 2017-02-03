package competition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

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

		for (int c = 0; c < (poplCount - 5); c++) {
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

		/*Set<Integer> selectedP1 =new HashSet();
		int s1 = getRandom( 0, winners.length - 1);
		selectedP1.add(s1);*/
		
		for (int c = 0; c < (poplCount - 5); c++) {
			
		/*	if(selectedP1.size() == winners.length){
				selectedP1 =new HashSet();
				s1 = getRandom( 0, winners.length - 1);
				selectedP1.add(s1);
			}
			
			while(c!=0 && selectedP1.contains(s1)){
				s1=getRandom( 0, winners.length - 1);
				if(!selectedP1.contains(s1))
					break;
			}
			selectedP1.add(s1);
			
			int s2=0 ;
			if(s1+1< winners.length-1)
				s2 = s1+1;
			
			int s3 =0;
			if(s2+1< winners.length-1)
				s3 = s2+1;
			*/
			
			int[] winCopy = Arrays.copyOf(winners, winners.length);
			
			int s1 = getRandom(rand,0, winCopy.length - 1);
			int p1 = winCopy[s1];
//			winCopy = getTrimmedArray(winCopy, s1);
			int s2 = getRandom(rand, 0, winCopy.length - 1,s1);
			int p2 = winCopy[s2];
//			winCopy = getTrimmedArray(winCopy, s2);
			int s3 = getRandom(rand, 0, winCopy.length - 1,s1,s2);
			int p3 = winCopy[s3];

			
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
					child[j] = parent3[j];
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

		int loopCnt = poplCount-5;
		
		for (int c = 0; c < loopCnt; c++) {
			int s1 = getRandom(rand, 0, winners.length - 1);
			int s2 = getRandom(rand, 0, winners.length - 1, s1);

			boolean[] parent1 = pops[winners[s1]];
			boolean[] parent2 = pops[winners[s2]];
			boolean[] c1 = parent1;
			boolean[] c2 = parent2;

			int point1 = getRandom(rand, 0, parent1.length - 1);
			int point2 = getRandom(rand, 0, parent1.length - 1, point1);

			int diff = Math.abs(point2 - point1);

			if (point1 > point2)
				point1 = point2;

			boolean[] sa1 = Arrays.copyOfRange(parent1, point1, point1 + diff);
			boolean[] sa2 = Arrays.copyOfRange(parent2, point1, point1 + diff);

			for (int i = 0; i < sa1.length; i++) {
				c1[i + point1] = sa2[i];
				c2[i + point1] = sa1[i];
			}
			

			if(rand.nextBoolean())
				children[c] = c1;
			else
				children[c] = c2;

		}

		return children;
	}

	
	public int[] getTrimmedArray(int[] src, int remIndex){
    	
    	int[] result = new int[src.length - 1];
    	System.arraycopy(src, 0, result, 0, remIndex);
    	if (src.length != remIndex) {
    	    System.arraycopy(src, remIndex + 1, result, remIndex, src.length - remIndex - 1);
    	}
		return result;
	}
	
/*	public int getRandom(int start, int end, Integer... exclude) {
		int random = start + rand.nextInt(end - start + 1 - exclude.length);
		for (int ex : exclude) {
			if (random < ex) {
				break;
			}
			random++;
		}
		return random;
	}
	*/
	/**
	 * 
	 * @param rnd
	 * @param start
	 * @param end
	 * @param exclude
	 * @return
	 */
	public int getRandom(Random rnd, int start, int end, int... exclude) {
		int random = start + rnd.nextInt(end - start + 1);
		
		Integer[] newArray = new Integer[exclude.length];
		int i = 0;
		for (int value : exclude) {
		    newArray[i++] = Integer.valueOf(value);
		}
		
		 List<Integer> list = Arrays.asList((Integer[]) newArray);
		 
		 int failCond =0;
		 while(list.contains(random)){
			 failCond++;
			 random = start + rnd.nextInt(end - start + 1);
			 // Just to be sure that it does not run into infinite loop
			 if(failCond>10)
				 break;
		 }
		return random;
	}

}
