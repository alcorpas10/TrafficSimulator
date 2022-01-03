package simulator.model;

import java.util.List;

import org.json.JSONObject;

public class RoundRobinStrategy implements LightSwitchingStrategy{

	private int timeslot;
	public RoundRobinStrategy(int timeslot) {
		this.timeslot = timeslot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.size() > 0) {
			if (currGreen != -1) {
				if (currTime - lastSwitchingTime >= timeslot)
					return (currGreen + 1) % roads.size();
				else return currGreen; //Mantiene todo como estaba
			}
			else return 0; //El primer semaforo debe ponerse a verde 
		}
		else return -1; //Ningun semaforo debe estar en verde (lista vacia)
	}

	@Override
	public JSONObject report() {
		JSONObject ob = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("timeslot", this.timeslot);
		ob.put("type", "round_robin_lss");
		ob.put("data", data);
		return ob;
	}

}
