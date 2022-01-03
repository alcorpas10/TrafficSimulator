package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.exceptions.ControllerException;
import simulator.exceptions.EventExecutionException;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator simulador;
	private Factory<Event> fabrica;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws ControllerException {
		if (sim != null && eventsFactory != null) {
			simulador = sim;
			fabrica = eventsFactory;
		}
		else throw new ControllerException(); 
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray jarray = jo.getJSONArray("events");
		for (int i = 0; i < jarray.length(); i++) {
			simulador.addEvent(fabrica.createInstance(jarray.getJSONObject(i)));
		}
	}
	public void run(int n, OutputStream out) throws EventExecutionException {
		JSONObject obj = new JSONObject();
		JSONArray jarray = new JSONArray();
		for (int i = 0; i < n; i++) {
			simulador.advance();
			jarray.put(simulador.report());
		}
		obj.put("states", jarray);
		if (out != null) {
			PrintStream p = new PrintStream(out);
			p.println(obj.toString(3));
		}
		else {
			System.out.println(obj.toString(3) + "\n\n");
		}
	}
	
	public void run(int n) throws EventExecutionException {
		
		for (int i = 0; i < n; i++) {
			simulador.advance();
		}
	}
	
	public void reset() {
		simulador.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		simulador.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		simulador.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		simulador.addEvent(e);
	}
}
