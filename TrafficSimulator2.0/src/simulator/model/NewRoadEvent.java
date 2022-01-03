package simulator.model;

import org.json.JSONObject;

public abstract class NewRoadEvent extends Event {

	protected String id;
	protected String srcJunc;
	protected String destJunc;
	protected int length;
	protected int co2Limit;
	protected int maxSpeed;
	protected Weather weather;
	
	public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id = id;
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("time", this._time);
		data.put("id", this.id);
		data.put("src", this.srcJunc);
		data.put("dest", this.destJunc);
		data.put("length", this.length);
		data.put("co2limit", this.co2Limit);
		data.put("maxspeed", this.maxSpeed);
		data.put("weather", this.weather.toString());
		obj.put("data", data);
		return obj;
	}
}
