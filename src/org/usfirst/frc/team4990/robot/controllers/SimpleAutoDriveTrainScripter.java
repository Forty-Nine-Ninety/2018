package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;

import org.usfirst.frc.team4990.robot.controllers.AutoDriveTrainScripter;

public class SimpleAutoDriveTrainScripter extends AutoDriveTrainScripter {

	public enum StartingPosition {
		LEFT, 
		MID, 
		RIGHT, 
		ERROR,
		STAY, 
		FORWARD
	};

	public void crossAutoLine() {
		//go forward and cross auto line
		//forward 140 in
		System.out.println("Only Crossing Auto Line: goDistance((140/12), true)");
		goDistance((140/12), true);
	}

	protected void init(StartingPosition s) {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		if (gameData.length() == 0 || s == StartingPosition.FORWARD) {
			//if there is no game message (string) OR just cross auto line
			crossAutoLine();
		} else if (gameData.charAt(0) == 'L') {
			System.out.println("Auto Init. Game data = " + gameData + " Position = " + s.toString());
			//Left side is ours
			switch(s) {
				case LEFT: //Joseph (Benjamin)
					goDistance(162/80, true);//1st: forward ~162 in
					turnForDegrees(90, Direction.RIGHT);//2nd: 90 degree turn to the right
					goDistance((55/12), true);//3rd: forward 55in
					//4th: drop cube
					break;
				case MID: //Benjamin
					//cut in front (in between auto line and exchange)
					goDistance(80/12, true);//1st: forward 80 in
					turnForDegrees(90, Direction.LEFT);//2nd: 90 degree turn to the left
					goDistance(60/12, true);//3rd: forward about 60 in
					turnForDegrees(90, Direction.RIGHT);//4th: 90 degree turn to the right
					goDistance(60/12, true);//5th: forward 60 in
					//6th: drop cube
					break;
				case RIGHT: //Dominic
					goDistance(230/12, true);//1st: forward 230 in
					turnForDegrees(90, Direction.LEFT);	//2nd: 90 degree turn to the left
					goDistance(150/12, true);//3rd: forward 150 in
					turnForDegrees(90, Direction.LEFT);	//4th: 90 degree turn to the left
					goDistance(30/12, true);	//5th: forward 30 in
					//6th: drop cube
					break;
				case ERROR: //debug/error case
					//turnDistance(10);
					//turnForDegrees(90, "l");
					System.err.println("StartingPosition == Error. THAT DOESN'T WORK");
					break;
				case STAY: //FREEZE!
					break;
				default:
					System.out.println("Default case of gameData LEFT activated. Going forward.");
					crossAutoLine();
			}
		} else if (gameData.charAt(0) == 'R') {//Right
			System.out.println("Auto Init. Game data = " + gameData + " Position = " + s.toString());
			switch(s) {
				case LEFT: //Dominic (more like domithiqq)
					goDistance(230/12, true); //1st: forward 230 in
					turnForDegrees(90, Direction.RIGHT);	 //2nd: 90 degree turn to the right
					goDistance(150/12, true); //3rd: forward 150 in
					turnForDegrees(90, Direction.RIGHT);	//4th: 90 degree turn to the right
					goDistance(30/12, true);	//5th: forward 30 in
					//6th: drop cube
					break;
				case MID: //Benjamin
					goDistance((140/12),true);//1st: forward 140 in
					//2nd: drop cube
					break;
				case RIGHT: //Joseph (benjamin)
					goDistance(162/12,true);//1st: forward ~162 in
					turnForDegrees(90, Direction.LEFT);//2nd: 90 degree turn to the left
					goDistance(55/12,true);//3rd: forward 55in
					//4th: drop cube
					break;
				case ERROR: //debug/error case
					break;
				case STAY: //FREEZE!
					System.out.println("I'm not going to move.  Sucks to be you.");
					break;
				default:
					System.out.println("Default case of gameData LEFT activated. Going forward.");
					crossAutoLine();
			}
		}
		else {
			System.out.println("Error: Unknown game data: " + gameData.charAt(0) + " StartingPostion " + s.toString());
		}

	}

	// Do not modify below por favor (this means "please" in Spanish)
	public SimpleAutoDriveTrainScripter(DriveTrain dtrain, StartingPosition startP, ADXRS450_Gyro gyro) {
		super(dtrain, startP, gyro);
		

		// this is for debugging

		gyroTurn(90, Direction.LEFT);

		// end debugging

		//this.init(); //uncomment this line to use Auto logic
		
		// super.init should be last
		super.init();
	}

	public void update() {
		super.update();
	}

}