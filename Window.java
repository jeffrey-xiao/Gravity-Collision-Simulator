import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Create a frame inside the window
	private Field f = new Field();
	
	static double GRAVITY = 9.81;
	static double RESISTITION = 1;
	static double FRAMESPERSEC = 500;
	static double SECSPERFRAME = 1000 / FRAMESPERSEC;
	Window () throws InterruptedException {
		// setting properties of the JFrame
		super("Circle Gravity");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// creating all the JSliders
		JSlider res = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		res.setMajorTickSpacing(10);
		res.setPaintTicks(true);
		res.setLabelTable(createLabels(0, 100, 0.0, 1.0, 4));
		res.setPaintLabels(true);
		res.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged (ChangeEvent e) {
				JSlider newSlider = (JSlider)(e.getSource());
				RESISTITION = newSlider.getValue()/100.0;
			}
		});
		
		JSlider gravity = new JSlider(JSlider.HORIZONTAL, 0, 250, 100);
		gravity.setMajorTickSpacing(50);
		gravity.setPaintTicks(true);
		gravity.setLabelTable(createLabels(0, 250, 0.00, 25.0, 4));
		gravity.setPaintLabels(true);
		gravity.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged (ChangeEvent e) {
				JSlider newSlider = (JSlider)(e.getSource());
				GRAVITY = newSlider.getValue()/10.0;
			}
		});
		
		JSlider fps = new JSlider(JSlider.HORIZONTAL, 60, 500, 250);
		fps.setMajorTickSpacing(50);
		fps.setPaintTicks(true);
		fps.setLabelTable(createLabels(60, 500, 60, 500, 4));
		fps.setPaintLabels(true);
		fps.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged (ChangeEvent e) {
				JSlider newSlider = (JSlider)(e.getSource());
				FRAMESPERSEC = newSlider.getValue();
				SECSPERFRAME = 1000 / FRAMESPERSEC;
			}
		});
		// Adding the JSliders to their individual Panels
		JPanel resFrame = new JPanel(new BorderLayout());
		resFrame.add(new JLabel("Coefficient of Restitution"), BorderLayout.NORTH);
		resFrame.add(res, BorderLayout.SOUTH);
		JPanel gravityFrame = new JPanel(new BorderLayout());
		gravityFrame.add(new JLabel("Gravity (m/s^2)"), BorderLayout.NORTH);
		gravityFrame.add(gravity, BorderLayout.SOUTH);
		JPanel fpsFrame = new JPanel(new BorderLayout());
		fpsFrame.add(new JLabel("Frames per second (FPS)"), BorderLayout.NORTH);
		fpsFrame.add(fps, BorderLayout.SOUTH);
		
		// Setting the layout manager to be a grid bag manager
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// formatting the position of the components
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 10;
		
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(resFrame, c);
		c.gridx = 1;
		c.gridy = 0;
		contentPane.add(gravityFrame, c);
		c.gridx = 2;
		c.gridy = 0;
		contentPane.add(fpsFrame, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weighty = 90;
		c.fill = GridBagConstraints.BOTH;
		contentPane.add(f, c);
		
		// displaying the frame
		setVisible(true);
		
		// main loop that handles the progression of frames
		int time = 0;
		while (true) {
			if (time > SECSPERFRAME) {
				f.update();
				f.revalidate();
				repaint();
				time = 0;
			} else {
				time++;
			}
			Thread.sleep(1);
		}
	}
	// creating labels given two ranges and the number of gaps
	private Dictionary<Integer, JLabel> createLabels (int start1, int end1, double start2, double end2, int gap) {
		Hashtable<Integer, JLabel> res = new Hashtable<Integer, JLabel>();
		int inc1 = (end1 - start1)/gap;
		double inc2 = (end2 - start2)/gap;
		for (int i = 0; i < gap + 1; i++)
			res.put(new Integer(start1 + inc1 * i), new JLabel(Double.toString(start2 + inc2 * i)));
		return res;
	}
}
