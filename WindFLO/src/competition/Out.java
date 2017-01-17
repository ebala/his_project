package competition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Out {

	private static String path = "";
	private static String filePath = "";

	private static boolean writeOutput = true;
	private static File file= null;
	
	public static void createFile(int scenario) {
		path = "D:/HIS/SEM3/HIS_Project/output/" + scenario;
		
		File pathDir = new File(path);
		if(!pathDir.exists()){
			pathDir.mkdirs();
		}
		
		Date d = new Date();
		String date = d.toString().replaceAll(" ", "_").replaceAll(":", "-");
		filePath = path + "/out_" +date  + ".txt";

		
		file = new File(filePath);

		try {
			
			/*if (file.exists()) {
				Files.move(file.toPath(), (new File(path + "output_" + date + ".txt")).toPath());
			}else{
				writeOutput = false;
				return;
			}*/
			file =new File(filePath); 
			file.createNewFile();
			file.setWritable(true);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			PrintWriter out = new PrintWriter(bw);
			out.println("Scenarion : " +scenario + " | Date : " + d.toString());
			out.println("======================== Uniform + m = = .05 | edge sele =< 50% Edge Turb = 75%==============================================");
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
			String outText = "";
			if(!isMinFit){
				outText = "      |" + it + "      |" + content;
				out.println(outText);
			}else{
				out.println("");
				out.println("                 " + content  );
				outText ="--------------------------| End of Evaluation "+ it +" |------------------------"  ;
				out.println(outText);
				out.println("");
				System.out.println(" ................................................... "+content);
			}
			System.out.println(outText);
			out.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
