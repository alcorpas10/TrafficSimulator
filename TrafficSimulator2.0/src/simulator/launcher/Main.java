package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.exceptions.ControllerException;
import simulator.exceptions.EventExecutionException;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static String _modeDefaultValue = "gui";
	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static int ticks = _timeLimitDefaultValue;
	private static String mode = _modeDefaultValue;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTimeOption(line);
			
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Number of ticks that will be run.").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Type of interface that will be displayed.").build());
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (!mode.equalsIgnoreCase("gui"))
			if (_inFile == null)
				throw new ParseException("An events file is missing");
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTimeOption(CommandLine line) throws ParseException {
		if (line.hasOption("t")) ticks = Integer.parseInt(line.getOptionValue("t"));
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		if (line.hasOption("m")) {
			String s = line.getOptionValue("m").toLowerCase();
			if (s.equalsIgnoreCase("gui") || s.equalsIgnoreCase("console"))
				mode = s;
			else
				throw new ParseException("Parametro de 'modo' no valido. Se esperaba 'gui' o 'console'. Encontrado -> " + s);
		}
	}

	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> builders = new ArrayList<>();
		builders.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
		builders.add(new NewCityRoadEventBuilder());
		builders.add(new NewInterCityRoadEventBuilder());
		builders.add(new NewVehicleEventBuilder());
		builders.add(new SetWeatherEventBuilder());
		builders.add(new SetContClassEventBuilder());
		_eventsFactory = new BuilderBasedFactory<>(builders);
	}

	private static void startBatchMode() throws IOException {
		TrafficSimulator sim = new TrafficSimulator();
		try {
			Controller controller = new Controller(sim, _eventsFactory);
			controller.loadEvents(new FileInputStream(new File(_inFile)));
			OutputStream out = null;
			if(_outFile != null) {
				out = new FileOutputStream(new File(_outFile));
			}
			controller.run(ticks, out);
			System.out.println("La simulacion se ha ejecutado correctamente");
		} catch(ControllerException | IllegalArgumentException | EventExecutionException e) {
			System.err.format(e + ". Se ha detenido la simulacion\n");
		}
	}
	
	private static void startGUIMode() throws IOException {
		TrafficSimulator sim = new TrafficSimulator();
		Controller ctrl;
		try {
			ctrl = new Controller(sim, _eventsFactory);		
			if (_inFile != null) 
				ctrl.loadEvents(new FileInputStream(new File(_inFile)));
			
			SwingUtilities.invokeLater( new Runnable() {
				@ Override
				public void run() {
					new MainWindow(ctrl);
				}
			});			
		
		} catch (ControllerException | IllegalArgumentException e) {
			System.err.format(e + ". Se ha detenido la simulacion\n");
		}

		
		
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if (mode.equalsIgnoreCase("gui"))
			startGUIMode();
		else
			startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			System.err.format("Error critico, se ha detenido la simulacion\n" + e + '\n');
		}

	}

}
