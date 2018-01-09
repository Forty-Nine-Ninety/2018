package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.controllers.AutoDriveTrainScripter;

public class SimpleAutoDriveTrainScripter extends AutoDriveTrainScripter {
	
	protected void init() {
		//@ -.5 power, 57 inches in one second FORWARD
		//@ -.5 power, approx 390 degrees per second CLOCKWISE
		//
		forward(2580, -.3);
		// a little more than 88 inches^^
		//forward(2982, -.3);
		//turn(350, -.5);
		//forward(1000, .5);
		
		//Please don't mess below if you want to change auto scripting
		super.init();
	}
	public SimpleAutoDriveTrainScripter(DriveTrain dtrain) {
		super(dtrain);
		init();
	}
	
	public void update() {
		super.update();
	}

}