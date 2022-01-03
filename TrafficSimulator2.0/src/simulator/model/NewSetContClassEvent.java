package simulator.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.NotCriticalException;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if(cs != null) {
			this.cs = cs;
		}
		else {
			throw new IllegalArgumentException("La lista de contaminacion esta vacia");
		}
	}

	@Override
	void execute(RoadMap map) throws NotCriticalException {
		for(Pair<String,Integer> par: cs) {
			String vehicleString = par.getFirst();
			int contClass = par.getSecond();
			Vehicle vehicle = map.getVehicle(vehicleString);
			
			if(vehicle != null) vehicle.setContaminationClass(contClass);
			else throw new NotCriticalException("No existe el vehiculo " + vehicleString, "SetContClassEvent");
		}
	}
	
	@Override
	public String toString() {
		String texto = "";
		for (Pair<String, Integer> par : cs) {
			if (!texto.equals("")) 
				texto += ", ";
			texto += "(" + par.getFirst() + ", " + par.getSecond() + ")";
		}
		return "Cambio Clase de Contaminacion: [" + texto + "]";
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray info = new JSONArray();
		JSONObject infoObject;
		for(Pair<String,Integer> par: cs) {
			infoObject = new JSONObject();
			infoObject.put("vehicle", par.getFirst());
			infoObject.put("class", par.getSecond());
			info.put(infoObject);
		}
		data.put("time", this._time);
		data.put("info", info);
		obj.put("type", "set_cont_class");
		obj.put("data", data);
		return obj;
	}
}
