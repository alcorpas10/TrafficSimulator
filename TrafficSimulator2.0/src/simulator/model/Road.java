package simulator.model;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.EventExecutionException;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {

	private Junction cruceOrig;
	private Junction cruceDest;
	private int longitud;
	protected int maxVel;
	protected int actVelLimit;
	protected int contLimit;
	protected Weather weather;
	protected int totalContam;
	private List<Vehicle> vehiculos; 
	
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,int contLimit, int length, Weather weather) throws EventExecutionException {
		super(id);
		if (maxSpeed > 0 && contLimit > 0 && length > 0 && 
				srcJunc != null && destJunc != null && weather != null) {
			cruceOrig = srcJunc;
			cruceDest = destJunc;
			longitud = length;
			maxVel = maxSpeed;
			actVelLimit = maxVel;
			this.contLimit = contLimit;
			this.weather = weather;
			totalContam = 0;
			vehiculos = new SortedArrayList<Vehicle>();
			cruceOrig.addOutGoingRoad(this);
			cruceDest.addIncommingRoad(this);
		}
		else throw new EventExecutionException("Argumentos invalidos al crear la carretera");
	}
	
	
	void enter(Vehicle v) {
		if(v.getActVel() == 0 && v.getLocalizacion() == 0) 
			vehiculos.add(v);
		else 
			throw new IllegalArgumentException("La velocidad o localizacion del vehiculo son distintas de 0");
	}
	void exit(Vehicle v) {
		vehiculos.remove(v);
	}
	void setWeather(Weather w) {
		if(w != null) 
			weather = w;
		else
			throw new IllegalArgumentException("El valor de Weather no puede ser null");
	
	}
	void addContamination(int c) {
		if (c>= 0)
			totalContam += c;
		else
			throw new IllegalArgumentException("El valor de la contaminacion no puede se negativo");
	}
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v: vehiculos) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		Collections.sort(vehiculos);
	}
	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		obj.put("id", _id);
		obj.put("speedlimit", actVelLimit);
		obj.put("weather", weather.toString());
		obj.put("co2", totalContam);
		JSONArray jarray = new JSONArray();
		for (Vehicle v : vehiculos) {
			jarray.put(v.toString());
		}
		obj.put("vehicles", jarray);
		return obj;
	}

	public Junction getCruceDest() {
		return cruceDest;
	}

	public Junction getCruceOrig() {
		return cruceOrig;
	}

	public int getLongitud() {
		return longitud;
	}

	public int getMaxVel() {
		return maxVel;
	}

	public int getActVelLimit() {
		return actVelLimit;
	}

	public int getContLimit() {
		return contLimit;
	}

	public Weather getWeather() {
		return weather;
	}

	public int getTotalContam() {
		return totalContam;
	}

	public List<Vehicle> getVehiculos() {
		return Collections.unmodifiableList(vehiculos);
	}	
}
