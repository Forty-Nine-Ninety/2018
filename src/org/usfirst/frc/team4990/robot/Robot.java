package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4990.robot.controllers.*;
import org.usfirst.frc.team4990.robot.controllers.SimpleAutoDriveTrainScripter.StartingPosition;
import org.usfirst.frc.team4990.robot.subsystems.*;
import org.usfirst.frc.team4990.robot.subsystems.motors.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	public Command autoCommand;
	public SendableChooser autoChooser;
	//public StartingPosition startPos = StartingPosition.MID;
	
	private Preferences prefs;
	private F310Gamepad driveGamepad;
	private DriveTrain driveTrain;
	
	private SimpleAutoDriveTrainScripter autoScripter;
	
	private TeleopDriveTrainController teleopDriveTrainController;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	System.out.println("This worky presumably?");
    	System.out.println("Version 1.9.2018.6.33");
    	this.prefs = Preferences.getInstance();
    	
    	this.driveGamepad = new F310Gamepad(1);
    	
    	this.driveTrain = new DriveTrain( 
    		new TalonMotorController(0),
    		new TalonMotorController(1),
    		new TalonMotorController(2),
    		new TalonMotorController(3),
    		0, 1, 2, 3);
    	autoScripter = new SimpleAutoDriveTrainScripter(driveTrain, StartingPosition.ERROR);
    	//~~~~ Smart Dashboard ~~~~
    	//Auto chooser
    	/*autoChooser = new SendableChooser();
    	System.out.println("This worky presumably?");
    	autoChooser.addObject("Left",  new selectAuto(StartingPosition.LEFT));
    	autoChooser.addDefault("Middle", new selectAuto(StartingPosition.MID));
    	autoChooser.addObject("Right",  new selectAuto(StartingPosition.RIGHT));
    	SmartDashboard.putData("Auto Location Chooser", autoChooser);
    	//refreshSelectAuto refreshSelectAuto_inst = new refreshSelectAuto();
    	SmartDashboard.putData("Refresh Auto Selector", new refreshSelectAuto());
    	//Other gauges and data
    	SmartDashboard.putData(Scheduler.getInstance());*/
    	
    	
    	
    }
    /*public class selectAuto extends Command {
    		boolean isDone = false;
	    	public selectAuto(StartingPosition start) {
	    		super("selectAuto");
	    		System.out.println("Hopefully this too?");
	    		//initialize(start);
	        }
	
	        protected void initialize(StartingPosition startpo) {
	        		startPos = startpo;
	        		System.out.println(startPos);
	        		isDone = true;
	        }
	        protected void execute() {
	        }
	
	 
	        protected boolean isFinished() {
	            return isDone;
	        }
	
	        protected void end() {
	        }
	
	        protected void interrupted() {
	        }
	    }
    
    public class refreshSelectAuto extends Command {
		boolean isDone = false;
		
    	public refreshSelectAuto() {
    		super("refreshSelectAuto");
    		initialize();
        }

        protected void initialize() {
        	SmartDashboard.putString("Selected Auto Pos", startPos.toString());
        	isDone = true;
        }
        protected void execute() {
        }

 
        protected boolean isFinished() {
            return isDone;
        }

        protected void end() {
        }

        protected void interrupted() {
        }
    }*/

    public void autonomousInit() {
    	//autoCommand = (Command) autoChooser.getSelected();
    	//autoCommand.start();
    	System.out.println("Auto Init");
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	autoScripter.update();
    	driveTrain.update();
    //	Scheduler.getInstance().run();
    }
    
    /*public StartingPosition autoRoboStartLoc() {
    	int trigger = -1;
    	for (int i = 1; i < 4; i++) {
    		if (! SmartDashboard.getBoolean("DB/Button " + i, false)) {
    			trigger = i;
    		}
    	}
    	if (trigger == -1) { trigger = 0;}
    	for (int i = 1; i < 4; i++) {
    		SmartDashboard.putBoolean("DB/Button " + i, false);
    		SmartDashboard.putBoolean("DB/LED " + i, false);
    	}
    	SmartDashboard.putBoolean("DB/LED " + trigger, true);
    	
    	
    	switch(trigger) {
    	case 0:
    		System.err.println("YA MESSED UP?");
    		break;
    	case 1:
    		return StartingPosition.LEFT;
    	case 2:
    		return StartingPosition.MID;
    	case 3:
    		return StartingPosition.RIGHT;
    	}
    	return StartingPosition.ERROR;
    }*/
    
    public void teleopInit() {
    	this.teleopDriveTrainController = new TeleopDriveTrainController(
        		this.driveGamepad, 
        		this.driveTrain, 
        		this.prefs.getDouble("maxTurnRadius", Constants.defaultMaxTurnRadius),
        		this.prefs.getBoolean("reverseTurningFlipped", false),
        		this.prefs.getDouble("smoothDriveAccTime", Constants.defaultAccelerationTime),
        		this.prefs.getDouble("lowThrottleMultiplier", .25),
        		this.prefs.getDouble("maxThrottle", 1.0));
    }
     
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
	    this.teleopDriveTrainController.updateDriveTrainState();
	    
	    //ever heard of the tale of last minute code
	    //I thought not, it is not a tale the chairman will tell to you

    	this.driveTrain.update();
    }
}
