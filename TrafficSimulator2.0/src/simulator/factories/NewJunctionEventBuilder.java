package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data){
		int time = data.getInt("time"), xcoor, ycoor;
		String id = data.getString("id");
		JSONArray coors = data.getJSONArray("coor");
		JSONObject ls = data.getJSONObject("ls_strategy");
		JSONObject dq = data.getJSONObject("dq_strategy");
		LightSwitchingStrategy lss = lssFactory.createInstance(ls);
		DequeuingStrategy dqs = dqsFactory.createInstance(dq);
		xcoor = coors.getInt(0);
		ycoor = coors.getInt(1);
		return new NewJunctionEvent(time, id, lss, dqs, xcoor, ycoor);
	}

}
