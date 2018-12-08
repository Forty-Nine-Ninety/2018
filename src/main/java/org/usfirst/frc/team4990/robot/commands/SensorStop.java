package org.usfirst.frc.team4990.robot.commands;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.SmartDashboardController;

import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.command.Command;

public class SensorStop extends Command {
	
	static double stopDistance = SmartDashboardController.getConst("UltrasonicStop/DefaultStopDistance", 20); // inches?
	Command command;
	double endCondition;
	SensorBase sensor;
	Method method;

	public HashMap<String, String> sensorMethods = new HashMap<String, String>(){{
		put("Ultrasonic", "getRangeInches");
		put("Encoder", "getDistance");
		put("AHRS","getAngle");
		put("ADXRS450_Gyro","getAngle");
		put("LimitSwitch", "getValue");
		put("AnalogInput", "getValue");
		put("DigitalInput","get");
	}};
	

	/**
	 * Command for stopping when robot becomes in range of object.
	 * @param distance in inches
	 */
	
	public SensorStop(SensorBase sensor, double endCondition, Command command) {
		this.sensor = sensor;
		this.endCondition = endCondition;
		this.command = command;
		if (sensorMethods.containsKey(sensor.getClass().getName())) {
			try {
				method = sensor.getClass().getDeclaredMethod(sensorMethods.get(sensor.getClass().getName()));
				System.out.println(method.invoke(sensor));
			} catch (Exception e) {
				e.printStackTrace();
				this.cancel();
			}
			
		}
	}
	
	/*public void execute() {
		System.out.println("current ultrasonic distance: " + RobotMap.ultrasonic.getRangeInches());
	}*/
	
	public boolean isFinished() {
		try {
			return method.invoke(sensor).equals(endCondition);
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public void end() {
		command.cancel();
	}
	
	public void interrupted() {
		command.cancel();
	}
}
