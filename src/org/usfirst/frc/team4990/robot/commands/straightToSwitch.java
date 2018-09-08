package org.usfirst.frc.team4990.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class straightToSwitch extends CommandGroup {
	
	public straightToSwitch() {
		this.addParallel(new gyroStraight(200)); //check number (in feet? inches?)
		this.addParallel(new ultrasonicStop(20, this)); //check number (in inches)
	}
}
