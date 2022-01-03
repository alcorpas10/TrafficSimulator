package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {

	public NewRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		String src = data.getString("src");
		String dest = data.getString("dest");
		int length = data.getInt("length");
		int co2Limit = data.getInt("co2limit");
		int maxSpeed = data.getInt("maxspeed");
		Weather weather = Weather.valueOf(data.getString("weather").toUpperCase());
		return createTheRoad(time, id, src, dest, length, co2Limit, maxSpeed, weather);
	}
	
	protected abstract Event createTheRoad(int time, String id, String src, String dest,
			int length, int co2Limit, int maxSpeed, Weather weather);
}
