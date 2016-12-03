package competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Out {

	private static String filePath = "../../../out/output.txt";

	public static void createFile() {
		File file = new File(filePath);

		if (file.exists()) {
			file.delete();
		}
		try {
			 file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			PrintWriter out = new PrintWriter(bw);
			out.println("|Iteration    |Turbine Count     |Fitness Value");
			out.close();
			bw.close();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeOut(int it, int tCount, double fv) {
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
