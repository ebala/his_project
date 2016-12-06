package competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Date;

public class Out {

	private static String path = "/var/www/html/";
	private static String filePath = "/var/www/html/output.txt";

	private static boolean writeOutput = true;
	private static File file= null;
	
	public static void createFile() {
		file = new File(filePath);

		try {
			if (file.exists()) {
				Date d = new Date();
				Files.move(file.toPath(), (new File(path + "output_" + d.toString().replaceAll(" ", "_") + ".txt")).toPath());
			}/*else{
				writeOutput = false;
				return;
			}*/
			file =new File(filePath); 
			file.createNewFile();
			file.setWritable(true);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			PrintWriter out = new PrintWriter(bw);
			out.println("No Edges | No elitism | All 20 children from 3 parent crossover | ");
			out.println("======================================================================");
			out.println("");
			out.close();
			bw.close();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public static void writeOut(int it, int tCount, double fv) {
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
	}*/
	
	public static void writeOut(int it, String content,boolean isMinFit) {
		if(!writeOutput){
			return;
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			PrintWriter out = new PrintWriter(bw);
			if(!isMinFit){
				out.println("      |" + it + "      |" + content);
			}else{
				out.println("");
				out.println("                 " + content  );
				out.println("");
				out.println("--------------------------| End of Evaluation "+ it +" |------------------------"  );
			}
			out.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
