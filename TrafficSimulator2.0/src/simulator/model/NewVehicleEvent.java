package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;



public class NewVehicleEvent extends Event{

	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;
	
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) throws EventExecutionException{
		List<Junction> itineraryJunction = new ArrayList<>(itinerary.size());
		
		for(String aux: itinerary) {
			itineraryJunction.add(map.getJunction(aux));
		
		}
		Vehicle vehiculo = new Vehicle(id, maxSpeed, contClass, itineraryJunction);
		map.addVehicle(vehiculo);
		vehiculo.moveToNextRoad();
	}


	
	@Override
	public String toString() {
		return "New Vehicle '" + id + "'";
	}

	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("time", this._time);
		data.put("id", this.id);
		data.put("maxspeed", this.maxSpeed);
		data.put("class", this.contClass);
		data.put("itinerary", itinerary);
		obj.put("type", "new_vehicle");
		obj.put("data", data);
		return obj;
	}
}
