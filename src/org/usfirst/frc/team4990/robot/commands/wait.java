package org.usfirst.frc.team4990.robot.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;

public class wait extends TimedCommand {
	
	/**
	 * Makes robot wait.
	 * @param time Time to wait for in seconds
	 */
	public wait(double t) {
		super(t);
		System.out.println("wait(" + t + " sec)");
	}
}
