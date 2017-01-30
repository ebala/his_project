package evaluation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Try {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int[] ex = new int[20];
		 
		for (int i = 0; i < ex.length; i++) {
			int v = Try.getRandom(new Random(), 0, 19, ex);
			System.out.println(v);
			ex[i] = v;
			
		}
		
	}

	
	public static int getRandom(Random rnd, int start, int end, int... exclude) {
		int random = start + rnd.nextInt(end - start + 1);
		
		Integer[] newArray = new Integer[exclude.length];
		int i = 0;
		for (int value : exclude) {
		    newArray[i++] = Integer.valueOf(value);
		}
		
		 List<Integer> list = Arrays.asList((Integer[]) newArray);
		 
		 while(list.contains(random)){
			 random = start + rnd.nextInt(end - start + 1);
		 }
		return random;
	}
}
