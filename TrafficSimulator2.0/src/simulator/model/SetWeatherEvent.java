package simulator.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
			
			if(road != null) 
				road.setWeather(tiempo);
			else 
				throw new NotCriticalException("No existe la carretera " + roadString, "SetWeatherEvent");
		}
	}

	@Override
	public String toString() {
		String texto = "";
		for (Pair<String, Weather> par : ws) {
			if (!texto.equals("")) 
				texto += ", ";
			texto += "(" + par.getFirst() + ", " + par.getSecond() + ")";
		}
		return "Cambio Tiempo: [" + texto + "]";
	}
	
	@Override
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray info = new JSONArray();
		JSONObject infoObject;
		for(Pair<String,Weather> par: ws) {
			infoObject = new JSONObject();
			infoObject.put("road", par.getFirst());
			infoObject.put("weather", par.getSecond().toString());
			info.put(infoObject);
		}
		data.put("time", this._time);
		data.put("info", info);
		obj.put("type", "set_weather");
		obj.put("data", data);
		return obj;
	}
}
