import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationApplicationGUI extends Application {

    // TODO let user control these
    private final Int _diameter = new Int(108); //mm
    private final Int _speed = new Int(2); //mph
    private final Int _numLasers = new Int(9);
    private final Int _spacing = new Int(35);
    private final Int _numEdges = new Int(32);

    private final Int _whole = new Int(100);
    private final Int _broken = new Int(100);

    private static class Int{
        int _value;
        public Int(int v){
            _value = v;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Breakage Monitoring Simulation");

        final ListView<String> generatorsList = new ListView<>(getSortedObservableList(masterGeneratorList()));
        final ListView<String> collectorsList = new ListView<>(getSortedObservableList(masterDataCollectorList()));
        final ListView<String> analyzersList = new ListView<>(getSortedObservableList(masterDataAnalyzerList()));

        final HBox rootPanel = new HBox();

        //#region Canvas Setup
        double canvasWidth = 500;
        double canvasHeight = 400;

        final VBox imagePanel = new VBox();

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Button imageButton = new Button("Visualize Configuration"){
            public void fire () {
                drawVisuals(gc, canvasWidth, canvasHeight, generatorsList, collectorsList);
                    /* Add display items that this has to modify here */
            }
        };

        //#endregion

        final VBox optionsPanel = new VBox();

        HBox settingsRow1 = new HBox();
        HBox settingsRow2 = new HBox();
        HBox settingsRow3 = new HBox();



        VBox diameterPanel = createVariableSetterPanel("Diameter", _diameter);
        VBox speedPanel = createVariableSetterPanel("Speed (MPH)", _speed);
        VBox numLasersPanel = createVariableSetterPanel("Num Lasers", _numLasers);
        VBox spacingPanel = createVariableSetterPanel("Laser Spacing", _spacing);
        VBox numEdgesPanel = createVariableSetterPanel("Num Edges", _numEdges);
        VBox wholePanel = createVariableSetterPanel("Whole Target Samples", _whole);
        VBox brokenPanel = createVariableSetterPanel("Broken Target Samples", _broken);

        /*
        Label diameterLabel = new Label("Diameter = " + _diameter);
        TextField diameterTf = new TextField(Integer.toString(_diameter));
        diameterPanel.getChildren().addAll(diameterLabel, diameterTf);
        EventHandler<ActionEvent> diameterEvent = e -> {
            try{
            _diameter = Integer.parseInt(diameterTf.getText());
            }
            catch(NumberFormatException ex) {
                // do nothing
            }
            diameterLabel.setText("Diameter = " + _diameter);
            System.out.println(_diameter);
        };
        diameterTf.setOnAction(diameterEvent);*/

        settingsRow1.getChildren().addAll(diameterPanel, speedPanel, numEdgesPanel);
        settingsRow2.getChildren().addAll(numLasersPanel, spacingPanel);
        settingsRow3.getChildren().addAll(wholePanel, brokenPanel);

            //#region ListView selectors setup
            final HBox selectionPanel = new HBox();
            selectionPanel.setPadding(new Insets(15, 12, 15, 12));
            selectionPanel.setSpacing(10);

                VBox generatorPanel = new VBox();
                generatorPanel.getChildren().addAll(new Label("Target Generator"), generatorsList); // TODO add generators
                VBox collectorPanel = new VBox();
                collectorPanel.getChildren().addAll(new Label("Data Collector"), collectorsList); // TODO add generators
                VBox analyzerPanel = new VBox();
                analyzerPanel.getChildren().addAll(new Label("Data Analyzer"), analyzersList); // TODO add generators

            selectionPanel.getChildren().addAll(generatorPanel, collectorPanel, analyzerPanel);
            //#endregion

            //#region Run button setup
            final VBox buttonPanel = new VBox();
            buttonPanel.setPadding(new Insets(15, 12, 15, 12));
            buttonPanel.setSpacing(10);
            final Label resultsLabel = new Label();
            final Button searchButton = new Button("Run simulation") {
                public void fire () {
                    runSimulation(generatorsList, collectorsList, analyzersList, resultsLabel
                            /* Add display items that this has to modify here */);
                }
            };
            //#endregion

        buttonPanel.getChildren().addAll(searchButton, resultsLabel);
        optionsPanel.getChildren().addAll(settingsRow1, settingsRow2, settingsRow3, selectionPanel, buttonPanel);
        imagePanel.getChildren().addAll(imageButton, canvas);
        rootPanel.getChildren().addAll(optionsPanel, imagePanel);
            // TODO allow export results to a file
        primaryStage.setScene(new Scene(rootPanel, 1000, 500));
        primaryStage.show();
    }

    private VBox createVariableSetterPanel(String text, Int var){
        VBox panel = new VBox();
        Label label = new Label(text + " = " + var._value);
        TextField tf = new TextField(Integer.toString(var._value));
        panel.getChildren().addAll(label, tf);
        EventHandler<ActionEvent> event = e -> {
            try{
                var._value = Integer.parseInt(tf.getText());
            }
            catch(NumberFormatException ex) {
                // do nothing
            }
            label.setText(text + " = " + var._value);
            System.out.println(var._value);
        };
        tf.setOnAction(event);

        return panel;
    }

    private void runSimulation(ListView<String> generators, ListView<String> collectors, ListView<String> analyzers, Label resultsLabel){
        // TODO Check that all info is valid
        if(generators.getSelectionModel().isEmpty() ||
                collectors.getSelectionModel().isEmpty() ||
                analyzers.getSelectionModel().isEmpty()){
            return;
        }

        TargetGenerator generator = masterGeneratorList().get(generators.getSelectionModel().getSelectedIndex());
        LaserDataCollector collector = masterDataCollectorList().get(collectors.getSelectionModel().getSelectedIndex());
        LaserDataAnalyzer analyzer = masterDataAnalyzerList().get(analyzers.getSelectionModel().getSelectedIndex());
        BreakageClassifier classifier = new LaserBreakageClassifier(collector, analyzer);
        BreakageClassifierTester tester = new BreakageClassifierTester(classifier, generator);

        TestStatistics stats = tester.test(_whole._value, _broken._value);
        resultsLabel.setText(stats.toString());
    }

    /**
     * Converts from a List of Listables to a List of String objects.
     */
    private static List<String> getNames (List<? extends Listable> items) {
        return items.stream().map(Listable::getName).collect(Collectors.toList());
    }

    /**
     * Converts from a Collection to an ObservableList of Node objects.
     * @param items the collection of Node objects to convert
     * @return the ObservableList of String objects converted from the collection.
     */
    private static ObservableList<String> getSortedObservableList (List<? extends Listable> items) {
        return FXCollections.observableList(getNames(items));
    }

    private List<TargetGenerator> masterGeneratorList(){
        List<TargetGenerator> generators = new ArrayList<TargetGenerator>();

        TargetGenerator generator0 = new RandomQuadrantGenerator(_numEdges._value, _diameter._value, _speed._value);
        generators.add(generator0);

        TargetGenerator generator1 = new ManySmallPiecesGenerator(_numEdges._value, _diameter._value, _speed._value);
        generators.add(generator1);

        // Add more options here

        return generators;
    }

    private List<LaserDataCollector> masterDataCollectorList(){
        List<LaserDataCollector> collectors = new ArrayList<>();

        LaserDataCollector collector0 = new InterruptLaserDataCollector(_numLasers._value, _spacing._value);
        collectors.add(collector0);

        LaserDataCollector collector1 = new PollingLaserDataCollector(_numLasers._value, _spacing._value);
        collectors.add(collector1);

        // Add more options here

        return collectors;
    }

    private List<LaserDataAnalyzer> masterDataAnalyzerList(){
        List<LaserDataAnalyzer> analyzers = new ArrayList<>();

        LaserDataAnalyzer analyzer0 = new BasicLaserDataAnalyzer(_diameter._value, _spacing._value);
        analyzers.add(analyzer0);

        // Add more options here

        return analyzers;
    }

    private void drawVisuals(GraphicsContext g, double width, double height, ListView<String> generators, ListView<String> collectors){
        g.clearRect(0,0,width,height);

        if(generators.getSelectionModel().isEmpty() ||
                collectors.getSelectionModel().isEmpty()){
            return;
        }

        TargetGenerator generator = masterGeneratorList().get(generators.getSelectionModel().getSelectedIndex());
        LaserDataCollector collector = masterDataCollectorList().get(collectors.getSelectionModel().getSelectedIndex());

        Target target = generator.getBrokenTarget((int) (width/2), (int) (height/2));
        int[][] lasers = collector.getLasers();
        paintTarget(target, g);
        paintLasers(lasers, width, height, g);
    }

    private void paintTarget(Target t, GraphicsContext g){
        g.setFill(Color.ORANGE);

        for(Piece p : t.getPieces()){
            int[] xs = p.getXs();
            int[] ys = p.getYs();
            double[] dxs = new double[xs.length];
            double[] dys = new double[ys.length];
            for(int i = 0; i < xs.length; i++){
                dxs[i] = (double) xs[i];
                dys[i] = (double) ys[i];
            }
            g.fillPolygon(dxs, dys, p.getXs().length);
        }
    }

    private void paintLasers(int[][] lasers, double width, double height, GraphicsContext g){
        g.setFill(Color.RED);
        double xOff = (width/2);
        double yOff = height/2 + height/5;
        for(int[] laser : lasers){
            double r = 6;
            g.fillOval(laser[0] - r/2 + xOff, laser[1] - r/2 + yOff, r, r);
        }
    }
}
