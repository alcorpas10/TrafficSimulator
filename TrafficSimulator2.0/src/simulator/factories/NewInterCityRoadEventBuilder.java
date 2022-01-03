package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheRoad(int time, String id, String src, String dest, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		return new NewInterCityRoadEvent(time, id, src, dest, length, co2Limit, maxSpeed, weather);
	}
}
