package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import java.util.LinkedList;
import java.util.Queue;

//You shouldn't fuck with this if you don't know what you're doing

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
}
