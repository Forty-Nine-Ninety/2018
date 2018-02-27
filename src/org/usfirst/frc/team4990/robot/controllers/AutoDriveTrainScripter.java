package org.usfirst.frc.team4990.robot.controllers;


import org.usfirst.frc.team4990.robot.controllers.SimpleAutoDriveTrainScripter.StartingPosition;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.subsystems.Elevator;
import org.usfirst.frc.team4990.robot.subsystems.ElevatorPID;
import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.Intake.BoxPosition;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import java.util.LinkedList;
import java.util.Queue;

//You shouldn't mess with this if you don't know what you're doing

public class AutoDriveTrainScripter {
	/**
	 * Base class for commands
	 * @author Old Coder People
	 *
	 */
	private interface CommandPackage {
		//called when the command first starts
		/**
		 * Called when the command starts; resets sensors and things
		 */
		public void init();

		/**
		 * Called every time the other updates are called; Makes sure that it isn't completed yet
		 */
		public void update();

		/**
		 * 
		 * @return Returns false if the command isn't done and true if it is.
		 */
		public boolean done();
	}

	private Queue<CommandPackage> commands = new LinkedList<>();

	private Intake intake;
	private Elevator elevator;
	private DriveTrain dt;
	@SuppressWarnings("unused")
	private StartingPosition startPos; //used in SimpleAutoDriveTrain
	private ADXRS450_Gyro gyro;

	/**
	 * Just a constructor
	 * @param dtrain Drivetrain
	 * @param startP Starting position of robot for auto period
	 * @param gy Gyro sensor
	 * @param i Intake
	 * @param e Elevator
	 * @author Old Coder People
	 */
	public AutoDriveTrainScripter(DriveTrain dtrain, StartingPosition startP, ADXRS450_Gyro gy, Intake i, Elevator e) {
		dt = dtrain;
		startPos = startP;
		gyro = gy;
		intake = i;
		elevator = e;
	}
	/**
	 * Starts first instruction if it exists;
	 */
	public void init() {
		// needs to be called once when auto starts
		CommandPackage top = commands.peek();
		if(top == null) return;

		top.init();
	}
	
	/**
	 * Updates instruction if not done; if done then starts next instruction
	 */
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
	
	/**
	 * Enum for direction because we like readable code
	 * @author Freshman Union
	 *
	 */
	public enum Direction {
		RIGHT,
		LEFT
	}
	
	/**
	 * @deprecated
	 * Command for moving forward/backwards
	 * @param distance Distance to travel in feet
	 * @param backwards If true then it goes backwards, if false then it goes forwards
	 */
	public void goDistance(double distance, boolean backwards) {
		/*Test LOG (format date: specified distance | actual distance)
		 * 1-20-18: 3ft | 3ft+7in
		 *
		 *
		 */
		class F_Package implements CommandPackage {
			private double value;
			private DriveTrain dt;
			private boolean done;
			private boolean backwards;

			public F_Package(DriveTrain d, double v, boolean backwards) {
				this.dt = d;
				// Use abs to make sure a negative number doesn't break anything
				this.value = Math.abs(v);
				this.done = false;
				this.backwards = backwards;
			}
			/**
			 * Clears drivetrain distances
			 */
			public void init() {
				this.dt.resetDistanceTraveled();
				System.out.println("goDistance(" + value + ")");
			}
			/**
			 * Updates forward instruction
			 */
			public void update() {
				// only the right side works...
				// and it's backwards
				// this entire robot is backwards
				//System.out.println("LEFT: "+  this.dt.getLeftDistanceTraveled() + " RIGHT: " + this.dt.getRightDistanceTraveled());
				double speed = .3;
				if (this.backwards == true) {
					speed = -speed;
				}
				if(Math.abs(this.dt.getRightDistanceTraveled()) < this.value) {
					dt.setLeftSpeed(speed);
					dt.setRightSpeed(speed);
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

		commands.add(new F_Package(dt, distance, backwards));
	}
	/**
	 * Makes robot wait
	 * @param time Time to wait for in milliseconds
	 */
	public void wait(double time) { //time is in milliseconds
		class W_Package implements CommandPackage {
			private boolean done;
			private long duration;
			private long startMillis;

			public W_Package(double t) {
				this.duration = (long) t;
				this.done = false;
			}
			/**
			 * Clears time
			 */
			public void init() {
				this.startMillis = System.currentTimeMillis();
				System.out.println("wait(" + duration + ")");
			}
			/**
			 * Sets done to true if time is up
			 */
			public void update() {
				if (startMillis + duration <= System.currentTimeMillis()) {
					//done waiting!
					this.done = true;
				}
			}
			/**
			 * Returns whether the command is done or not
			 */
			public boolean done() {
				return this.done;
			}
		}

		commands.add(new W_Package(time));
	}

	/**
	 * Turns robot for degrees
	 * @deprecated
	 * @param degrees
	 * @param lr
	 */
	public void turnForDegrees(double degrees, Direction lr) {
		//0.01709 feet per 1 degree
		class turnForDegrees_Package implements CommandPackage {
			// feetPer1Degree = ((24 * pi) inches / 360 degrees) / (12 inches / 1 foot)
			// times some other random constant lol
			private double feetPer1Degree = 0.01745329  * .99;
			private double classdegrees;
			private boolean done;
			private DriveTrain dt;
			private boolean left;
			private double encoderDistanceToStriveFor;
			private double currentEncoderDistance;

			public turnForDegrees_Package(DriveTrain d, double classdegrees, Direction classlr) {
				// please note that the right encoder is backwards
				this.dt = d;
				this.classdegrees = classdegrees;
				this.done = false;
				encoderDistanceToStriveFor = this.classdegrees * feetPer1Degree;

				currentEncoderDistance = 0;
				
				if (classlr == Direction.LEFT) {
					this.left = true;
				}
				else {
					this.left = false;
				}
			}
			public void init() {
				this.dt.resetDistanceTraveled();
				System.out.println("turnForDegrees(" + degrees + ", Left:" + left + ")");
			}

			public void update() {
				double speed = 0.20;
				if (this.left == true) {
					// if it's supposed to turn left (I know it's weird just go with it)
					speed = -speed;
				}

				if (currentEncoderDistance <= encoderDistanceToStriveFor) {
	
					// Gets the average of each side's distance
					// Needs abs or else only works one direction
					currentEncoderDistance = (Math.abs(this.dt.getRightDistanceTraveled()) +
											  Math.abs(this.dt.getLeftDistanceTraveled())) / 2;

					//DEBUG ENCODER PRINTER
					System.out.println("LEFT: " + this.dt.getLeftDistanceTraveled() + "  RIGHT: " + this.dt.getRightDistanceTraveled() + "  "+ encoderDistanceToStriveFor);

					this.dt.setLeftSpeed(speed); // left needs to go forwards
					this.dt.setRightSpeed(-speed); // right needs to go backwards
				}
				else {
					this.dt.setSpeed(0.0, 0.0);
					this.done = true;
				}

			}
			
			public boolean done() {
				return this.done;
			}
		}
		commands.add(new turnForDegrees_Package(dt, degrees, lr));
	}
	/**
	 * Command for going straight
	 * @param distance Distance to go straight for in feet
	 */
	public void gyroStraight(double distance) {
		class gyroStraight_Package implements CommandPackage {
			private double distanceToGo;
			private double startingGyro;
			private boolean done;
			private DriveTrain dt;
			private ADXRS450_Gyro gyro;
			private double baseMotorPower;
			private double currentGyroData;
			private double leftMotorAdjust;
			private double currentDistanceTraveled;


			public gyroStraight_Package(DriveTrain dt, ADXRS450_Gyro gyro, double distance) {
				//Remember that the right motor is the slow one
				this.done = false;
				this.dt = dt;
				this.gyro = gyro;
				this.distanceToGo = distance;
				this.startingGyro = 0;
				this.baseMotorPower = 0.3;
			}
			public void init() {
				System.out.println("gyroStraight(" + distance + ")");
				this.dt.resetDistanceTraveled();
				gyro.reset();
			}
			public void update() {
				this.currentDistanceTraveled = Math.abs(this.dt.getRightDistanceTraveled()) * 1.06517;
				this.currentGyroData = gyro.getAngle();

				System.out.println("current distance: " + currentDistanceTraveled + " stopping at: " + this.distanceToGo + "r encoder: " + this.dt.getRightDistanceTraveled() + this.dt.getLeftDistanceTraveled());
				if (currentDistanceTraveled < this.distanceToGo) {
					
					if (this.currentGyroData > this.startingGyro) {
						this.leftMotorAdjust = this.baseMotorPower - 0.064023; //add to number to go more LEFT
					} else if (this.currentGyroData < this.startingGyro) {
						this.leftMotorAdjust = this.baseMotorPower + 0.05;

					}
					this.dt.setSpeed(this.leftMotorAdjust, this.baseMotorPower);
				} else {
					this.done = true;
					this.dt.setSpeed(0, 0);
				}
			}
			
			public boolean done() {
				
				return this.done;
			}
		}
		commands.add(new gyroStraight_Package(dt, gyro, distance));
	}

	/**
	 * Turns left or right
	 * @param inputDegrees Degrees to turn
	 * @param lr Left or right(As an object of type Direction)
	 */
	public void gyroTurn(double inputDegrees, Direction lr) {
		class gyroTurn_Package implements CommandPackage {
			private double degrees;
			private boolean done;
			private DriveTrain dt;
			private ADXRS450_Gyro gyro;
			private Direction dir;

			public gyroTurn_Package(DriveTrain d, ADXRS450_Gyro g, double classdegrees, Direction classlr) {
				// please note that the right encoder is backwards
				this.dt = d;
				this.degrees = classdegrees;
				this.done = false;
				gyro = g;
				dir = classlr;
			}

			public void init() {
				this.gyro.reset();
				System.out.println("gyroTurn(" + degrees + ", " + dir + ")");
			}

			public void update() {
				double speed = 0.5;
				if (dir == Direction.LEFT) speed *= -1;
				
				double currentDegreesTraveled = Math.abs(gyro.getAngle());
				
				if (currentDegreesTraveled < this.degrees * 0.67) {
					//DEBUG GYRO PRINTER
					//System.out.println("Current: " + this.gyro.getAngle() + "  Stopping at: " + this.degrees);

					this.dt.setSpeed(speed, -speed); // left needs to go forwards, right needs to go backwards
				}
				else if (currentDegreesTraveled < this.degrees - (this.degrees * 0.02777)) {
					System.out.println("Current: " + this.gyro.getAngle() + "  Stopping at: " + this.degrees);
					
					this.dt.setSpeed(speed / 3, -speed / 3);
				}
				else {
					this.dt.setSpeed(0.0, 0.0);
					this.done = true;
				}

			}
			
			public boolean done() {
				return this.done;
			}
		}
		commands.add(new gyroTurn_Package(dt, gyro, inputDegrees, lr));
	}
	/**
	 * Makes intake throw whatever is inside out
	 */
	public void intakeOut() {
		class IntakeOUT_Package implements CommandPackage {
			private Intake intake;
			private boolean done;
			private double speed = -0.6;
			
			public IntakeOUT_Package(Intake i) {
				this.intake = i;
				this.done = false;
			}
			
			public void init() {
				//nothing.
			}
			
			public void update() {
				BoxPosition boxPos = intake.getBoxPosition();
				if (boxPos.equals(BoxPosition.OUT)) {
					done = true;
				} else if (boxPos.equals(BoxPosition.MOVING) || boxPos.equals(BoxPosition.IN)) {
					intake.setSpeed(speed);

				}
				
				intake.update();
			}
			
			public boolean done() {
				if (this.done) {
					intake.stop();
				}
				return done;
			}
		}
		
		commands.add(new IntakeOUT_Package(intake));
	}
	/**
	 * Makes intake take in whatever is in front of it(people included)
	 */
	public void intakeIn() {
		class IntakeIN_Package implements CommandPackage {
			private Intake intake;
			private boolean done;
			private double speed = 0.6;
			
			public IntakeIN_Package(Intake i) {
				this.intake = i;
				this.done = false;
			}
			
			public void init() {
				//nothing.
			}
			
			public void update() {
				BoxPosition boxPos = intake.getBoxPosition();
				if (boxPos.equals(BoxPosition.IN)) {
					done = true;
				} else if (boxPos.equals(BoxPosition.MOVING) || boxPos.equals(BoxPosition.OUT)) {
					intake.setSpeed(speed);

				}
				
				intake.update();
			}
			
			public boolean done() {
				if (this.done) {
					intake.stop();
				}
				return done;
			}
		}
		
		commands.add(new IntakeIN_Package(intake));
	}
	/**
	 * Moves elevator a set distance
	 * @param distance Distance to move; positive is up, negative is down I think
	 */
	public void moveElevator(double distance) { //TODO Implement PID for elevator
		class Elevator_package implements CommandPackage {
			private Elevator elevator;
			private boolean done;
			private double doneTolerance = 3; //percent
			private PIDController elevatorPID;
			
			public Elevator_package(double dist, Elevator e) {
				this.elevator = e;
				this.done = false;
				elevatorPID = new PIDController(1, 0, 0, elevator.encoder, new ElevatorPID(elevator));
				elevatorPID.setInputRange(0, 4.8); //minimumInput, maximumInput
				elevatorPID.setOutputRange(-1, 1); //minimumOutput, maximumoutput (motor constraints)
				elevatorPID.setSetpoint(dist);
				elevatorPID.setAbsoluteTolerance(doneTolerance);
				LiveWindow.add(elevatorPID);	
			}
			
			public void init() {
				elevator.resetEncoderDistance();
				elevatorPID.enable();
			}
			
			public void update() {
				elevator.setElevatorPower(elevatorPID.get());
					
				if (elevatorPID.onTarget()){ //done
					done = true;
					elevatorPID.disable();
				}
				
			}
			
			public boolean done() {
				if (this.done) {
					elevator.setElevatorPower(0);
				}
				return done;
			}
		}
		
		commands.add(new Elevator_package(distance, elevator));
	}
}
