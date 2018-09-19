package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.Sendable;

public interface SmartDashboardInterface {

	public double getConst(String key, double def);

	public void putConst(String key, double def);

	public void putData(String string, Sendable data);

	public void putNumber(String string, double d);

	public void putBoolean(String string, boolean b);

	public String getString(String key, String def);

}