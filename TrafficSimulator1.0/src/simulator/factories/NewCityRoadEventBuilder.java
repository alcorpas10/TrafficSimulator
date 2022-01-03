package simulator.factories;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheRoad(int time, String id, String src, String dest, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		return new NewCityRoadEvent(time, id, src, dest, length, co2Limit, maxSpeed, weather);
	}

}
