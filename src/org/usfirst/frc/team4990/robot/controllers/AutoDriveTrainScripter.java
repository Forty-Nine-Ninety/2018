package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import java.util.LinkedList;
import java.util.Queue;

//You shouldn't mess with this if you don't know what you're doing

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
	
	public void forwardDistance(double distance) { //TODO make it go specified distance
		/*Test LOG (format date: specified distance | actual distance)
		 * 1-20-18: 3ft | 3ft+7in
		 * 
		 * 
		 */
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
				// and it's backwards
				// this entire robot is backwards
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
	
	public void wait(double time) { //time is in milliseconds
		class W_Package implements CommandPackage {
			private boolean done;
			private long duration;
			private long startMillis;
			
			public W_Package(double t) {
				this.duration = (long) t;
				this.done = false;
				this.startMillis = System.currentTimeMillis(); 
			}
			
			public void update() {
				System.out.println(startMillis + duration);
				System.out.println(System.currentTimeMillis());
				if (startMillis + duration <= System.currentTimeMillis()) {//done waiting!
					this.done = true;
				}
			}
			
			public boolean done() {
				return this.done;
			}
		}
		
		commands.add(new W_Package(time));
	}
	
	public void timeTurn(double degrees, boolean rside) { //degrees are how it sounds (degrees of a circle), if rside == true then robot will turn to the right 
		class T_Package implements CommandPackage {
			private boolean done;
			private double degreesTime;
			private boolean rside;
			private long startMillis;
			
			public T_Package(double d, boolean s) {
				this.rside = s;
				this.degreesTime = d; //TODO do math to compute how long it takes to turn d degree
				this.done = false;
				this.startMillis = System.currentTimeMillis(); 
			}
			
			public void update() {
					if (startMillis + degreesTime > System.currentTimeMillis()) { //now turning
						
						if (rside) { //turn toward robot's right
							//TODO make sure this turns RIGHT
							dt.setLeftSpeed(0.3);//left side forward
							dt.setRightSpeed(-0.3);//right side backward
						} else {//turn toward robot's left
							//TODO make sure this turns LEFT
							dt.setLeftSpeed(-0.3);//left side backward
							dt.setRightSpeed(0.3);//right side forward
						}
					} else { //robot finished turning
						this.done = true;
					}
					
					
				}
			
			public boolean done() {
				return this.done;
			}
		}
		
		commands.add(new T_Package(degrees, rside));
	}
	
	public void encoderTurn(double degrees, boolean rside) { //degrees are how it sounds (degrees of a circle), if rside == true then robot will turn to the right 
		class T_Package implements CommandPackage {
			private boolean done;
			private boolean rside;
			private DriveTrain dt;
			private double value;
			
			public T_Package(DriveTrain drivet, double d, boolean s) {
				this.rside = s;
				this.dt = drivet;
				this.dt.resetDistanceTraveled();
				this.done = false;
			}
			
			public void update() {
				if (-this.dt.getRightDistanceTraveled() < this.value) {
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
		
		commands.add(new T_Package(dt, degrees, rside));
	}
	
	public void turnDistance(double degrees) {
		class T_Package implements CommandPackage {//"Container" for turn command
			private double distance;//Distance to turn
			private DriveTrain dt;//Drivetrain
			private boolean done;//If it's done or not
			public T_Package(DriveTrain d, double degrees) {
				this.dt = d;
				this.distance = (degrees / 360) * Math.PI * 46.0208333;//Converts degrees to distance based on radius of 23.5 inches
				this.done = false;
				this.dt.resetDistanceTraveled();
			}
			
			public void update() {
				System.out.println(this.dt.getLeftDistanceTraveled() + " " + this.distance);
				if (this.dt.getLeftDistanceTraveled() < this.distance - (1/6)) {
					dt.setSpeed(0.2, -0.2);
				}
				else if (this.dt.getLeftDistanceTraveled() < this.distance) {
					dt.setSpeed(0.05, -0.05);
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
