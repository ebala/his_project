package competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Out {

	private static String basePath = "D:/HIS/SEM3/HIS_Project/trial/";
	private static String path = "";
	private static String filePath = "";

	private static boolean writeOutput = true;
	private static File file = null;

	private static String fitFile = null;

	public static void createFile(String run,int scenario) {
		
		Date d = new Date();
		String date = d.toString().replaceAll(" ", "_").replaceAll(":", "-");
		
		path = basePath + "/" + run + "/" +scenario + "/" + date +"/";

		File pathDir = new File(path);
		if (!pathDir.exists()) {
			pathDir.mkdirs();
		}


		filePath = path + "/output.txt";

		fitFile = path + "/fitness.txt";
		
		try {
			// Create fitness file
			file = new File(fitFile);
			file.createNewFile();
			file.setWritable(true);
		
			// reset
			file = null;

			file = new File(filePath);

			// now create out file
			file = new File(filePath);
			file.createNewFile();
			file.setWritable(true);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			PrintWriter out = new PrintWriter(bw);
			out.println("Scenarion : " + scenario + " | Date : " + d.toString());
			out.println("======================== ==============================================");
			out.println("");
			out.close();
			bw.close();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void writeOut(int it, String content, boolean isMinFit) {
		if (!writeOutput) {
			return;
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			PrintWriter out = new PrintWriter(bw);
			String outText = "";
			if (!isMinFit) {
				outText = "      |" + it + "      |" + content;
				out.println(outText);
			} else {
				out.println("");
				out.println("                 " + content);
				outText = "--------------------------| End of Evaluation " + it + " |------------------------";
				out.println(outText);
				out.println("");
				System.out.println(" ................................................... " + content);
			}
			System.out.println(outText);
			out.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String csvFile;
	public static void createCSVFile(String fName){
		csvFile = path + fName;
		File f = new File(filePath);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void writeCSVData(double[][] layout){
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile,true));
			PrintWriter out = new PrintWriter(bw);
			
			for (int i = 0; i < layout.length; i++) {
				out.println(layout[i][0] + "," + layout[i][1]);
			}
			
			out.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void writeFitness(double val){
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fitFile,true));
			PrintWriter out = new PrintWriter(bw);
			
			out.println(val*10000);
			
			out.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
