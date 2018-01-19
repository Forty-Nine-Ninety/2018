package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.TalonSRX;

<<<<<<< HEAD:src/org/usfirst/frc/team4990/robot/Sad.java
public class Sad extends TalonSRX{
=======
public class Motor extends TalonSRX implements MotorInit{
>>>>>>> 45937e228989cfb2755081fc083d378c4bbf8c42:src/org/usfirst/frc/team4990/robot/Motor.java

	public Motor(int channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	public void setPower(double power) {
		this.set(power);
	}
}
