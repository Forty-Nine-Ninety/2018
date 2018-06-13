package org.usfirst.frc.team4990.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class wait extends Command {
	private boolean isFinished;
	private long duration;
	private long startMillis;
	
	/**
	 * Makes robot wait
	 * @param time Time to wait for in milliseconds
	 */
	public wait(double t) {
		this.duration = (long) t;
		this.isFinished = false;
	}
	
	/**
	 * Clears time
	 */
	public void initialize() {
		this.startMillis = System.currentTimeMillis();
		System.out.println("wait(" + duration + ")");
	}
	
	/**
	 * Sets isFinished to true if time is up
	 */
	public void execute() {
		if (startMillis + duration <= System.currentTimeMillis()) {
			//done waiting!
			this.isFinished = true;
		}
	}
	
	/**
	 * Returns whether the command is done or not
	 */
	public boolean isFinished() {
		return this.isFinished;
	}
}
