package simulator.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

@SuppressWarnings("serial")
public class ChangeWeatherDialog extends ChangeDialog<Road, Weather> {
	
	public ChangeWeatherDialog(Controller ctr, RoadMap roadMap, MainWindow mw) {
		super(ctr, roadMap, mw);
		initGUI();
	}

	private void initGUI() {
		
		info.setText("<html>Cambia el tiempo atmosferico de la carretera seleccionada despues de que transcurran los ticks indicados desde el momento actual.<html>");
		
		label1.setText("Carretera: ");
		comboBox1.setToolTipText("Carreteras disponibles");
		label2.setText("Weather: ");
		comboBox2.setToolTipText("Tiempos meteorologicos disponibles");
		Weather[] weathers = Weather.values();
		for(int i = 0; i < weathers.length; i++) {
			comboBox2.addItem(weathers[i]);
		}
		
		Dimension aunMax = new Dimension(75, 20);
		comboBox2.setMaximumSize(aunMax);
		comboBox2.setPreferredSize(aunMax);
		
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Pair<String, Weather>> listaPares = new ArrayList<>();
				listaPares.add(new Pair<String, Weather>((String)comboBox1.getSelectedItem().toString(), Weather.valueOf(comboBox2.getSelectedItem().toString())));
				controller.addEvent(new SetWeatherEvent((int)ticksSpinner.getValue() + time, listaPares));
				setVisible(false);
			}
		});
		
		this.setTitle("Cambiar Tiempo Carretera");
	}
	
	public void showDialog(int time) {	
		this.setLocationRelativeTo(this.mw);
		this.time = time;
		List<Road> lista = roadMap.getRoads();
		if(lista.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No existen carretera en la simulacion", "No hay carretera", JOptionPane.WARNING_MESSAGE);;
		}
		else {
			if(comboBox2.getItemCount() != 0)
				comboBox2.setSelectedIndex(0);
			ticksSpinner.setValue(1);
			comboBox1.removeAllItems();
			for(Road r : lista) {
				comboBox1.addItem(r);
			}
			this.setVisible(true);
		}
	}
	
}
