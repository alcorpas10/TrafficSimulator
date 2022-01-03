package simulator.model;

import simulator.exceptions.EventExecutionException;

public class InterCityRoad extends Road {

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws EventExecutionException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		switch (weather) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
			break;
		case WINDY:
			x = 15;
			break;
		case STORM:
			x = 20;
			break;
		}
		totalContam =  (int) (((100.0-x)/100.0)*totalContam);
		
	}
	@Override
	void updateSpeedLimit() {
		if(totalContam > contLimit)
			actVelLimit = (int) (maxVel*0.5);
		else
			actVelLimit = maxVel;
	}
	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(weather == Weather.STORM)
			return (int) (actVelLimit*0.8);
		else
			return actVelLimit;
	}
}
