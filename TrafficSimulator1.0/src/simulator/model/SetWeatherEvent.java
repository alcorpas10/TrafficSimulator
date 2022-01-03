package simulator.model;

import java.util.List;

import simulator.exceptions.NotCriticalException;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {

	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws != null) {
			this.ws = ws;
		}
		else {
			throw new IllegalArgumentException("La lista metereologica esta vacia");
		}
	}

	@Override
	void execute(RoadMap map) throws NotCriticalException {
		for(Pair<String,Weather> par: ws) {
			String roadString = par.getFirst();
			Weather tiempo = par.getSecond();
			Road road = map.getRoad(roadString);
			
			if(road != null) road.setWeather(tiempo);
			else throw new NotCriticalException("No existe la carretera " + roadString, "SetWeatherEvent");
		}

	}

}
