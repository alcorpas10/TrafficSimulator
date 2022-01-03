package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;

public class NewJunctionEvent extends Event {
	
	private LightSwitchingStrategy lsStrategy; 
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;
	private String id;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	@Override
	void execute(RoadMap map) throws EventExecutionException {
		map.addJunction(new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor));
	}
	
	@Override
	public String toString() {
		return "New Junction '" + id + "'";
	}

	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray coor = new JSONArray();
		coor.put(this.xCoor);
		coor.put(this.yCoor);
		data.put("time", this._time);
		data.put("id", this.id);
		data.put("coor", coor);
		data.put("ls_strategy", lsStrategy.report());
		data.put("dq_strategy", dqStrategy.report());
		obj.put("type", "new_junction");
		obj.put("data", data);
		return obj;
	}
}
