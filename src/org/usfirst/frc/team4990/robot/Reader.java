package org.usfirst.frc.team4990.robot;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Reader {
	
	public static String read(String path) {
		String finalresult = "";
		String line = "Error :(";
		File file = new File(path);
		
		try{
			Scanner scan = new Scanner(file);
			
			while (scan.hasNextLine()){
				line = scan.nextLine();
				finalresult = finalresult + line;
			}
			scan.close();
			return "Reading: " + finalresult;
			
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return line;
	}
		
}