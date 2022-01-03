package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class MoveAllStrategy implements DequeuingStrategy {

	public MoveAllStrategy() {
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> lista = new ArrayList<>(q.size());
		for (Vehicle v : q) {
			lista.add(v);
		}
		return lista;
	}
	
	@Override
	public JSONObject report() {
		JSONObject ob = new JSONObject();
		ob.put("type", "most_all_dqs");
		ob.put("data", new JSONObject());
		return ob;
	}
}
