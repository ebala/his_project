package competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Out {

	private static String filePath = "/var/www/html/output.txt";

	private static boolean writeOutput = true;
	
	public static void createFile() {
		File file = new File(filePath);

		if (file.exists()) {
			Date d = new Date();
			file.renameTo(new File("output_" + d.toString() + ".txt"));
		}else{
			writeOutput = false;
			return;
		}
		try {
			 file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			PrintWriter out = new PrintWriter(bw);
			out.println("No Edges | No elitism | All 20 children from 3 parent crossover | ");
			out.println("======================================================================");
			out.println("");
			out.println("|Iteration    |Turbine Count     |Fitness Value");
			out.close();
			bw.close();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeOut(int it, int tCount, double fv) {
		if(!writeOutput){
			return;
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath),true));
			PrintWriter out = new PrintWriter(bw);
			out.println("|" + it + "      |" + tCount + "    |" + fv + "   |");
			out.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
