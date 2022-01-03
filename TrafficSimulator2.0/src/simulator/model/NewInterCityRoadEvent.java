package simulator.model;

import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;

public class NewInterCityRoadEvent extends NewRoadEvent {
	
	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) throws EventExecutionException {
		map.addRoad(new InterCityRoad(id, map.getJunction(srcJunc), map.getJunction(destJunc), maxSpeed, co2Limit, length, weather));
	}
	
	@Override
	public String toString() {
		return "New Inter City Road '" + id + "'";
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = super.report();
		obj.put("type", "new_inter_city_road");
		return obj;
	}
}
