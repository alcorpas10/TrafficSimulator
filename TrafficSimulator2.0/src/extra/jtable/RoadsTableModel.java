package extra.jtable;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;

@SuppressWarnings("serial")
public class RoadsTableModel extends GenericTableModel<Road>{


	private static String[] _colNames = { "Id", "Longitud", "Tiempo Atmosferico", "Max. Velocidad", "Limite Velocidad", "CO2 Total", "CO2 Limit" };
	
	public RoadsTableModel(Controller _ctrl) {
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
			s = _lista.get(rowIndex).getLongitud();
			break;
		case 2:
			s = _lista.get(rowIndex).getWeather();
			break;
		case 3:
			s = _lista.get(rowIndex).getMaxVel();
			break;
		case 4:
			s = _lista.get(rowIndex).getActVelLimit();
			break;
		case 5:
			s = _lista.get(rowIndex).getTotalContam();
			break;
		case 6:
			s = _lista.get(rowIndex).getContLimit();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setList(map.getRoads());
	}
}
