import java.awt.*;
import javax.swing.*;

public class ButtonPanel extends JPanel {

	private StepButton _step;

	private RunSlowButton _slow;
	
	private RunFastButton _fast;

	private StopButton _stop;

	private LowerHalfButton _lower;
	
	private UpperHalfButton _upper;
	
	private RepeatButton _repeat;
	
	private ResetButton _clear;

	/**
	 * Constructor - add all of the buttons to the ButtonPanel.
	 */

	public ButtonPanel(MainPanel m) {

		// Send a reference to the Main Panel
		// to all of the buttons.

		_step = new StepButton(m);
		_slow = new RunSlowButton(m);
		_fast = new RunFastButton(m);
		_stop = new StopButton(m);
		_lower = new LowerHalfButton(m);
		_upper = new UpperHalfButton(m);
		_repeat = new RepeatButton(m);
		_clear = new ResetButton(m);
		setLayout(new FlowLayout());

		// Add all of the buttons

		add(_step);
		add(_slow);
		add(_fast);
		add(_stop);
		add(_lower);
		add(_upper);
		add(_repeat);
		add(_clear);
	}

}
