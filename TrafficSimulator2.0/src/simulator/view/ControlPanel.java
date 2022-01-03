package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	
	private MainWindow mainWindow;
	private JToolBar toolBar;
	private JButton carga, cambioCont, cambioAtm, play, stop, exit;
	private JSpinner ticks;
	
	public ControlPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.PAGE_START);
		
		carga = new JButton(new ImageIcon("resources/icons/open.png"));
		carga.setToolTipText("Cargar archivo");
		toolBar.add(carga);
		carga.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.fileChooserDialog();
			}
		});
		
		toolBar.addSeparator();	    
		
		cambioCont = new JButton(new ImageIcon("resources/icons/co2class.png"));
		cambioCont.setToolTipText("Cambiar contaminacion coche");
		toolBar.add(cambioCont);
		cambioCont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.co2ClassDialog();
			}
		});
		
		cambioAtm = new JButton(new ImageIcon("resources/icons/weather.png"));
		cambioAtm.setToolTipText("Cambiar clima carretera");
		toolBar.add(cambioAtm);
		cambioAtm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.weatherDialog();
			}
		});
		
		toolBar.addSeparator();
		
		JLabel texto = new JLabel(" Ticks:  ");
		ticks = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		ticks.setToolTipText("Spinner de ticks");
		ticks.setPreferredSize(new Dimension(50,ticks.getHeight()));
		ticks.setMaximumSize(new Dimension(50,50));
		
		play = new JButton(new ImageIcon("resources/icons/run.png"));
		play.setToolTipText("Comenzar simulacion");
		toolBar.add(play);
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.play((int) ticks.getValue());
			}
		});
		
		stop = new JButton(new ImageIcon("resources/icons/stop.png"));
		stop.setToolTipText("Parar simulacion");
		toolBar.add(stop);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.stop();
			}
		});
		
		toolBar.add(texto);
		toolBar.add(ticks);
		
		toolBar.add(Box.createHorizontalGlue());
		toolBar.addSeparator();
		
		exit = new JButton(new ImageIcon("resources/icons/exit.png"));
		exit.setToolTipText("Salir de la aplicacion");
		toolBar.add(exit);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(getParent().getParent(), "Desea salir del programa?", "Salir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(n == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		
	}
	
	public void enableToolBar(boolean b) {
		carga.setEnabled(b);
		cambioCont.setEnabled(b);
		cambioAtm.setEnabled(b);
		play.setEnabled(b);
	}

	public int getTicks() {
		toolBar.requestFocus();
		return (int) ticks.getValue();
	}
}
