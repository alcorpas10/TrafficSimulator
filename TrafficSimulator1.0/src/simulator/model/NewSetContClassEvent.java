package simulator.model;

import java.util.List;

import simulator.exceptions.NotCriticalException;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if(cs != null) {
			this.cs = cs;
		}
		else {
			throw new IllegalArgumentException("La lista de contaminacion esta vacia");
		}
	}

	@Override
	void execute(RoadMap map) throws NotCriticalException {
		for(Pair<String,Integer> par: cs) {
			String vehicleString = par.getFirst();
			int contClass = par.getSecond();
			Vehicle vehicle = map.getVehicle(vehicleString);
			
			if(vehicle != null) vehicle.setContaminationClass(contClass);
			else throw new NotCriticalException("No existe el vehiculo " + vehicleString, "SetContClassEvent");
		}
	}
}
