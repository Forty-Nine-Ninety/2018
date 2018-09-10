package org.usfirst.frc.team4990.robot;

import java.io.*;
import java.text.*;
import java.util.Scanner;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

@SuppressWarnings("unused")
public class Logger {
	
	private FileLogger fileLogger;
	
	public Logger() {
		fileLogger = new FileLogger("/home/lvuser/log/logs.txt");
	}
	
	
	
	public void profileDriveTrain(DriveTrain driveTrain) {
		SmartDashboard.putNumber("left set speed", driveTrain.left.getSetSpeed());
		SmartDashboard.putNumber("right set speed", driveTrain.right.getSetSpeed());
		SmartDashboard.putNumber("left measured speed", driveTrain.left.getVelocity());
		SmartDashboard.putNumber("right measured speed", driveTrain.right.getVelocity());
		
		fileLogger.writeToLog("left set speed" + driveTrain.left.getSetSpeed());
		//File Logger purposes
		
		System.out.println("left set speed: " + driveTrain.left.getSetSpeed());
		System.out.println("right set speed: " + driveTrain.right.getSetSpeed());
		System.out.println("left measured speed: " + driveTrain.left.getVelocity());
		System.out.println("right measured speed: " + driveTrain.right.getVelocity());
		
		
	}
}
	