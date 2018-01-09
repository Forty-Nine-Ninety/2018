package org.usfirst.frc.team4990.robot.controllers;

import java.util.Date;

import org.usfirst.frc.team4990.robot.lib.MotionProfile;
import org.usfirst.frc.team4990.robot.lib.PositionPIDLoop;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

public class AutoDriveTrainController {
	
	private DriveTrain driveTrain;
	private Date motionProfileStart;
	
	private double currentLDistanceTraveled;
	
	private double wantedDistance;
	private int delay;
	private double velocity;
	
	public AutoDriveTrainController(DriveTrain driveTrain) {
		this.driveTrain = driveTrain;
	}
	
	public void setAutoDriveConstraints(int delay, double distance, double velocity)
	{
		//delay in seconds
		//distance measured in feet
		//velocity from 0 - 1
		this.wantedDistance = distance;
		this.delay = delay;
		this.velocity = velocity;
	}
	
	public void updateAutoDrive()
	{
		//getleftdistancetraveled returns in feet
		//based on left distance
		//arbitrary if bot moves straight
		this.currentLDistanceTraveled = this.driveTrain.getLeftDistanceTraveled();
		
		if(this.currentLDistanceTraveled < this.wantedDistance/2)
		{
			this.driveTrain.setSpeed(velocity, velocity);
		}
		else if (this.wantedDistance/2 < this.currentLDistanceTraveled & this.currentLDistanceTraveled < this.wantedDistance){
			this.driveTrain.setSpeed(velocity, -velocity);
		}
		else {
			double STOP = 0.0;
			this.driveTrain.setSpeed(STOP, STOP);
		}
		
	}
}