package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.PIDOutput;

public class ElevatorPID implements PIDOutput{
	
	public Elevator elevator;
	
	public ElevatorPID(Elevator e) {
		elevator = e;
	}

	public void pidWrite(double output) {
		elevator.setElevatorPower(output);
	}
}
