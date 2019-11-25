import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ResetButton extends JButton {

	private MainPanel _m;

	/**
	 * Constructor - Adds a listener to the button.
	 * 
	 * @param m the main animation panel where all the action happens
	 */
	public ResetButton(MainPanel m) {
		super("Reset");
		_m = m;
		addActionListener(new ResetButtonListener());
	}

	class ResetButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			_m.reset();
		}
	}

}
