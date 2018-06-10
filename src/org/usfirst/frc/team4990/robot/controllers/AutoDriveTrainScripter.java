package org.usfirst.frc.team4990.robot.controllers;


import org.usfirst.frc.team4990.robot.controllers.SimpleAutoDriveTrainScripter.StartingPosition;
import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4990.robot.subsystems.Elevator;
import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.Intake.BoxPosition;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Ultrasonic;

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
		public void initialize();

		/**
		 * Called every time the other updates are called; Makes sure that it isn't completed yet
		 */
		public void execute();

		/**
		 * 
		 * @return Returns false if the command isn't done and true if it is.
		 */
		public boolean isFinished();
	}

	private Queue<CommandPackage> commands = new LinkedList<>();

	private Intake intake;
	private Elevator elevator;
	private DriveTrain dt;
	@SuppressWarnings("unused")
	private StartingPosition startPos; //used in SimpleAutoDriveTrain
	private ADXRS450_Gyro gyro;
	private Ultrasonic ultrasonic;
	private AHRS ahrs;

	/**
	 * Just a constructor
	 * @param dtrain Drivetrain
	 * @param startP Starting position of robot for auto period
	 * @param gy Gyro sensor
	 * @param i Intake
	 * @param e Elevator
	 * @param u Ultrasonic
	 * @param a AHRS gyro (NAVX-MXP)
	 * @author Old Coder People
	 */
	public AutoDriveTrainScripter(DriveTrain dtrain, StartingPosition startP, ADXRS450_Gyro gy, Intake i, Elevator e, Ultrasonic u, AHRS ahrs) {
		dt = dtrain;
		startPos = startP;
		gyro = gy;
		intake = i;
		elevator = e;
	}
	/**
	 * Starts first instruction if it exists;
	 */
	public void initialize() {
		// needs to be called once when auto starts
		CommandPackage top = commands.peek();
		if(top == null) return;

		top.initialize();
	}
	
	/**
	 * executes instruction if not done; if done then starts next instruction
	 */
	public void execute() {
		CommandPackage top = commands.peek();
		if(top == null) return;

		if(!top.isFinished() ) {
			top.execute();
		}
		else {
			commands.remove();

			// we can use recursion
			// but I don't want to take the risk
			top = commands.peek();
			if(top == null) return;
			top.initialize();
			top.execute();
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
	
	//------ Begin commands ------//
	
	/**
	 * Makes robot wait
	 * @param time Time to wait for in milliseconds
	 */
	public void wait(double time) { //time is in milliseconds
		class W_Package implements CommandPackage {
			private boolean isFinished;
			private long duration;
			private long startMillis;

			public W_Package(double t) {
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

		commands.add(new W_Package(time));
	}
	/**
	 * Command for going straight
	 * @param distance Distance to go straight for in feet
	 */
	public void gyroStraight(double distance) {
		class gyroStraight_Package implements CommandPackage {
			private double distanceToGo;
			private double startingGyro;
			private boolean isFinished;
			private DriveTrain dt;
			private ADXRS450_Gyro gyro;
			private double baseMotorPower;
			private double currentDistanceTraveled;

			public gyroStraight_Package(DriveTrain dt, ADXRS450_Gyro gyro, double distance) {
				//Remember that the right motor is the slow one
				this.isFinished = false;
				this.dt = dt;
				this.gyro = gyro;
				this.distanceToGo = distance;
				this.startingGyro = 0;
				this.baseMotorPower = 0.3;
			}
			public void initialize() {
				System.out.println("gyroStraight(" + distance + ")");
				this.dt.resetDistanceTraveled();
				gyro.reset();
			}
			public void execute() {
				this.currentDistanceTraveled = (distance > 0) ? -this.dt.right.getDistanceTraveled() * 1.06517 : this.dt.right.getDistanceTraveled() * 1.06517;

				System.out.println("current distance: " + currentDistanceTraveled + " stopping at: " + this.distanceToGo + "r encoder: " + this.dt.right.getDistanceTraveled() + this.dt.left.getDistanceTraveled());
				if (currentDistanceTraveled < this.distanceToGo) { //not at goal yet
					this.dt.setSpeed(baseMotorPower + (0.1 * (startingGyro - gyro.getAngle())), baseMotorPower - (0.1 * (startingGyro - gyro.getAngle()))); //TODO: find proportional values (right now 0.1) for drive train
				} else {
					this.isFinished = true;
					this.dt.setSpeed(0, 0);
				}
			}
			
			public boolean isFinished() {
				return this.isFinished;
			}
		}
		commands.add(new gyroStraight_Package(dt, gyro, distance));
	}
	
	/**
	 * Command for going straight
	 * @param distance Distance to go straight for in feet
	 */
	public void straightToSwitch() {
		class straightToSwitch_Package implements CommandPackage {
			private double startingGyro;
			private boolean isFinished;
			private DriveTrain dt;
			private ADXRS450_Gyro gyro;
			private Ultrasonic ultrasonic;
			private double baseMotorPower;
			private double currentGyroData;
			private double leftMotorAdjust;


			public straightToSwitch_Package(DriveTrain dt, ADXRS450_Gyro gyro, Ultrasonic ultrasonic) {
				//Remember that the right motor is the slow one
				this.isFinished = false;
				this.dt = dt;
				this.gyro = gyro;
				this.ultrasonic = ultrasonic;
				this.startingGyro = 0;
				this.baseMotorPower = 0.3;
			}
			
			public void initialize() {
				System.out.println("straightToSwitch()");
				this.dt.resetDistanceTraveled();
				gyro.reset();
			}
			public void execute() {
				this.currentGyroData = gyro.getAngle();
				System.out.println("current ultrasonic distance: " + ultrasonic.getRangeInches());
				
				if (ultrasonic.getRangeInches() < 20) { //THIS LINE TELLS ROBOT WHEN TO STOP
					
					if (this.currentGyroData > this.startingGyro) {
						this.leftMotorAdjust = this.baseMotorPower - 0.064023; //add to number to go more LEFT
					} else if (this.currentGyroData < this.startingGyro) {
						this.leftMotorAdjust = this.baseMotorPower + 0.05;

					}
					this.dt.setSpeed(this.leftMotorAdjust, this.baseMotorPower);
				} else {
					this.isFinished = true;
					this.dt.setSpeed(0, 0);
				}
			}
			
			public boolean isFinished() {
				return this.isFinished;
			}
		}
		commands.add(new straightToSwitch_Package(dt, gyro, ultrasonic));
	}

	/**
	 * Turns left or right
	 * @param inputDegrees Degrees to turn (Positive = right, negative = left)
	 */
	public void gyroTurn(double inputDegrees) {
		class gyroTurn_Package implements CommandPackage {
			private double degrees;
			private boolean isFinished;
			private DriveTrain dt;
			private ADXRS450_Gyro gyro;

			public gyroTurn_Package(DriveTrain d, ADXRS450_Gyro g, double classdegrees) {
				// please note that the right encoder is backwards
				this.dt = d;
				this.degrees = classdegrees;
				this.isFinished = false;
				gyro = g;
			}

			public void initialize() {
				this.gyro.reset();
				System.out.println("gyroTurn(" + degrees + ")");
			}

			public void execute() {
				
				double currentDegreesTraveled = Math.abs(gyro.getAngle());
				System.out.println("Current angle: " + this.gyro.getAngle() + "  Stopping at: " + this.degrees);
				
				if (currentDegreesTraveled < this.degrees) {
					this.dt.setSpeed( //TODO change (1/90) to actual porportional value
							(degrees > 0) ? //turning right?
							(1/90) * (degrees - gyro.getAngle()) : -(1/90) * (degrees - gyro.getAngle()), 
							(degrees > 0) ? //turning right?
							-(1/90) * (degrees - gyro.getAngle()) : (1/90) * (degrees - gyro.getAngle())
							); // left needs to go forwards, right needs to go backwards to turn right	
				} else {
					this.dt.setSpeed(0, 0);
					this.isFinished = true;
				}
			}
			
			public boolean isFinished() {
				return this.isFinished;
			}
		}
		commands.add(new gyroTurn_Package(dt, gyro, inputDegrees));
	}
	
	public void ahrsTurn(double heading) {
		class AhrsTurn_Package implements CommandPackage {
			private AHRS ahrs;
			private DriveTrain dt;
			private double maxSpeed = 0.65;
			private double regSpeed = 0.35;
			private PIDController yawPIDController;
			private double heading = 0;
			private double kP = 0.03;
			private double kI = 0;
			private double kD = 0;
			
			public AhrsTurn_Package(AHRS ahrs_input, DriveTrain dt_input, double heading_input) {
				this.ahrs = ahrs_input;
				this.dt = dt_input;
				this.heading = heading_input;
			}
			
			public void initialize() {
				yawPIDController = new PIDController(kP, kI, kD, ahrs, dt);
				yawPIDController.setInputRange(-180f, 180f);
				yawPIDController.setSetpoint(heading);
				yawPIDController.setContinuous(true);
		        yawPIDController.setOutputRange(-maxSpeed, maxSpeed);
		        yawPIDController.setAbsoluteTolerance(3);
		        yawPIDController.enable();
		        ahrs.zeroYaw();
			}

			public void execute() {
				if (yawPIDController.onTarget()) { //go straight
					dt.setSpeed(0);
				} else if (yawPIDController.getError() > 0) { //turn right?
					dt.setSpeed(regSpeed + yawPIDController.get(), regSpeed - yawPIDController.get()); 
				} else if (yawPIDController.getError() < 0) {//turn left?
					dt.setSpeed(regSpeed - yawPIDController.get(), regSpeed + yawPIDController.get());
				}
			}

			public boolean isFinished() {
				return yawPIDController.onTarget();
			}
		}
		commands.add(new AhrsTurn_Package(ahrs, dt, heading));
	}
	
	/**
	 * Makes intake throw whatever is inside out
	 */
	public void intakeOut() {
		class IntakeOUT_Package implements CommandPackage {
			private Intake intake;
			private boolean isFinished;
			private double speed = -0.6;
			
			public IntakeOUT_Package(Intake i) {
				this.intake = i;
				this.isFinished = false;
			}
			
			public void initialize() {
				//nothing.
			}
			
			public void execute() {
				BoxPosition boxPos = intake.getBoxPosition();
				if (boxPos.equals(BoxPosition.OUT)) {
					isFinished = true;
				} else if (boxPos.equals(BoxPosition.MOVING) || boxPos.equals(BoxPosition.IN)) {
					intake.setSpeed(speed);

				}
				if (this.isFinished) {
					intake.stop();
				}
				intake.update();
			}
			
			public boolean isFinished() {
				return isFinished;
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
			private boolean isFinished;
			private double speed = 0.6;
			
			public IntakeIN_Package(Intake i) {
				this.intake = i;
				this.isFinished = false;
			}
			
			public void initialize() {
				//nothing.
			}
			
			public void execute() {
				BoxPosition boxPos = intake.getBoxPosition();
				if (boxPos.equals(BoxPosition.IN)) {
					isFinished = true;
				} else if (boxPos.equals(BoxPosition.MOVING) || boxPos.equals(BoxPosition.OUT)) {
					intake.setSpeed(speed);

				}
				if (this.isFinished) {
					intake.stop();
				}
				intake.update();
			}
			
			public boolean isFinished() {
				return isFinished;
			}
		}
		
		commands.add(new IntakeIN_Package(intake));
	}
	/**
	 * Moves elevator a set distance
	 * @param distance Distance to move; positive is up, negative is down I think
	 * TODO: Needs to be modified for actual encoder
	 */
	public void moveElevator(double distance) { 
		class Elevator_package implements CommandPackage {
			private Elevator elevator;
			private boolean isFinished;
			private PIDController elevatorPID;
			
			public Elevator_package(double dist, Elevator e) {
				this.elevator = e;
				this.isFinished = false;
				this.elevatorPID = e.elevatorPID;
			}
			
			public void initialize() {
				elevator.resetEncoderDistance();
				elevatorPID.enable();
			}
			
			public void execute() {
				elevator.setElevatorPower(elevatorPID.get());
					
				if (elevatorPID.onTarget()){ //isFinished
					isFinished = true;
					elevatorPID.disable();
				}
				
				if (this.isFinished) {
					elevator.setElevatorPower(0);
				}
				
			}
			
			public boolean isFinished() {
				return this.isFinished;
			}
		}
		
		commands.add(new Elevator_package(distance, elevator));
	}
	
	/**
	 * Moves elevator for duration.
	 * @param time in milliseconds
	 * @param power 0 to 1 for up, 0 to -1 for down
	 */
	
	
	public void moveElevatorTime(double time, double power) { //time is in milliseconds
		class EW_Package implements CommandPackage {
			private boolean isFinished;
			private long duration;
			private double power;
			private long startMillis;
			private Elevator elevator;

			public EW_Package(double t, double power_input, Elevator elevator) {
				this.duration = (long) t;
				this.power = power_input;
				this.isFinished = false;
				this.elevator = elevator;
			}
			/**
			 * Clears time
			 */
			public void initialize() {
				this.startMillis = System.currentTimeMillis();
				System.out.println("moveElevatorTime(" + duration + ")");
				elevator.setElevatorPower(power);
			}
			/**
			 * Sets isFinished to true if time is up
			 */
			public void execute() {
				if (startMillis + duration <= System.currentTimeMillis()) {
					//done waiting!
					elevator.setElevatorPower(0);
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

		commands.add(new EW_Package(time, power, elevator));
	}
	
	
}
