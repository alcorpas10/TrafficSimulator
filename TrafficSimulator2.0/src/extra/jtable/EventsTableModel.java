package extra.jtable;

import java.util.List;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;

public class EventsTableModel extends GenericTableModel<Event> {

	private static final long serialVersionUID = 1L;
	

	private static String[] _colNames = { "Tiempo", "Descripcion" };

	public EventsTableModel(Controller _ctrl) {
		super(_ctrl, _colNames);
	}
	
	

	@Override
	// método obligatorio
	// así es como se va a cargar la tabla desde el ArrayList
	// el índice del arrayList es el número de fila pq en este ejemplo
	// quiero enumerarlos.
	//
	// returns the value of a particular cell 
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _lista.get(rowIndex).getTime();
			break;
		case 1:
			s = _lista.get(rowIndex).toString();
			break;
		}
		return s;
	}


	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setList(events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setList(events);
	}
	
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setList(events);
	}
}
