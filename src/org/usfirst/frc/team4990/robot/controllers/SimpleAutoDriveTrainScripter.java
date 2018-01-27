package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;

import org.usfirst.frc.team4990.robot.controllers.AutoDriveTrainScripter;

public class SimpleAutoDriveTrainScripter extends AutoDriveTrainScripter {
	
	public enum StartingPosition {LEFT, MID, RIGHT, ERROR, STAY, FORWARD};
	
	protected void init(StartingPosition s) {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		//if (gameData.charAt(0) == 'L') {//Left side is ours
		switch(s) {
			case LEFT:
				debugEncoders(0.3);
				break;
			case MID:
				//forwardDistance(1);
				turnForDegrees(90, "l"); //turns 90 degrees to the left
				break;
			case RIGHT:
				break;
			case ERROR:
				//turnDistance(10);
				debugEncoders(0.2);
				//turnForDegrees(90, "l");
				System.err.println("THAT DOESN'T WORK");
				break;
			case STAY:
				System.out.println("I'm not going to move.  Sucks to be you.");
				break;
			case FORWARD:
				forwardDistance(12);
				break;
		}
		//}
		/*
		else if (gameData.charAt(0) == 'R') {//Right 
			switch(s) {
				case LEFT:
					break;
				case MID:
					break;
				case RIGHT:
					break;
				case ERROR:
					System.err.println("THAT DOESN'T WORK");
					break;
				case STAY:
					System.out.println("I'm not going to move.  Sucks to be you.");
					break;
				case FORWARD:
					forwardDistance(12);
					break;
			}
		}
		else {
			System.out.println("YA MESSED UP PPL IDK WHAT HAPPENED THO HERE'S WHAT I GOT: " + gameData.charAt(0));
		}
		*/
	}
	
	// Do not modify below por favor (this means "please" in Spanish)
	public SimpleAutoDriveTrainScripter(DriveTrain dtrain, StartingPosition s) {
		super(dtrain);
		this.init(s);
		//turnForDegrees(90,"l");
		//forwardDistance(3);
	}
	
	public void update() {
		super.update();
	}

}