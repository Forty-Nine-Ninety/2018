package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class ElevatorEncoder implements PIDSource {

	private Elevator elevator;
	private PIDSourceType pidSource;
	
	public ElevatorEncoder(Elevator e) {
		this.elevator = e;
	}

	public void setPIDSourceType(PIDSourceType pidSource) {
		this.pidSource = pidSource;
		
	}

	public PIDSourceType getPIDSourceType() {
		return pidSource;
	}

	public double pidGet() {
		return elevator.elevatorMotor.getSelectedSensorPosition(0);
	}


}
