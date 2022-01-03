package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.model.RoadMap;

@SuppressWarnings("serial")
public abstract class ChangeDialog<V, T> extends JDialog{
	
	protected Controller controller;
	protected RoadMap roadMap;
	protected JComboBox<V> comboBox1;
	protected JComboBox<T> comboBox2;
	protected JLabel info, label1, label2;
	private JLabel ticksLabel;
	protected JButton ok;
	protected JSpinner ticksSpinner;
	protected int time;
	protected MainWindow mw;
	
	public ChangeDialog(Controller ctr, RoadMap roadMap, MainWindow mw) {
		this.roadMap = roadMap;
		this.controller = ctr;
		this.time = 0;
		this.mw = mw;
		initGUI();
	}

	private void initGUI() {
		JPanel panel = new JPanel(new BorderLayout());
		info = new JLabel();
		info.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(centre, BoxLayout.LINE_AXIS));
		
		label1 = new JLabel();
		label2 = new JLabel();
		ticksLabel = new JLabel("Ticks: ");
		comboBox1 = new JComboBox<V>();
		comboBox2 = new JComboBox<T>();
		ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		ticksSpinner.setToolTipText("Spinner de ticks");
		
		Dimension max = new Dimension(50, 20);
		
		comboBox1.setMaximumSize(max);
		comboBox1.setPreferredSize(max);
		
		comboBox2.setMaximumSize(max);
		comboBox2.setPreferredSize(max);
		
		ticksSpinner.setMaximumSize(max);
		ticksSpinner.setPreferredSize(max);
		
		centre.add(label1);
		centre.add(comboBox1);
		centre.add(Box.createHorizontalGlue());
		centre.add(label2);
		centre.add(comboBox2);
		centre.add(Box.createHorizontalGlue());
		centre.add(ticksLabel);
		centre.add(ticksSpinner);
		centre.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		
		
		JPanel south = new JPanel();
		JButton cancel = new JButton("Cancelar");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		ok = new JButton("OK");
		
		south.add(cancel);
		south.add(ok);
		
		panel.add(info, BorderLayout.NORTH);
		panel.add(centre, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);
		this.add(panel);
		this.setModal(true);
		this.setVisible(false);
		this.setSize(new Dimension(430, 170));
	}
	
	public abstract void showDialog(int time);
	
}
