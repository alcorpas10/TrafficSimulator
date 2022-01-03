package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;

public class Junction extends SimulatedObject {

	private List<Road> entRoads;
	private Map<Junction, Road> salRoads;
	private List<List<Vehicle>> colas;
	private Map<Road,List<Vehicle>> mapaColas;
	private int semVerde;
	private int lastSemCambio;
	private LightSwitchingStrategy semaforoStrat;
	private DequeuingStrategy dequeuStrat;
	private int x; //En esta parte de la practica no se usara
	private int y; //En esta parte de la practica no se usara
	
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws EventExecutionException {
		super(id);
		if (lsStrategy != null && dqStrategy != null && xCoor >= 0 && yCoor >= 0) {
			entRoads = new ArrayList<>();
			salRoads = new HashMap<>();
			colas = new ArrayList<>();
			mapaColas = new HashMap<>();
			semVerde = -1;
			lastSemCambio = 0; //TODO Comprobar a que valor hay que inicializarlo
			semaforoStrat = lsStrategy;
			dequeuStrat = dqStrategy;
			x = xCoor;
			y = yCoor;
		}
		else throw new EventExecutionException("Argumentos invalidos para crear un cruce");
	}

	void addIncommingRoad(Road r) {
		if (r.getCruceDest().equals(this)) { 
			entRoads.add(r);
			List<Vehicle> nuevaCola = new LinkedList<>();
			colas.add(nuevaCola);
			mapaColas.put(r, nuevaCola);
		}
		else throw new IllegalArgumentException("El argumento recibido no es una carretera entrante");
	}
	
	void addOutGoingRoad(Road r) {
		Junction junc = r.getCruceDest();
		if(!salRoads.containsKey(junc)) {
			if(this.equals(r.getCruceOrig())) {
				salRoads.put(junc, r);
			}
			else throw new IllegalArgumentException("El argumento recibido no es una carretera saliente");
		}
		else throw new IllegalArgumentException("Ya existe otra carretera que une los dos cruces");
	}
	
	void enter(Vehicle v) {
		Road r =  v.getCarretera();
		mapaColas.get(r).add(v);
	}
	
	Road roadTo(Junction j) {
		return salRoads.get(j);		
	}
	
	@Override
	void advance(int time) {
		if(semVerde != -1) {
			List<Vehicle> lista = dequeuStrat.dequeue(colas.get(semVerde));
			for(Vehicle v: lista) {
				v.moveToNextRoad();
				colas.get(semVerde).remove(v);
			}
		}
		int nextGreen = semaforoStrat.chooseNextGreen(entRoads, colas, semVerde, lastSemCambio, time);
		if(nextGreen != semVerde) {
			lastSemCambio = time;
			semVerde = nextGreen;
		}
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("id", _id);
		if (semVerde != -1)
			obj.put("green", entRoads.get(semVerde).getId());
		else
			obj.put("green", "none");
		/*JSONArray jarray = new JSONArray();
		Set<Road> listacolas = mapaColas.keySet();
		for (Road r : listacolas) {
			JSONObject objeto = new JSONObject();
			objeto.put("road", r.getId());
			objeto.put("vehicles", mapaColas.get(r));
			jarray.put(objeto);
		}*/
		JSONArray jarray = new JSONArray();
        for(int i = 0; i < entRoads.size(); i++) {
            JSONObject jobj = new JSONObject();
            jobj.put("road", entRoads.get(i).getId());
            JSONArray jarray2 = new JSONArray();
            List<Vehicle> lista = colas.get(i);
            for (Vehicle v : lista) {
            	jarray2.put(v.toString());
            }
            jobj.put("vehicles", jarray2);
            jarray.put(jobj);
        }
		obj.put("queues", jarray);
		return obj;
	}
}
