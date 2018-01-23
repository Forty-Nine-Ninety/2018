package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import java.util.LinkedList;
import java.util.Queue;

//You shouldn't fuck with this if you don't know what you're doing

//Well I do know what I'm doing I think

public class AutoDriveTrainScripter {
	
	private interface CommandPackage {
		// called every time
		public void update();
		
		// returns true if command is finished
		public boolean done();
	}
	
	private Queue<CommandPackage> commands = new LinkedList<>();
	
	private DriveTrain dt;

	public AutoDriveTrainScripter(DriveTrain dtrain) {
		dt = dtrain;
	}
	
	public void update() {
		CommandPackage top = commands.peek();
		if(top == null) return;

		if(!top.done() ) {
			top.update();
		}
		else {
			commands.remove();
			
			// we can use recursion
			// but I don't want to take the risk
			top = commands.peek();
			if(top == null) return;
			top.update();
		}
	}
	
	public void forwardDistance(double distance) {
		class F_Package implements CommandPackage {
			private double value;
			private DriveTrain dt;
			private boolean done;
			
			public F_Package(DriveTrain d, double v) {
				this.dt = d;
				this.value = v;
				this.done = false;
				this.dt.resetDistanceTraveled();
			}
			
			public void update() {
				// only the right side works...
				// and it's fucking backwards
				// this entire fucking robot is backwards
				if(-this.dt.getRightDistanceTraveled() < this.value) {
					dt.setLeftSpeed(.3);
					dt.setRightSpeed(.3);
				}
				else {
					dt.setLeftSpeed(0.0);
					dt.setRightSpeed(0.0);
					this.done = true;
				}
			}
			
			public boolean done() {
				return this.done;
			}
		}
		
		commands.add(new F_Package(dt, distance));
	}
	
	public void turnDistance(double degrees) {
		class T_Package implements CommandPackage {//"Container" for turn command
			private double distance;//Distance to turn
			private DriveTrain dt;//Drivetrain
			private boolean done;//If it's done or not
			public T_Package(DriveTrain d, double degrees) {
				this.dt = d;
				this.distance = (degrees / 360) * Math.PI * 552.25;//Converts degrees to distance based on radius of 23.5 inches
				this.done = false;
				this.dt.resetDistanceTraveled();
			}
			
			public void update() {
				if (this.dt.getLeftDistanceTraveled() < this.distance - (1/6)) {
					dt.setSpeed(0.5, -0.5);
				}
				else if (this.dt.getLeftDistanceTraveled() < this.distance) {
					dt.setSpeed(0.1, -0.1);
				}
				else {
					dt.setSpeed(0, 0);
					this.done = true;
				}
			}
			public boolean done() {
				return this.done;
				
			}
			
		}
		
		commands.add(new T_Package(dt, degrees));
		
	}
}
