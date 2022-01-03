package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
 
	private List<Junction> crucesList;
	private List<Road> carreterasList;
	private List<Vehicle> vehiculosList;
	private Map<String, Junction> crucesMap;
	private Map<String, Road> carreterasMap;
	private Map<String, Vehicle> vehiculosMap;
	
	RoadMap() {
		crucesList = new ArrayList<>();
		carreterasList = new ArrayList<>();
		vehiculosList = new ArrayList<>();
		crucesMap = new TreeMap<>();
		carreterasMap = new TreeMap<>();
		vehiculosMap = new TreeMap<>();
	}
	
	void addJunction(Junction j) {
		if(!crucesMap.containsKey(j.getId())) {
			crucesList.add(j);
			crucesMap.put(j.getId(), j);
		}
	}
	void addRoad(Road r) {
		if(!carreterasMap.containsKey(r.getId()) && crucesMap.containsKey(r.getCruceOrig().getId()) &&
				crucesMap.containsKey(r.getCruceDest().getId())) {
			carreterasList.add(r);
			carreterasMap.put(r.getId(), r);
		}
		else 
			throw new IllegalArgumentException("La carretera no es valida");
	}
	void addVehicle(Vehicle v) {
		boolean ok = true;
		if(!vehiculosMap.containsKey(v.getId())) {
			List<Junction> lista = v.getItinerario();
			Junction j1 = lista.get(0), j2;
			for(int i = 1; i < lista.size() && ok == true; i++) {
				j2 = lista.get(i);
				ok = carreterasMap.containsKey(j1.roadTo(j2).getId());
				j1 = j2;
			}
			if(ok) {
				vehiculosList.add(v);
				vehiculosMap.put(v.getId(), v);
			}
			else
				throw new IllegalArgumentException("El vehiculo no puede cumplir su itinerario");
		}
		else
			throw new IllegalArgumentException("El vehiculo ya existe");
	}
	public Junction getJunction(String id) {
		return crucesMap.get(id);
	}
	public Road getRoad(String id) {
		return carreterasMap.get(id);
	}
	public Vehicle getVehicle(String id) {
		return vehiculosMap.get(id);
	}
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(new ArrayList<>(crucesList)); //TODO preguntar si hace falta el new o poner return Collections.unmodifiableList(crucesList)
	}
	public List<Road> getRoads(){
		return Collections.unmodifiableList(new ArrayList<>(carreterasList));
	}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(new ArrayList<>(vehiculosList));
	}
	void reset() {
		crucesList.clear();
		carreterasList.clear();
		vehiculosList.clear();
		crucesMap.clear();
		carreterasMap.clear();
		vehiculosMap.clear();
	}
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		JSONArray jarrayJ = new JSONArray();
		JSONArray jarrayR = new JSONArray();
		JSONArray jarrayV = new JSONArray();
		for (Junction j : crucesList)
			jarrayJ.put(j.report());
		for (Road r : carreterasList)
			jarrayR.put(r.report());
		for (Vehicle v : vehiculosList)
			jarrayV.put(v.report());
		obj.put("junctions", jarrayJ);
		obj.put("roads", jarrayR);
		obj.put("vehicles", jarrayV);
		return obj;
	}
}
