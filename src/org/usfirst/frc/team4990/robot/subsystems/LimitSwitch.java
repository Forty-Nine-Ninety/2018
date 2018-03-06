package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {
	private DigitalInput limitSwitch;
	private Counter counter;
	
	private int currentCount = 0;
	private int lastCount = 0;
	
	private int counterSensitivity; // threshold to compensate for noise
	
	/**
	 * Initialize limit switch. Use getValue() to read state.
	 * @param digitalIOChannel
	 * @param counterSensitivity
	 */
	
	public LimitSwitch(int digitalIOChannel) {
		this.limitSwitch = new DigitalInput(digitalIOChannel);
		this.counter = new Counter(this.limitSwitch);
		this.counterSensitivity = 4;
	}
	
	/**
	 * @deprecated
	 * Used for counter system. Instead, use getValue() to read state. 
	 */
	
	public void update() {
		this.lastCount = this.currentCount;
		this.currentCount = this.counter.get();
		//System.out.println("lastCount: " + this.lastCount + "; currentCount: " + this.currentCount);
		//System.out.println(limitSwitch.get());
	}
	
	/** 
	 * Instead, use getValue() to read state. 
	 * @deprecated
	 * @return boolean if switch is triggered (inaccurate?)
	 */
	
	public boolean isSwitched() {
		return this.currentCount - this.lastCount > this.counterSensitivity;
	}
	
	/**
	 * @deprecated
	 * Used for counter system. Instead, use getValue() to read state. 
	 */
	
	public void reset() {
		this.counter.reset();
		this.currentCount = 0;
		this.lastCount = 0;
	}
	
	/**
	 * Instead, use getValue() to read state. 
	 * @deprecated
	 * @return int of counter
	 */
	
	public int getCurrCount() {
		return this.currentCount;
	}
	
	/**
	 * Use this method to read value of limit switch.
	 * @return boolean (is switch triggered)
	 */
	
	public boolean getValue() {
		return limitSwitch.get();
	}
	
	/**
	 * Part of counter system. Instead, use getValue() to read state. 
	 * @deprecated
	 * @return int last counter reading
	 */
	
	public int getLastCount() {
		return this.lastCount;
	}
}
