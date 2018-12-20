package org.usfirst.frc.team4990.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A command that runs another command.
 * <p>
 * The command will be killed after a certain amount of time, if it hasn't
 * already run to completion.
 */
public class TimeoutCommand extends Command {

    private final Command command;

    /**
     * Wrap the provided command with a timeout
     * 
     * @param timeout The maximum time before timing out
     * @param cmd     The command to time out
     */
    public TimeoutCommand(final double timeout, final Command cmd) {
        this("Timeout(" + cmd.getName() + ", " + timeout + ")", timeout, cmd);
    }

    /**
     * Wrap the provided command with a timeout
     * 
     * @param name    The name for the timed out command
     * @param timeout The maximum time before timing out
     * @param cmd     The command to time out
     */
    public TimeoutCommand(final String name, final double timeout, final Command cmd) {
        super(name, timeout);
        command = cmd;
    }

    @Override
    protected void initialize() {
        command.start();
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut() || command.isCompleted();
    }

    @Override
    protected void end() {
        if (command.isRunning()) {
            command.cancel();
        }
    }
}
