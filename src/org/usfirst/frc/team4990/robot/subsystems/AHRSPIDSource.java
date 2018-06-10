package org.usfirst.frc.team4990.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AHRSPIDSource implements PIDSource {

	private AHRS ahrs;
	private PIDSourceType pidSourceType = PIDSourceType.kDisplacement;
	
	public AHRSPIDSource(AHRS ahrs) {
		this.ahrs = ahrs;
	}
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		pidSourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSourceType;
	}

	@Override
	public double pidGet() {
		return ahrs.getAngle();
	}

}
