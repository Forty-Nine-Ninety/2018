package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.Preferences;

public class SmartDashboardInterface {

	private Preferences preferences = Preferences.getInstance();

	/**
	 * Retrieves a numerical constant from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            number to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	public double getConst(String key, double def) {

		if (!preferences.containsKey("Const/" + key)) {
			preferences.putDouble("Const/" + key, def);
			if (preferences.getDouble("Const/ + key", def) != def) {
				System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getDouble("Const/" + key, def);
	}

	/**
	 * Retrieves a string from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            string to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	
	public String getString(String key, String def) {
		if (!preferences.containsKey("String/" + key)) {
			preferences.putString("String/" + key, def);
			if (preferences.getString("String/ + key", def) != def) {
				System.err.println("pref Key" + "String/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getString("String/" + key, def);
	}

	/**
	 * Adds a constant to SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            value to be stored
	 * @author Deep Blue Robotics (Team 199)
	 */

	
	public void putConst(String key, double def) {
		preferences.putDouble("Const/" + key, def);
		if (preferences.getDouble("Const/ + key", def) != def) {
			System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
		}
	}

	
	public void putString(String string, String def) {
		preferences.putString("String/" + string, def);
		if (preferences.getString("String/ + key", def) != def) {
			System.err.println("pref Key" + "String/" + def + "already taken by a different type");
		}

	}

	/**
	 * Retrieves a boolean from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            boolean to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	@SuppressWarnings("unused")
	public boolean getBool(String key, boolean def) {
		if (!preferences.containsKey("Bool/" + key)) {
			preferences.putBoolean("Bool/" + key, def);
			if (preferences.getBoolean("Bool/" + key, def) == def) {
				System.err.println("pref Key" + "Bool/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getBoolean("Bool/" + key, def);
	}

	
	public void putBool(String string, Boolean def) {
		preferences.putBoolean("Boolean/" + string, def);
		if (preferences.getBoolean("Boolean/ + key", def) != def) {
			System.err.println("pref Key" + "Boolean/" + def + "already taken by a different type");
		}

	}
}