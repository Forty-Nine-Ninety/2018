package org.usfirst.frc.team4990.robot.subsystems.motors;

import edu.wpi.first.wpilibj.Talon;

public class TalonSRXMotorController extends Talon implements Motor {
	public TalonSRXMotorController(int pwmPort) {
		super(pwmPort);
	}
	
	@Override
	public void setPower(double power) {
		this.set(power);
	}
}
