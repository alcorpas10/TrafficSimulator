package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray lista = data.getJSONArray("info");
		List<Pair<String, Integer>> cs = new ArrayList<>(lista.length());
		for (int i = 0; i < lista.length(); i++) {
			JSONObject obj = lista.getJSONObject(i);
			Pair<String, Integer> par = new Pair<String, Integer>(obj.getString("vehicle"), obj.getInt("class"));
			cs.add(par);
		}
		return new NewSetContClassEvent(time, cs);
	}

}
