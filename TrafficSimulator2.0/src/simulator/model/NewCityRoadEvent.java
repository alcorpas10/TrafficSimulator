package simulator.model;

import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;

public class NewCityRoadEvent extends NewRoadEvent {
	
	public NewCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) throws EventExecutionException {
		map.addRoad(new CityRoad(id, map.getJunction(srcJunc), map.getJunction(destJunc), maxSpeed, co2Limit, length, weather));
	}
	
	@Override
	public String toString() {
		return "New City Road '" + id + "'";
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = super.report();
		obj.put("type", "new_city_road");
		return obj;
	}
}
