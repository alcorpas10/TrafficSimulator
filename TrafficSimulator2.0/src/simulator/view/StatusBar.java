package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimObserver {
	
	private Controller controller;
	private JLabel simTime;
	private JLabel info;

	public StatusBar(Controller _ctrl) {
		controller = _ctrl;
		controller.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		simTime = new JLabel("0");
		info = new JLabel("Bienvenido!");
		JSeparator separador = new JSeparator(JSeparator.VERTICAL);
		separador.setPreferredSize(new Dimension(1, 20));
		JSeparator separador1 = new JSeparator();
		separador1.setPreferredSize(new Dimension(50, 0));
		
		this.add(new JLabel("Time: "));
		this.add(simTime);
		this.add(separador1);
	    this.add(separador);
		this.add(info);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		simTime.setText(Integer.toString(time));
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		info.setText(e.toString());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		simTime.setText("0");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {
		info.setText(err);
	}


}
