package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.controllers.AutoDriveTrainScripter;

public class SimpleAutoDriveTrainScripter extends AutoDriveTrainScripter {
	
	protected void init() {
		forwardDistance(3.0);
	}
	public SimpleAutoDriveTrainScripter(DriveTrain dtrain) {
		super(dtrain);
	}
	
	public void update() {
		super.update();
	}

}