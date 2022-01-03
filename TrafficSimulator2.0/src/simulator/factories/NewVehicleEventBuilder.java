package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int clase = data.getInt("class");
		JSONArray lista = data.getJSONArray("itinerary");
		List<String> itinerary = new ArrayList<>(lista.length());
		for (int i = 0; i < lista.length(); i++) {
			itinerary.add(lista.getString(i));
		}
		return new NewVehicleEvent(time, id, maxSpeed, clase, itinerary);
	}

}
