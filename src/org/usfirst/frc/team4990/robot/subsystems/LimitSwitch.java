package org.usfirst.frc.team4990.robot.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {
	private DigitalInput limitSwitch;
	private Counter counter;
	
	private int currentCount = 0;
	private int lastCount = 0;
	
	private int counterSensitivity; // threshold to compensate for noise
	
	public LimitSwitch(int digitalIOChannel, int counterSensitivity) {
		this.limitSwitch = new DigitalInput(digitalIOChannel);
		this.counter = new Counter(this.limitSwitch);
		this.counterSensitivity = counterSensitivity;
	}
	
	public void update() {
		this.lastCount = this.currentCount;
		this.currentCount = this.counter.get();
		//System.out.println("lastCount: " + this.lastCount + "; currentCount: " + this.currentCount);
		//System.out.println(limitSwitch.get());
	}
	
	public boolean isSwitched() {
		return this.currentCount - this.lastCount > this.counterSensitivity;
	}
	
	public void reset() {
		this.counter.reset();
		this.currentCount = 0;
		this.lastCount = 0;
	}
	
	public int getCurrCount() {
		return this.currentCount;
	}
	
	public boolean getValue() {
		return limitSwitch.get();
	}
	
	public int getLastCount() {
		return this.lastCount;
	}
}
