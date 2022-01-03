package simulator.model;

import java.util.ArrayList;
import java.util.List;

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

}
