package extra.jtable;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class VehiclesTableModel extends GenericTableModel<Vehicle>{

	private static String[] _colNames = { "Id", "Localizacion", "Itinerario", "Clase CO2", "Max. Velocidad", "Velocidad", "CO2 Total", "Distancia" };
	
	public VehiclesTableModel(Controller _ctrl) {
		super(_ctrl, _colNames);
	}
	

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _lista.get(rowIndex).getId();
			break;
		case 1:
			s = _lista.get(rowIndex).getLocalizacion();
			break;
		case 2:
			s = _lista.get(rowIndex).getItinerario();
			break;
		case 3:
			s = _lista.get(rowIndex).getClassContam();
			break;
		case 4:
			s = _lista.get(rowIndex).getMaxVel();
			break;
		case 5:
			s = _lista.get(rowIndex).getActVel();
			break;
		case 6:
			s = _lista.get(rowIndex).getTotalContam();
			break;
		case 7:
			s = _lista.get(rowIndex).getTotalDist();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setList(map.getVehicles());
	}
}
