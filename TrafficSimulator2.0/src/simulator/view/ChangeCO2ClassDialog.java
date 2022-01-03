package simulator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class ChangeCO2ClassDialog extends ChangeDialog<Vehicle, Integer> {

	public ChangeCO2ClassDialog(Controller ctr, RoadMap roadMap, MainWindow mw) {
		super(ctr, roadMap, mw);
		initGUI();
	}

	private void initGUI() {
		
		info.setText("<html>Cambia la clase del vehiculo seleccionado despues de que transcurran los ticks indicados desde el momento actual.<html>");
		
		label1.setText("Vehiculo: ");
		comboBox1.setToolTipText("Vehiculos disponibles");
		label2.setText("Clase C02: ");
		Integer[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		for(int i = 0; i < array.length; i++) {
			comboBox2.addItem(array[i]);
		}
		comboBox2.setToolTipText("Clases contaminacion disponibles");
		
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Pair<String, Integer>> listaPares = new ArrayList<>();
				listaPares.clear();
				listaPares.add(new Pair<String, Integer>((String)comboBox1.getSelectedItem().toString(), (int)comboBox2.getSelectedItem()));
				controller.addEvent(new NewSetContClassEvent((int)ticksSpinner.getValue() + time, listaPares));
				setVisible(false);
			}
		});
		
		this.setTitle("Cambiar Clase CO2");
	}
	
	public void showDialog(int time) {
		this.setLocationRelativeTo(this.mw);
		this.time = time;
		List<Vehicle> lista = roadMap.getVehicles();
		if(lista.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No existen vehiculos en la simulacion", "No hay vehiculos", JOptionPane.WARNING_MESSAGE);
		}
		else {	
			if(comboBox2.getItemCount() != 0)
				comboBox2.setSelectedIndex(0);
			ticksSpinner.setValue(1);
			comboBox1.removeAllItems();
			for(Vehicle v : lista) {
				comboBox1.addItem(v);
			}
			this.setVisible(true);
		}
	}
	
}
