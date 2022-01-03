package simulator.model;

import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;
import simulator.exceptions.NotCriticalException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	
	private RoadMap roadMap;
	private List<Event> listEventos;
	private int simTime;

	public TrafficSimulator() {
		roadMap = new RoadMap();
		listEventos = new SortedArrayList<>();
		simTime = 0;
	}

	public void addEvent(Event e) {
		listEventos.add(e);
	}
	public void advance() throws EventExecutionException{
		simTime++;
		if(listEventos.size() != 0) {
			Event e = listEventos.get(0);
			while (e.getTime() == simTime && listEventos.size() != 0) {
				listEventos.remove(e);
				try {
					e.execute(roadMap);
				} catch (NotCriticalException ex) {
					System.err.format(ex.toString() + ". Error al cargar el evento " + ex.getEvent() + ". La simulacion CONTINUA\n");
				}
				if (listEventos.size() != 0)
					e = listEventos.get(0);
			}
		}
		for(Junction j: roadMap.getJunctions()) {
			j.advance(simTime);
		}
		for(Road r: roadMap.getRoads()) {
			r.advance(simTime);
		}
	}
	public void reset() {
		roadMap.reset();
		listEventos.clear();
		simTime = 0;
	}
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("time", simTime);
		obj.put("state", roadMap.report());
		return obj;
	}
}
