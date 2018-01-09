package org.usfirst.frc.team4990.robot.lib;

import java.util.Date;

public class PositionPIDLoop {
	private double Kp;
	private double Kd;
	private double Kv;
	private double Ka;
	
	private double goalPos;
	private double goalVel;
	private double goalAcc;
	
	private double lastPos;
	private Date lastUpdate;
	
	public PositionPIDLoop(double Kp, double Kd, double Kv, double Ka) {
		this.Kp = Kp;
		this.Kd = Kd;
		this.Kv = Kv;
		this.Ka = Ka;
		
		this.lastPos = 0;
		this.lastUpdate = new Date();
	}
	
	public void setGoal(double goalPos, double goalVel, double goalAcc) {
		this.goalPos = goalPos;
		this.goalVel = goalVel;
		this.goalAcc = goalAcc;
	}
	
	public double getNextVelocity(double currPos) {
		double posError = this.goalPos - currPos;
		
		double dt = ((new Date()).getTime() - this.lastUpdate.getTime()) / 1000.0; //divide by 1000 to convert to seconds
		double velocity = dt != 0 ? (currPos - this.lastPos) / dt : 0;
		double velError = this.goalVel - velocity;
				
		this.lastPos = currPos;
		this.lastUpdate = new Date();
		
		System.out.println("currPos: " + currPos + "; posError: " + posError + "; dt: " + dt + "; velocity: " + velocity + "; velError: " + velError);
		
		return this.Kp * posError + Kd * velError + Kv * this.goalVel + Ka * this.goalAcc;
	}
}
