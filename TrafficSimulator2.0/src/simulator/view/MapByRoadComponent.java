package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

@SuppressWarnings("serial")
public class MapByRoadComponent extends JPanel implements TrafficSimObserver{

	private static final int _JRADIUS = 10;
	private static final int _ROADSTARTPOINT = 50;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _ROAD_COLOR = Color.BLACK;
	private static final Color _ROAD_LABEL_COLOR = Color.BLACK;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;
	private Image _car;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		
		
		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
	}

	private void drawRoads(Graphics g) {
		int i = 0;
		int x1 = _ROADSTARTPOINT;
		int x2 = getWidth() - 100;
		int y;
		
		for (Road r : _map.getRoads()) {
			y = (i+1)*50;
			g.setColor(_ROAD_LABEL_COLOR);
			g.drawString(r.getId(), x1/3, y + 4);
			g.setColor(_ROAD_COLOR);
			g.drawLine(x1, y, x2, y);
			drawJunctions(g, x1, x2, y, r);
			drawVehicles(g, r.getVehiculos(), r.getLongitud(), x1, x2, y);
			drawWeather(g, r.getWeather(), y);
			drawCont(g, r.getTotalContam(), r.getContLimit(), y);
			i++;
		}

	}
	
	private void drawJunctions(Graphics g, int x1, int x2, int y, Road r) {
		Color destColor = _RED_LIGHT_COLOR;
		int idx = r.getCruceDest().getSemVerde();
		if (idx != -1 && r.equals(r.getCruceDest().getEntRoads().get(idx))) {
			destColor = _GREEN_LIGHT_COLOR;
		}
		
		//Cruce origen
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getCruceOrig().getId(), x1 - 4, y - 10);
		
		//Cruce destino
		g.setColor(destColor);
		g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getCruceDest().getId(), x2 - 4, y- 10);
	}

	private void drawVehicles(Graphics g, List<Vehicle> vehiculos, int longitud, int x1, int x2, int y) {
		
		for (Vehicle v : vehiculos) {

			int x = x1 + (int) ((x2 - x1) * ((double) v.getLocalizacion() / (double) longitud));
			int vLabelColor = (int) (25.0 * (10.0 - (double) v.getClassContam()));
			g.setColor(new Color(0, vLabelColor, 0));
			g.drawString(v.getId(), x+3, y - 12);
			g.drawImage(_car, x, y - 15, 20, 20, this);
		}
	}
	
	private void drawWeather(Graphics g, Weather w, int y) {
		Image weatherImage = null;
		switch(w) {
		case SUNNY:
			weatherImage = loadImage("sun.png");
			break;
		case CLOUDY:
			weatherImage = loadImage("cloud.png");
			break;
		case RAINY:
			weatherImage = loadImage("rain.png");
			break;
		case WINDY:
			weatherImage = loadImage("wind.png");
			break;
		case STORM:
			weatherImage = loadImage("storm.png");
			break;
		}
		g.drawImage(weatherImage, getWidth() - 87, y - 16, 32, 32, this);
	}
	
	private void drawCont(Graphics g, int totalCont, int limiteCont,int y) {
		Image contImage = null;
		int cont = (int) Math.floor(Math.min((double) totalCont/(1.0 + (double) limiteCont),1.0) / 0.19);
		contImage = loadImage("cont_" + cont + ".png");
		g.drawImage(contImage, getWidth() - 45, y - 16, 32, 32, this);
	}

	

	// this method is used to update the preffered and actual size of the component,
	// so when we draw outside the visible area the scrollbars show up
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
	
		if (maxW > getWidth() || maxH > getHeight()) {
		    setPreferredSize(new Dimension(maxW, maxH));
		    setSize(new Dimension(maxW, maxH));
		}
	}


	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}


}
