package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot.StartingPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomusCommand extends CommandGroup {
	public AutonomusCommand(StartingPosition s, String gameData) {
		System.out.println("Auto Logic INIT");
		
		if (gameData.length() == 0 || s == StartingPosition.FORWARD) {
			//if there is no game message (string) OR just cross auto line
			//System.out.println("Only Crossing Auto Line: gyroStraight((140/12), true)");
			addSequential(new gyroStraight(140/12)); //forward 140 in 
		} else if (gameData.charAt(0) == 'L') {
			System.err.println("Auto Init. Game data = " + gameData + " Position = " + s.toString());
			//Left side is ours
			switch(s) {
				case LEFT: 
					addSequential(new MoveElevatorTime(2.5, 0.7)); //moveElevator(2.25);
					addSequential(new gyroStraight(162/12));//1st: forward ~162 in
					addSequential(new gyroTurn(90));//2nd: 90 degree turn to the right
					addSequential(new gyroStraight(55/12));//3rd: forward 55in
					//4th: drop cube
					//runIntake(false);
					break;
				case MID: 
					addSequential(new MoveElevatorTime(2.5, 0.7)); //moveElevator(2.25);
					//cut in front (in between auto line and exchange)
					addSequential(new gyroStraight(80/12));//1st: forward 80 in
					addSequential(new gyroTurn(-90));//2nd: 90 degree turn to the left
					addSequential(new gyroStraight(60/12));//3rd: forward about 60 in
					addSequential(new gyroTurn(90));//4th: 90 degree turn to the right
					addSequential(new gyroStraight(60/12));//5th: forward 60 in
					//runIntake(false);
					//6th: drop cube
					break;
				case RIGHT: 
					addSequential(new MoveElevatorTime(2.5, 0.7)); //moveElevator(2.25);
					addSequential(new gyroStraight(230/12)); //1st: forward 230 in
					addSequential(new gyroTurn(-90)); //2nd: 90 degree turn to the left
					addSequential(new gyroStraight(150/12));//3rd: forward 150 in
					addSequential(new gyroTurn(-90)); //4th: 90 degree turn to the left
					addSequential(new gyroStraight(30/12)); //5th: forward 30 in
					//runIntake(false);
					//6th: drop cube
					break;
				case ERROR: //error case
					//System.err.println("StartingPosition == Error. THAT DOESN'T WORK");
					break;
				case STAY: //FREEZE!
					//System.out.println("Case STAY, Side Left. I'm not going to move. Sucks to be you.");
					break;
				default:
					//System.out.println("Default case of gameData LEFT activated. Going forward.");
					//System.out.println("Only Crossing Auto Line: gyroStraight((140/12), true)");
					addSequential(new gyroStraight(140/12)); //forward 140 in 
			}
		} else if (gameData.charAt(0) == 'R') {//Right
			System.out.println("Auto Init. Game data = " + gameData + " Position = " + s.toString());
			switch(s) {
				case LEFT: 
					addSequential(new MoveElevatorTime(2.5, 0.7)); //moveElevator(2.25);
					addSequential(new gyroStraight(230/12)); //1st: forward 230 in
					addSequential(new gyroTurn(90));	 //2nd: 90 degree turn to the right
					addSequential(new gyroStraight(150/12)); //3rd: forward 150 in
					addSequential(new gyroTurn(90));	//4th: 90 degree turn to the right
					addSequential(new gyroStraight(30/12));	//5th: forward 30 in
					//6th: drop cube
					//runIntake(false);
					break;
				case MID: 
					addSequential(new MoveElevatorTime(2.5, 0.7)); //moveElevator(2.25);
					addSequential(new gyroStraight(140/12));//1st: forward 140 in
					//2nd: drop cube
					//runIntake(false);
					break;
				case RIGHT:
					addSequential(new MoveElevatorTime(2.5, 0.7)); //moveElevator(2.25);
					addSequential(new gyroStraight(162/12));//1st: forward ~162 in
					addSequential(new gyroTurn(-90));//2nd: 90 degree turn to the left
					addSequential(new gyroStraight(55/12));//3rd: forward 55in
					//4th: drop cube
					//runIntake(false);
					break;
				case ERROR: //debug/error case
					//System.out.println("StartingPosition == Error. THAT DOESN'T WORK");
					break;
				case STAY: //FREEZE!
					//System.out.println("Case STAY, Side right. I'm not going to move. Sucks to be you.");
					break;
				default:
					//System.out.println("Default case of gameData RIGHT activated. Going forward.");
					//System.out.println("Only Crossing Auto Line: gyroStraight((140/12), true)");
					addSequential(new gyroStraight(140/12)); //forward 140 in 
			}
		} else {
			//System.out.println("Error: Unknown game data: " + gameData.charAt(0) + " StartingPostion " + s.toString());
			//System.out.println("Only Crossing Auto Line: gyroStraight((140/12), true)");
			addSequential(new gyroStraight(140/12)); //forward 140 in 
		}
	}
}
