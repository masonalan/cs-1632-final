import java.awt.*;
import javax.swing.*;

public class MainFrame {

	private static final int HEIGHT = 600;
	private static final int WIDTH = 800;

	private JFrame _frame = new JFrame("Bean Counter");

	private MainPanel _mainPanel;

	private ButtonPanel _buttonPanel;

	/**
	 * Constructor - creates the main panel. Inside it is the main panel and the
	 * button panel.
	 * 
	 * @param beanCount number of beans in the machine
	 * @param luck      whether beans progress through pure luck (or skill)
	 */
	public MainFrame(int beanCount, boolean luck) {

		_frame.setSize(MainFrame.WIDTH, MainFrame.HEIGHT);
		// Close program when window is closed
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add Main Panel and Button Panel

		_mainPanel = new MainPanel(beanCount, luck);

		_buttonPanel = new ButtonPanel(_mainPanel);

		_frame.add(_mainPanel, BorderLayout.CENTER);
		_frame.add(_buttonPanel, BorderLayout.SOUTH);

		_frame.setVisible(true);
	}

}
