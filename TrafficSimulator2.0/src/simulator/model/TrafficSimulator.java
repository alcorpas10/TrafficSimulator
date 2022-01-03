package simulator.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;
import simulator.exceptions.NotCriticalException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
	private RoadMap roadMap;
	private List<Event> listEventos;
	private int simTime;
	private List<TrafficSimObserver> observadores;

	public TrafficSimulator() {
		roadMap = new RoadMap();
		listEventos = new SortedArrayList<>();
		observadores = new ArrayList<>();
		simTime = 0;
	}

	public void addEvent(Event e) {
		listEventos.add(e);
		onEventAdded(e);
	}
	public void advance() throws EventExecutionException{
		simTime++;
		onAdvanceStart();
		if(listEventos.size() != 0) {
			Event e = listEventos.get(0);
			while (e.getTime() == simTime && listEventos.size() != 0) {
				listEventos.remove(e);
				try {
					e.execute(roadMap);
				} catch (NotCriticalException ex) {
					onError(ex.getMessage() + ". Error al cargar el evento " + ex.getEvent() + ". La simulacion CONTINUA\n");
				} catch (IllegalArgumentException e2) {
					onError(e2.getMessage());
					throw new IllegalArgumentException(e2);
				} catch (EventExecutionException e3) {
					onError(e3.getMessage());
					throw new EventExecutionException(e3);
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
		onAdvanceEnd();
	}
	public void reset() {
		roadMap.reset();
		listEventos.clear();
		simTime = 0;
		onReset();
	}
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("time", simTime);
		obj.put("state", roadMap.report());
		return obj;
	}
	
	private void onAdvanceStart() {
		for (TrafficSimObserver t : observadores) {
			t.onAdvanceStart(roadMap, listEventos, simTime);
		}
	}
	
	private void onAdvanceEnd() {
		for (TrafficSimObserver t : observadores) {
			t.onAdvanceEnd(roadMap, listEventos, simTime);
		}
	}
	
	private void onEventAdded(Event e) {
		for (TrafficSimObserver t : observadores) {
			t.onEventAdded(roadMap, listEventos, e, simTime);
		}
	}
	
	private void onReset() {
		for (TrafficSimObserver t : observadores) {
			t.onReset(roadMap, listEventos, simTime);
		}
	}
	
	private void onRegister() {
		for (TrafficSimObserver t : observadores) {
			t.onRegister(roadMap, listEventos, simTime);
		}
	}
	
	private void onError(String s) {
		for (TrafficSimObserver t : observadores) {
			t.onError(s);
		}
	}
		
	public List<Vehicle> getVehiculos(){
		return roadMap.getVehicles();
	}
		
	public List<Road> getRoads(){
		return roadMap.getRoads();
	}

	public int getSimTime() {
		return simTime;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observadores.add(o);
		onRegister();
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observadores.remove(o);		
	}
}
