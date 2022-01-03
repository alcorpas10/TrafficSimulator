package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	public MoveFirstStrategy() {
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> lista = new ArrayList<>(1);
		if(!q.isEmpty())
			lista.add(q.get(0));
		return lista;
	}
}
