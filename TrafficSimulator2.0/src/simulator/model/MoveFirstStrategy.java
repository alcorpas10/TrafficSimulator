package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class MoveFirstStrategy implements DequeuingStrategy {

	public MoveFirstStrategy() {
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> lista = new ArrayList<>(1);
		if(!q.isEmpty())
			lista.add(q.get(0));
		return lista;
	}

	@Override
	public JSONObject report() {
		JSONObject ob = new JSONObject();
		ob.put("type", "move_first_dqs");
		ob.put("data", new JSONObject());
		return ob;
	}
}
