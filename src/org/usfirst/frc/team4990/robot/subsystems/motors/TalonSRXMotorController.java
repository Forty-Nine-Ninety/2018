package org.usfirst.frc.team4990.robot.subsystems.motors;

import edu.wpi.first.wpilibj.TalonSRX;

public class TalonSRXMotorController extends TalonSRX implements Motor {
	public TalonSRXMotorController(int pwmPort) {
		super(pwmPort);
	}
	
	@Override
	public void setPower(double power) {
		this.set(power);
	}
}
