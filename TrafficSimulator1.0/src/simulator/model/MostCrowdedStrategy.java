package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int timeslot;
	public MostCrowdedStrategy(int timeslot) {
		this.timeslot = timeslot;
	}

	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.size() > 0) {
			int semVerde = 0, maxLong = 0, longi;
			if (currGreen != -1) {
				if (currTime - lastSwitchingTime >= timeslot) {
					int i = (currGreen + 1) % roads.size();
					do {
						longi = qs.get(i).size();
						if (longi > maxLong) {
							semVerde = i;
							maxLong = longi;
						}
						i = (i + 1) % roads.size();
					} while (i != (currGreen + 1) % roads.size()); //TODO Mirar la implementacion con for, creo que sobra algun (currGreen + 1) % roads.size())
					return semVerde;
				}
				else return currGreen;
			}
			else {
				for (int i = 0; i < qs.size(); i++) {
					longi = qs.get(i).size();
					if (longi > maxLong) {
						semVerde = i;
						maxLong = longi;
					}
				}
				return semVerde;
			}
		}
		else return -1;
	}

}
