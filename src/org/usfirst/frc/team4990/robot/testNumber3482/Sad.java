package org.usfirst.frc.team4990.robot.testNumber3482;

import edu.wpi.first.wpilibj.TalonSRX;

public class Sad extends TalonSRX implements Sad2{

	public Sad(int channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setPower(double power) {
		this.set(power);
	}
}
