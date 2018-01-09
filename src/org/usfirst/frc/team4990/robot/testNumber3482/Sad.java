package prototype_2018;

import edu.wpi.first.wpilibj.PWMTalonSRX;

public class Sad extends PWMTalonSRX implements Sad2{

	public Sad(int channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setPower(double power) {
		this.set(power);
	}
}
