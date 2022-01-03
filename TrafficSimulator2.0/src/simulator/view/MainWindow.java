package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONException;

import extra.jtable.EventsTableModel;
import extra.jtable.JunctionsTableModel;
import extra.jtable.RoadsTableModel;
import extra.jtable.VehiclesTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements TrafficSimObserver {

	private Controller _ctrl;
	private boolean _stopped;
	private ChangeCO2ClassDialog co2ClassDialog;
	private ChangeWeatherDialog weatherDialog;
	private JFileChooser chooser;
	private ControlPanel controlPanel;
	private RoadMap roadMap;
	private File file;
	private int time;
	
	public MainWindow(Controller ctrl) {
		super ("Traffic Simulator");
		roadMap = null;
		_ctrl = ctrl;
		time = 0;
		_ctrl.addObserver(this);
		_stopped = true;
		file = null;
		initGUI();
	}
	
	private void initGUI() {
		co2ClassDialog = new ChangeCO2ClassDialog(_ctrl, roadMap, this);
		weatherDialog = new ChangeWeatherDialog(_ctrl, roadMap, this);
		chooser = new JFileChooser(System.getProperty("user.dir") + "/resources/examples");
		chooser.setFileFilter(new FileNameExtensionFilter("Archivo JSON (*.json)", "json"));
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		controlPanel = new ControlPanel(this);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel( new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// tables
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);	
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);
		
		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		JPanel roadMapView = createViewPanel(new MapByRoadComponent(_ctrl), "Map by Road");
		roadMapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(roadMapView);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setMinimumSize(new Dimension(600,400));
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JScrollPane(c));
		Border borde = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title);
		p.setBorder(borde);
		return p;
	}
	
	public void fileChooserDialog() {
		chooser.setSelectedFile(new File("ex1.json"));
		int selection = chooser.showOpenDialog(this);
		if(selection == JFileChooser.APPROVE_OPTION) {
			InputStream inputStream;
			_ctrl.reset();
			try {
				file = chooser.getSelectedFile();
				inputStream = new FileInputStream(file);
				_ctrl.loadEvents(inputStream);
			} catch (FileNotFoundException | IllegalArgumentException | JSONException e) {
				JOptionPane.showMessageDialog(this,"No se ha podido cargar el archivo de entrada", "Error al cargar archivo", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void co2ClassDialog(){
		co2ClassDialog.showDialog(this.time);
	}
	
	public void weatherDialog() {
		weatherDialog.showDialog(this.time);
	}
	
	public void play(int ticks) {
		_stopped = false;
		controlPanel.enableToolBar(false);
		run_sim(ticks);
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			controlPanel.enableToolBar(true);
			_stopped = true;
		}
	}
	
	public void stop() {
		_stopped = true;
	}

	public int getTicks() {
		return controlPanel.getTicks();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.roadMap = map;
		this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.roadMap = map;
		this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.roadMap = map;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.roadMap = map;
		this.time = time;
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(this, err,"Error", JOptionPane.ERROR_MESSAGE);
	}
}
