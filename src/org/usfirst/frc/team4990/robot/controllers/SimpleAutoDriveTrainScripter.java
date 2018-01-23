package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.controllers.AutoDriveTrainScripter;

public class SimpleAutoDriveTrainScripter extends AutoDriveTrainScripter {
	
	protected void init() {
		System.out.println("Initing");
		wait(1000.0);
		forwardDistance(3.0);
	}
	
	// Do not modify below por favor (this means "please" in Spanish)
	public SimpleAutoDriveTrainScripter(DriveTrain dtrain) {
		super(dtrain);
		this.init();
	}
	
	public void update() {
		super.update();
	}

}