package extra.jtable;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;

@SuppressWarnings("serial")
public class JunctionsTableModel extends GenericTableModel<Junction>{

	
	private static String[] _colNames = { "Id", "Green", "Queues"};
	
	public JunctionsTableModel(Controller _ctrl) {
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
			s = _lista.get(rowIndex).semaforoVerde();
			break;
		case 2:
			String colasAux = _lista.get(rowIndex).getMapaColas().toString().replace('=', ':');
			s = colasAux.substring(1, colasAux.length() - 1);
			break;
		}
		return s;
	}


	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setList(map.getJunctions());
	}
}
