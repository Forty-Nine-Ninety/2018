package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {
	private DigitalInput limitSwitch;
	
	/**
	 * Initialize limit switch. Use getValue() to read state.
	 * @param digitalIOChannel
	 */
	
	public LimitSwitch(int digitalIOChannel) {
		this.limitSwitch = new DigitalInput(digitalIOChannel);
	}
	
	/**
	 * Use this method to read value of limit switch.
	 * @return boolean (is switch triggered)
	 */
	
	public boolean getValue() {
		return limitSwitch.get();
	}

}
