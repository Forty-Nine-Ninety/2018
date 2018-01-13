package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import java.util.LinkedList;
import java.util.Queue;

//You shouldn't fuck with this if you don't know what you're doing

public class AutoDriveTrainScripter {
	
	private interface CommandPackage {
		public void update();
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
			top = commands.peek();
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
				if(this.dt.getLeftDistanceTraveled() < this.value) {
					dt.setLeftSpeed(.8);
					dt.setRightSpeed(.8);
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
