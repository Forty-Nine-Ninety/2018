package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import org.usfirst.frc.team4990.robot.SmartDashboardController;
public class LimitSwitch {
	private DigitalInput dInput;
	
	/**
	 * Initialize limit switch. Use getValue() to read state.
	 * @param digitalIOChannel DigitalIO on RoboRIO
	 */
	
	public LimitSwitch(int digitalIOChannel) {
		dInput = new DigitalInput(digitalIOChannel);
	}
	
	public LimitSwitch(DigitalInput digitalInput) {
		dInput = digitalInput;
	}
	
	/**
	 * Use this method to read value of limit switch.
	 * @return boolean (is switch triggered)
	 */
	
	public boolean getValue() {
		return dInput.get();
	}

}
