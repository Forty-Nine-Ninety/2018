package org.usfirst.frc.team4990.robot;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {

	private File log;
	
	FileLogger(String filePath)
	{
		log = new File(filePath);
		if(!log.isFile() )
		{
			try {
				log.getParentFile().mkdirs();
				log.createNewFile();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
			
	}
	
	public void writeToLog(String text)
	{
		DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/YYYY");
		Date date = new Date();
		
		try {
			PrintWriter pw = new PrintWriter(
							 new BufferedWriter(
							 new FileWriter(log, true)), true);
			
			
			
			pw.println(df.format(date) + " "  + text);
		
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
