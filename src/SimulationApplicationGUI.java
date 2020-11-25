import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.bind.JAXBPermission;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationApplicationGUI extends Application {

    // TODO let user control these
    private int _diameter = 108; //mm
    private int _speed = 2; //mph
    private int _numLasers = 9;
    private int _spacing = 35;
    private int _numEdges = 32;

    private int _whole = 100;
    private int _broken = 100;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Breakage Monitoring Simulation");

        final ListView<String> generatorsList = new ListView<>(getSortedObservableList(masterGeneratorList()));
        final ListView<String> collectorsList = new ListView<>(getSortedObservableList(masterDataCollectorList()));
        final ListView<String> analyzersList = new ListView<>(getSortedObservableList(masterDataAnalyzerList()));

        final HBox rootPanel = new HBox();

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

        final VBox optionsPanel = new VBox();
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

        buttonPanel.getChildren().addAll(searchButton, resultsLabel);
        optionsPanel.getChildren().addAll(selectionPanel, buttonPanel);
        imagePanel.getChildren().addAll(imageButton, canvas);
        rootPanel.getChildren().addAll(optionsPanel, imagePanel);

        primaryStage.setScene(new Scene(rootPanel, 1000, 500));
        primaryStage.show();
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

        TestStatistics stats = tester.test(_whole, _broken);
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

        TargetGenerator generator0 = new RandomQuadrantGenerator(_numEdges, _diameter, _speed);
        generators.add(generator0);

        TargetGenerator generator1 = new ManySmallPiecesGenerator(_numEdges, _diameter, _speed);
        generators.add(generator1);

        // Add more options here

        return generators;
    }

    private List<LaserDataCollector> masterDataCollectorList(){
        List<LaserDataCollector> collectors = new ArrayList<>();

        LaserDataCollector collector0 = new InterruptLaserDataCollector(_numLasers, _spacing);
        collectors.add(collector0);

        LaserDataCollector collector1 = new PollingLaserDataCollector(_numLasers, _spacing);
        collectors.add(collector1);

        // Add more options here

        return collectors;
    }

    private List<LaserDataAnalyzer> masterDataAnalyzerList(){
        List<LaserDataAnalyzer> analyzers = new ArrayList<>();

        LaserDataAnalyzer analyzer0 = new BasicLaserDataAnalyzer(_diameter, _spacing);
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
