package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.TalonSRX;

public class Motor extends TalonSRX implements MotorInit{

	public Motor(int channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	public void setPower(double power) {
		this.set(power);
	}
}
