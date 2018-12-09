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

	public HashMap<Class, Method> sensorMethods = new HashMap<Class, Method>(){{
		put(Ultrasonic.class, Ultrasonic.getRangeInches);
		put(Encoder.class, Encoder.getDistance);
		put(AHRS.class, AHRS.getDistance);
		put(ADXRS450_Gyro.class, ADXRS450_Gyro.getAngle);
		put(LimitSwitch.class, LimitSwitch.getValue);
		put(AnalogInput.class, AnalogInput.getValue);
		put(DigitalInput.class, DigitalInput.get);
	}};
	

	/**
	 * Command for stopping when robot becomes in range of object.
	 * @param distance in inches
	 */
	
	public SensorStop(SensorBase sensor, double endCondition, Command command) {
		this.sensor = sensor;
		this.endCondition = endCondition;
		this.command = command;
		if (sensorMethods.containsKey(sensor.getClass())) {
			try {
				method = sensorMethods.get(sensor.getClass());
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
