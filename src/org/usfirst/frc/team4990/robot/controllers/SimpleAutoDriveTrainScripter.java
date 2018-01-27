package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;

import org.usfirst.frc.team4990.robot.controllers.AutoDriveTrainScripter;

public class SimpleAutoDriveTrainScripter extends AutoDriveTrainScripter {
	
	public enum StartingPosition {LEFT, MID, RIGHT, ERROR, STAY, FORWARD};
	
	protected void init(StartingPosition s) {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		if (gameData.length() == 0 || s == StartingPosition.FORWARD) {//if there is no game message (string) OR just cross auto line
			//go forward and cross auto line
			forwardDistance(12,false);
		} else if (gameData.charAt(0) == 'L') {//Left side is ours
		switch(s) {
			case LEFT:
				//1st: forward ~162 in
				//2nd: 90º turn right
				//3rd: forward 55in
				//4th: drop cube
				break;
			case MID:
				//cut in front (in between auto line and exchange)
					//1st: forward 80 in
					//2nd: turn 90º left
					//3rd: forward about 60 in
					//4th: turn 90º right
					//5th: forward 60 in
				break;
			case RIGHT:
				//1st: forward 230 in
				//2nd: turn 90º left
				//3rd: forward 150 in
				//4th: turn 90º left
				//5th: forward 30 in
				//6th: drop cube
				break;
			case ERROR: //debug/error case
				//turnDistance(10);
				debugEncoders(0.2);
				//turnForDegrees(90, "l");
				System.err.println("StartingPosition == Error. THAT DOESN'T WORK");
				break;
			case STAY: //FREEZE!
				break;
		}
		} else if (gameData.charAt(0) == 'R') {//Right 
			switch(s) {
				case LEFT:
					//1st: forward 230 in
					//2nd: turn 90º right
					//3rd: forward 150 in
					//4th: turn 90º right
					//5th: forward 30 in
					//6th: drop cube 
					break;
				case MID:
					//1st: forward 140 in
					//2nd: drop cube
					break;
				case RIGHT:
					//1st: forward ~162 in
					//2nd: 90º turn left
					//3rd: forward 55in
					//4th: drop cube
					break;
				case ERROR: //debug/error case
					break;
				case STAY: //FREEZE!
					System.out.println("I'm not going to move.  Sucks to be you.");
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
		super.init();
		//turnForDegrees(90,"l");
		//forwardDistance(3);
	}
	
	public void update() {
		super.update();
	}

}