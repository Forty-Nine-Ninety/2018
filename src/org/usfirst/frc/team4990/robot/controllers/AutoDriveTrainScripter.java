package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.DriveTrain;

import java.util.LinkedList;
import java.util.Queue;

//You shouldn't fuck with this if you don't know what you're doing

public class AutoDriveTrainScripter {
	
	public enum SCRIPTCOMMAND {
		FORWARD,
		TURN
	}
	
	public class ScriptPackage {
		public SCRIPTCOMMAND sc;
		public double milliDuration;
		public double specialval;
		public ScriptPackage(SCRIPTCOMMAND ssc, double milli, double spec) {
			sc = ssc;
			milliDuration = milli;
			specialval = spec;
		}
	}
	
	private void evaluateAndRun(ScriptPackage sp) {
		switch(sp.sc) {
		case FORWARD:
			System.out.println(sp.specialval);
			dt.setSpeed(sp.specialval, sp.specialval);
			break;
		case TURN:
			dt.setSpeed(sp.specialval, -sp.specialval);
			break;
		default:
			break;
		
		}
	}
	
	private DriveTrain dt;
	
	private Queue<ScriptPackage> cmdqueue = new LinkedList<ScriptPackage>();
	private ScriptPackage currcmd;
	private long starttime;

	public AutoDriveTrainScripter(DriveTrain dtrain) {
		dt = dtrain;
	}
	
	protected void init() {
		currcmd = cmdqueue.remove();
		starttime = System.currentTimeMillis();
	}
	
	public void update() {
		dt.setSpeed(0, 0);
		
		if(currcmd == null) return;
		
		if(starttime + currcmd.milliDuration <= System.currentTimeMillis() ) {
			if(cmdqueue.size() != 0) {
				currcmd = cmdqueue.remove();
				starttime = System.currentTimeMillis();
			}
			else {
				currcmd = null;
				return;
			}
		}
		
		evaluateAndRun(currcmd);
	}
	
	//oh boy
	//here starts the commands that can be implemented in init()!
	public void forward(double duration, double speed) {
		cmdqueue.add(new ScriptPackage(SCRIPTCOMMAND.FORWARD, duration, speed) );
	}
	
	public void turn(double duration, double speed) {
		cmdqueue.add(new ScriptPackage(SCRIPTCOMMAND.TURN, duration, speed) );
	}
}
