package org.usfirst.frc.team4990.robot.controllers;
//GODISTANCE

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

//import edu.wpi.first.wpilibj.Solenoid; //If we need to implement air compressors

import java.util.LinkedList;
import java.util.Queue;

//You shouldn't mess with this if you don't know what you're doing

public class AutoDriveTrainScripter {

	private interface CommandPackage {
		//called when the command first starts
		public void init();

		// called every time
		public void update();

		// returns true if command is finished
		public boolean done();
	}

	private Queue<CommandPackage> commands = new LinkedList<>();

	private DriveTrain dt;
	//private Solenoid solenoid;

	public AutoDriveTrainScripter(DriveTrain dtrain) {
		dt = dtrain;
		//Solenoid solenoid = solen;
	}

	public void init() {
		// needs to be called once when auto starts
		CommandPackage top = commands.peek();
		if(top == null) return;

		top.init();
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
			top.init();
			top.update();
		}
	}

	public void goDistance(double distance, boolean backwards) { //TODO make it go specified distance
		/*Test LOG (format date: specified distance | actual distance)
		 * 1-20-18: 3ft | 3ft+7in
		 *
		 *
		 */
		class F_Package implements CommandPackage {
			private double value;
			private DriveTrain dt;
			private boolean done;
			private boolean constbck;

			public F_Package(DriveTrain d, double v, boolean constbck) {
				this.dt = d;
				this.value = v;
				this.done = false;
				this.constbck = constbck;
			}

			public void init() {
				this.dt.resetDistanceTraveled();
			}

			public void update() {
				// only the right side works...
				// and it's backwards
				// this entire robot is backwards
				//System.out.println("right:"+ -this.dt.getRightDistanceTraveled() + " left:"+ this.dt.getLeftDistanceTraveled());
				if (this.constbck == true) {
					if(-this.dt.getRightDistanceTraveled() < this.value) { //THIS MAY NEED TO BE NEGATIVE THIS.DT.GETDISTANCE TRAVELED.. ETC
						dt.setLeftSpeed(.3);
						dt.setRightSpeed(.3);
					}
					else {
						dt.setLeftSpeed(0.0);
						dt.setRightSpeed(0.0);
						this.done = true;
					}
				} else if (this.constbck == false) {
					if(-this.dt.getRightDistanceTraveled() < this.value) {
						dt.setLeftSpeed(-0.3);
						dt.setRightSpeed(-0.3);
					}
					else {
						dt.setLeftSpeed(0.0);
						dt.setRightSpeed(0.0);
						this.done = true;
					}
				}
			}

			public boolean done() {
				return this.done;
			}
		}

		commands.add(new F_Package(dt, distance, backwards));
	}
	public void debugEncoders(double speed) { //TODO make it go specified distance
		class debugEncoders_Package implements CommandPackage {
			private double speed;
			private DriveTrain dt;
			private boolean done;

			public debugEncoders_Package(DriveTrain d, double s) {
				this.dt = d;
				this.speed = s;
				this.done = false;
			}

			public void init() {
				this.dt.resetDistanceTraveled();
			}

			public void update() {
				System.out.println("right:"+ -this.dt.getRightDistanceTraveled() + " left:"+ this.dt.getLeftDistanceTraveled());

					dt.setLeftSpeed(speed);
					dt.setRightSpeed(speed);
				}

			public boolean done() {
				return this.done;
			}

		}
		commands.add(new debugEncoders_Package(dt, speed));
	}

	public void wait(double time) { //time is in milliseconds
		class W_Package implements CommandPackage {
			private boolean done;
			private long duration;
			private long startMillis;

			public W_Package(double t) {
				this.duration = (long) t;
				this.done = false;
			}

			public void init() {
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


	public void turnForDegrees(double degrees, String lr) {
		//0.01709 feet per 1 degree
		class turnForDegrees_Package implements CommandPackage{
			private double feetPer1Degree = 0.00213625; //0.01709 / 8
			private double classdegrees;
			private String classlr;
			private boolean done;
			private DriveTrain dt;
			private boolean left;
			//RESET DISTANCE TRAVELED USED TO BE IN CONSTRUCTOR. MOVE IT BACK IF STUFF GOES WRONG
			private double encoderDistanceToStriveFor;
			private double currentEncoderDistance;

			public turnForDegrees_Package(DriveTrain d, double classdegrees, String classlr) {
				// please note that the right encoder is backwards
				this.dt = d;
				this.classdegrees = classdegrees;
				this.classlr = classlr;
				this.done = false;
				encoderDistanceToStriveFor = this.classdegrees * feetPer1Degree;
				System.out.println(encoderDistanceToStriveFor);
				currentEncoderDistance = 0;
				if (this.classlr == "l") {
					this.left = true;
				}
				else {
					this.left = false;
				}
			}

			public void init() {
				this.dt.resetDistanceTraveled();
			}

			public void update() {
				System.out.println(currentEncoderDistance);
				if (this.left == true) { // if it's supposed to turn left (I know it's weird just go with it)
					if (currentEncoderDistance <= encoderDistanceToStriveFor) {
						//DONT TOUCH THIS NEXT LINE
						currentEncoderDistance = (-this.dt.getLeftDistanceTraveled() + this.dt.getRightDistanceTraveled()) / 2; //Takes the average of the two encoder distance traveled

						//DEBUG ENCODER PRINTER
						System.out.print("LEFT: " + this.dt.getLeftDistanceTraveled() + "  RIGHT: " + this.dt.getRightDistanceTraveled());

						this.dt.setLeftSpeed(-0.3); // left needs to go backwards
						this.dt.setRightSpeed(0.3); // right needs to go forwards
					}
					else {
						this.done = true;
					}

				} else if (this.left == false) { //if it's supposed to turn right (I know it's weird just go with it)
					if (currentEncoderDistance <= encoderDistanceToStriveFor) {
						//DONT TOUCH THIS NEXT LINE

						currentEncoderDistance = (this.dt.getLeftDistanceTraveled() - this.dt.getRightDistanceTraveled()) / 2; //Takes average of the two encoder distances

						//DEBUG ENCODER PRINTER
						System.out.print("LEFT: " + this.dt.getLeftDistanceTraveled() + "  RIGHT: " + this.dt.getRightDistanceTraveled());

						this.dt.setLeftSpeed(0.3); // left needs to go forewards
						this.dt.setRightSpeed(-0.3); // right needs to go backwards
					}
					else {
						this.done = true;
					}

				}
			}
			public boolean done() {
				if (this.done) {
					this.dt.setSpeed(0.0, 0.0);
				}
				return this.done;
			}
		}
		commands.add(new turnForDegrees_Package(dt, degrees, lr));
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
			}

			public void init() {
				this.dt.resetDistanceTraveled();
			}

			public void update() {
				System.out.println(this.dt.getLeftDistanceTraveled() + " / " + this.distance);
				if (this.distance < 0) {
					if (this.dt.getLeftDistanceTraveled() > this.distance + (1/6)) {
						dt.setSpeed(-0.2, 0.2);
					}
					else if (this.dt.getLeftDistanceTraveled() > this.distance) {
						dt.setSpeed(-0.05, 0.05);
					}
					else {
						dt.setSpeed(0, 0);
						this.done = true;
					}
				}
				else {
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
			}
			public boolean done() {
				return this.done;

			}

		}

		commands.add(new T_Package(dt, degrees));

	}


}
