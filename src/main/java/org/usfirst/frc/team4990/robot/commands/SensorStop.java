package org.usfirst.frc.team4990.robot.commands;

import java.lang.reflect.Method;
import java.util.HashMap;
import edu.wpi.first.wpilibj.command.Command;


import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import com.kauailabs.navx.frc.AHRS;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.LimitSwitch;


public class SensorStop extends Command {
	Command command;
	double endCondition;
	Class sensor;
	Method method;
	public enum Condition {
		equal, not_equal, greater, less, greater_equals, less_equals
	}
	Condition condition;

	public HashMap<Class<?>, Method> sensorMethods;

	public SensorStop() {
		try {
			sensorMethods = new HashMap<Class<?>, Method>(){{
				put(Ultrasonic.class, Ultrasonic.class.getMethod("getRangeInches"));
				put(Encoder.class, Encoder.class.getMethod("getDistance"));
				put(AHRS.class, AHRS.class.getMethod("getAngle"));
				put(ADXRS450_Gyro.class, ADXRS450_Gyro.class.getMethod("getAngle"));
				put(LimitSwitch.class, LimitSwitch.class.getMethod("getValue"));
				put(AnalogInput.class, AnalogInput.class.getMethod("getValue"));
				put(DigitalInput.class, DigitalInput.class.getMethod("get"));
			}};
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Command for stopping when robot becomes in range of object.
	 * @param distance in inches
	 */
	
	public SensorStop(Command command, Class<?> sensor, Condition condition, double endCondition) {
		this();
		this.sensor = sensor;
		this.endCondition = endCondition;
		this.command = command;
		this.condition = condition;

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
	
	public void execute() {
		try {
			System.out.println("current sensor value: " + (double) method.invoke(sensor) +
			", stopping when value is " + condition.toString() + " to " + endCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isFinished() {
		try {
			switch (condition) {
				case not_equal: return (double) method.invoke(sensor) != endCondition;
				case greater: return (double) method.invoke(sensor) > endCondition;
				case less: return (double) method.invoke(sensor) < endCondition;
				case greater_equals: return (double) method.invoke(sensor) >= endCondition;
				case less_equals: return (double) method.invoke(sensor) <= endCondition;
				case equal: return (double) method.invoke(sensor) == endCondition;
				default: return (double) method.invoke(sensor) == endCondition;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public void end() {
		command.cancel();
	}
	
	public void interrupted() {
		end();
	}
}
