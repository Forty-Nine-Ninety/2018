package org.usfirst.frc.team4990.robot;

public interface SmartDashboardInterface {

	public double getConst(String key, double def);

	public void putConst(String key, double def);

	public String getString(String key, String def);

	public void putString(String string, String def);

	void putBool(String string, Boolean def);

}