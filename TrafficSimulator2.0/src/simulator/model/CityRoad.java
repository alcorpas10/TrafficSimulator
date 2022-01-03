package simulator.model;

import simulator.exceptions.EventExecutionException;

public class CityRoad extends Road{

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws EventExecutionException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 2;
		if(weather == Weather.WINDY || weather == Weather.STORM)
			x = 10;
		
		totalContam = Math.max(0, totalContam - x); //totalContam no puede ser negativo
	}
	@Override
	void updateSpeedLimit() {
		
	}
	@Override
	int calculateVehicleSpeed(Vehicle v) {
		//return (int) Math.ceil(((11.0 - v.getGradContam()) / 11.0) * actVelLimit);
		return (int) (((11.0 - v.getClassContam()) / 11.0) * actVelLimit);
	}
}
