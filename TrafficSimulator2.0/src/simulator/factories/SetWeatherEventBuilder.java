package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		JSONArray lista = data.getJSONArray("info");
		List<Pair<String, Weather>> ws = new ArrayList<>(lista.length());
		for (int i = 0; i < lista.length(); i++) {
			JSONObject obj = lista.getJSONObject(i);
			Weather weather = Weather.valueOf(obj.getString("weather").toUpperCase());
			Pair<String, Weather> par = new Pair<String, Weather>(obj.getString("road"), weather);
			ws.add(par);
		}
		return new SetWeatherEvent(time, ws);
	}

}
