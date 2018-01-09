package org.usfirst.frc.team4990.robot;

import java.io.*;
import java.text.*;
import java.util.Scanner;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.subsystems.Forklift;

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
	
	public void profileForklift(Forklift forklift) {
		SmartDashboard.putBoolean("isAbove", forklift.isCarriageAbove());
		SmartDashboard.putBoolean("isBelow", forklift.isCarriageBelow());
		
		System.out.println("top switch curr count: "+ forklift.topSwitchCurrCount() + "; top switch last count: " + forklift.topSwitchLastCount());
		System.out.println("bottom switch curr count: "+ forklift.bottomSwitchCurrCount() + "; bottom switch last count: " + forklift.bottomSwitchLastCount());
	}
}