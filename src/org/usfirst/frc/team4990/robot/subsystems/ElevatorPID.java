package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.PIDOutput;

public class ElevatorPID implements PIDOutput{
	
	public Elevator elevator;
	
	public ElevatorPID(Elevator e) {
		elevator = e;
	}
	/**
	 * Sets elevator power to output
	 * @param output elevator power level
	 */
	public void pidWrite(double output) {
		elevator.setElevatorPower(output);
	}
}
