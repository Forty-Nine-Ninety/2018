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
		SmartDashboard.putNumber("left set speed", driveTrain.getLeftSetSpeed());
		SmartDashboard.putNumber("right set speed", driveTrain.getRightSetSpeed());
		SmartDashboard.putNumber("left measured speed", driveTrain.getLeftVelocity());
		SmartDashboard.putNumber("right measured speed", driveTrain.getRightVelocity());
		
		fileLogger.writeToLog("left set speed" + driveTrain.getLeftSetSpeed());
		//File Logger purposes
		
		System.out.println("left set speed: " + driveTrain.getLeftSetSpeed());
		System.out.println("right set speed: " + driveTrain.getRightSetSpeed());
		System.out.println("left measured speed: " + driveTrain.getLeftVelocity());
		System.out.println("right measured speed: " + driveTrain.getRightVelocity());
		
		
	}
}
	