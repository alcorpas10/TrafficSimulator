package simulator.model;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle>{

	private List<Junction> itinerario;
	private int maxVel;
	private int actVel;
	private VehicleStatus estado;
	private Road carretera;
	private int localizacion;
	private int classContam;
	private int totalContam;
	private int totalDist;
	private int nextJunction = 0;
	
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws EventExecutionException {
		super(id);
		if (maxSpeed > 0 && contClass >= 0 && contClass <= 10 && itinerary.size() >= 2) {
			itinerario = Collections.unmodifiableList(itinerary);
			maxVel = maxSpeed;
			actVel = 0;
			estado = VehicleStatus.PENDING;
			carretera = null;
			localizacion = 0;
			classContam = contClass;
			totalContam = 0;
			totalDist = 0;
		}
		else throw new EventExecutionException("Argumentos invalidos para crear un vehiculo"); 
	}	

	void setSpeed(int s) {
		if (s >= 0) {
			if (estado == VehicleStatus.TRAVELING) 
				actVel = Math.min(s, maxVel);
		}
		else throw new IllegalArgumentException("La velocidad de un vehiculo debe ser positiva. Encontrado -> " + s);
	}
	void setContaminationClass(int c) {
		if (c >= 0 && c <= 10) {
			classContam = c;
		}
		else throw new IllegalArgumentException("La contaminacion de un vehiculo debe estar en [0:10]. Encontrado -> " + c);
	}
	void moveToNextRoad() {
		if (estado.equals(VehicleStatus.PENDING)) {
			carretera = itinerario.get(nextJunction).roadTo(itinerario.get(nextJunction + 1));
			carretera.enter(this);
			estado = VehicleStatus.TRAVELING;
			nextJunction++;
		}
		else if (estado.equals(VehicleStatus.WAITING)) {
			carretera.exit(this);
			if (itinerario.size() == nextJunction + 1) {
				estado = VehicleStatus.ARRIVED;
			}
			else {
				carretera = itinerario.get(nextJunction).roadTo(itinerario.get(nextJunction + 1));
				localizacion = 0;
				actVel = 0;
				carretera.enter(this);
				estado = VehicleStatus.TRAVELING;
				nextJunction++;
			}	
		}
		else throw new IllegalArgumentException("El vehiculo no esta en uno de los estados "
				+ "habilitados para realizar esta accion");		
	}
	@Override
	void advance(int time) {
		if (estado.equals(VehicleStatus.TRAVELING)) {
			int nuevaLocalizacion = Math.min(localizacion + actVel, carretera.getLongitud());
			int contaminacionProd = (nuevaLocalizacion - localizacion) * classContam;
			totalContam += contaminacionProd;
			carretera.addContamination(contaminacionProd);
			if (nuevaLocalizacion >= carretera.getLongitud()) {
				totalDist += (carretera.getLongitud() - localizacion);
				localizacion = carretera.getLongitud();
				estado = VehicleStatus.WAITING;
				actVel = 0;
				itinerario.get(nextJunction).enter(this);
			}
			else {
				totalDist += (nuevaLocalizacion - localizacion);
				localizacion = nuevaLocalizacion;
			}
		}
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("id", _id);
		obj.put("speed", actVel);
		obj.put("distance", totalDist);
		obj.put("co2", totalContam);
		obj.put("class", classContam);
		obj.put("status", estado.toString());
		if (!estado.equals(VehicleStatus.PENDING) && !estado.equals(VehicleStatus.ARRIVED)) {
			obj.put("road", carretera.getId());
			obj.put("location", localizacion);
		}
		return obj;
	}
	
	public int getActVel() {
		return actVel;
	}

	public int getLocalizacion() {
		return localizacion;
	}

	public int getClassContam() {
		return classContam;
	}
	
	public Road getCarretera() {
		return carretera;
	}
	
	public int getMaxVel() {
		return maxVel;
	}


	public int getTotalContam() {
		return totalContam;
	}


	public int getTotalDist() {
		return totalDist;
	}
	
	public VehicleStatus getStatus() {
		return estado;
	}

	public List<Junction> getItinerario() {
		return itinerario;
	}


	@Override
	public int compareTo(Vehicle o) {
		if (localizacion == o.getLocalizacion()) return 0;
		else if (localizacion < o.getLocalizacion()) return 1;
		else return -1;
	}
}
