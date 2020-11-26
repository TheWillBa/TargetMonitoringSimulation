import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationApplicationGUI extends Application {

    // TODO add ability to test over a range of a single dependant var and export to excel and display graph in new window
    TesterMaster _master = new TesterMaster();


    // These are class vars because they're so much easier to deal with
    final Label _trueWholeLabel = new Label("True Whole: ???");
    final Label _trueBrokenLabel = new Label("True Broken: ???");
    final Label _falseWholeLabel = new Label("False Whole: ???");
    final Label _falseBrokenLabel = new Label("False Broken: ???");
    final Label _totalWholeLabel = new Label("Total Whole: ???");
    final Label _totalBrokenLabel = new Label("Total Whole: ???");

    final Label _accuracyLabel = new Label("Accuracy: ???");
    final Tooltip aTT = new Tooltip("Fraction of total correct classifications");
                //accuracyLabel.setTooltip(aTT);
    final Label _precisionLabel = new Label("Precision: ???");
    final Label _recallLabel = new Label("Recall: ???");
    final Tooltip rTT = new Tooltip("Fraction of whole targets correctly classified");
                //recallLabel.setTooltip(rTT);
    final Label _specificityLabel = new Label("Specificity: ???");
    final Tooltip sTT = new Tooltip("Fraction of broken targets correctly classified");
                //specificityLabel.setTooltip(sTT);
    final Label _fBetaLabel = new Label("F-beta Score: ???");

    //#region Get list views
    final ListView<String> generatorsList = new ListView<>(getSortedObservableList(_master.masterGeneratorList()));
    final ListView<String> collectorsList = new ListView<>(getSortedObservableList(_master.masterDataCollectorList()));
    final ListView<String> analyzersList = new ListView<>(getSortedObservableList(_master.masterDataAnalyzerList()));
    //#endregion



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Breakage Monitoring Simulation");


        final HBox rootPanel = new HBox();

        //#region Canvas Setup
        double canvasWidth = 500;
        double canvasHeight = 400;

        final VBox imagePanel = new VBox();
        imagePanel.setPadding(new Insets(15, 12, 15, 12));
        imagePanel.setSpacing(5);

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        Button imageButton = new Button("Visualize Configuration"){
            public void fire () {
                drawVisuals(gc, canvasWidth, canvasHeight);
                    /* Add display items that this has to modify here */
            }
        };

        //#endregion

        final VBox optionsPanel = new VBox();

        //#region Variable Config Settings

        HBox settingsRow1 = new HBox();
        settingsRow1.setPadding(new Insets(15, 0, 0, 12));
        HBox settingsRow2 = new HBox();
        settingsRow2.setPadding(new Insets(0, 0, 0, 12));
        HBox settingsRow3 = new HBox();
        settingsRow3.setPadding(new Insets(0, 0, 0, 12));

        VBox diameterPanel = createVariableSetterPanel("Diameter (mm)", _master.getDiameterObj());
        VBox speedPanel = createVariableSetterPanel("Speed (MPH)", _master.getSpeedObj());
        VBox numLasersPanel = createVariableSetterPanel("Num Lasers", _master.getNumLasersObj());
        VBox spacingPanel = createVariableSetterPanel("Laser Spacing (mm)", _master.getSpacingObj());
        VBox numEdgesPanel = createVariableSetterPanel("Num Edges", _master.getNumEdgesObj());
        VBox wholePanel = createVariableSetterPanel("Whole Targets", _master.getWholeObj());
        VBox brokenPanel = createVariableSetterPanel("Broken Targets", _master.getBrokenObj());

        settingsRow1.getChildren().addAll(diameterPanel, speedPanel, numEdgesPanel);
        settingsRow2.getChildren().addAll(numLasersPanel, spacingPanel);
        settingsRow3.getChildren().addAll(wholePanel, brokenPanel);

        //#endregion

            //#region ListView selectors setup
        final Label currentGeneratorLabel = new Label("Current = " + _master.getTargetGenerator().getName());
        final Label currentCollectorLabel = new Label("Current = " + _master.getLaserDataCollector().getName());
        final Label currentAnalyzerLabel = new Label("Current = " + _master.getLaserDataAnalyzer().getName());

        generatorsList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                _master.get_generatorIndex()._value = newValue.intValue();
                currentGeneratorLabel.setText("Current = " + _master.getTargetGenerator().getName());
            }
        });

        collectorsList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                _master.get_collectorIndex()._value = newValue.intValue();
                currentCollectorLabel.setText("Current = " + _master.getLaserDataCollector().getName());
            }
        });

        analyzersList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                _master.get_analyzerIndex()._value = newValue.intValue();
                currentAnalyzerLabel.setText("Current = " + _master.getLaserDataAnalyzer().getName());
            }
        });


            final HBox selectionPanel = new HBox();
            selectionPanel.setPadding(new Insets(15, 12, 15, 12));
            selectionPanel.setSpacing(10);

                VBox generatorPanel = new VBox();
                generatorPanel.getChildren().addAll(new Label("Target Generator"), currentGeneratorLabel ,generatorsList); // TODO add generators
                VBox collectorPanel = new VBox();
                collectorPanel.getChildren().addAll(new Label("Data Collector"), currentCollectorLabel, collectorsList); // TODO add generators
                VBox analyzerPanel = new VBox();
                analyzerPanel.getChildren().addAll(new Label("Data Analyzer"), currentAnalyzerLabel, analyzersList); // TODO add generators

            selectionPanel.getChildren().addAll(generatorPanel, collectorPanel, analyzerPanel);
            //#endregion

            //#region Run button setup
            final HBox buttonPanel = new HBox();
            buttonPanel.setPadding(new Insets(10, 0, 0, 0));
            buttonPanel.setSpacing(5);
            final Label resultsLabel = new Label();

            final VBox resultPanel = new VBox();
            resultPanel.setPadding(new Insets(0, 0,15,12));
            resultPanel.setSpacing(5);


            resultPanel.getChildren().addAll(_trueWholeLabel, _trueBrokenLabel, _falseWholeLabel, _falseBrokenLabel, _totalWholeLabel, _totalBrokenLabel);

            final VBox statsPanel = new VBox();
            statsPanel.setPadding(new Insets(0, 0,15,12));
            statsPanel.setSpacing(5);



            statsPanel.getChildren().addAll(_accuracyLabel, _precisionLabel, _recallLabel, _specificityLabel, _fBetaLabel);

            final HBox allResultsPanel = new HBox();
            allResultsPanel.getChildren().addAll(resultPanel, statsPanel);


        final Button searchButton = new Button("Run Simulation") {
            public void fire () {
                TestStatistics stats;
                try {
                    stats = _master.runSimulation();

                    //#region Gross display nonsense
                    _trueWholeLabel.setText("True Whole: " + stats.trueWhole());
                    _falseWholeLabel.setText("False Whole: " + stats.falseWhole());
                    _trueBrokenLabel.setText("True Broken: " + stats.trueBroken());
                    _falseBrokenLabel.setText("False Broken: " + stats.falseBroken());
                    _totalWholeLabel.setText("Total Whole: " + stats.numWholeSamples());
                    _totalBrokenLabel.setText("Total Broken: " + stats.numBrokenSamples());
                    _accuracyLabel.setText("Accuracy: " + stats.accuracy());
                    _precisionLabel.setText("Precision: " + Math.floor(stats.precision() * 1000) / 1000.0);
                    _recallLabel.setText("Recall: " + stats.recall());
                    _specificityLabel.setText("Specificity: " + stats.specificity());
                    //fbs.setText("F-beta Score: " + stats.fBetaScore(/* beta */));
                    //#endregion
                } catch (IllegalArgumentException e) {
                    // Do nothing
                }


            }
        };
            //#endregion

        buttonPanel.getChildren().addAll(imageButton, searchButton, resultsLabel);
        optionsPanel.getChildren().addAll(settingsRow1, settingsRow2, settingsRow3, selectionPanel, allResultsPanel);
        imagePanel.getChildren().addAll(buttonPanel, canvas);
        rootPanel.getChildren().addAll(optionsPanel, imagePanel);
            // TODO allow export results to a file (make data exporter a separate class)

        //#region Testing Over a Single Variable
        Button button = new Button();
        button.setText("Test Over Single Variable");

        buttonPanel.getChildren().addAll(button);

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Label secondLabel = new Label("Coming soon");

                VBox secondaryLayout = new VBox();


                Scene secondScene = new Scene(secondaryLayout, 400, 150);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Test Single Variable");
                newWindow.setScene(secondScene);

                // Specifies the modality for new window.
                newWindow.initModality(Modality.APPLICATION_MODAL);

                // Specifies the owner Window (parent) for new window
                newWindow.initOwner(primaryStage);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);


                final Int[] selectedVar = new Int[1];
                final String[] selectedVarText = new String[1];

                Button diameterButton = new Button("Diameter"){
                    @Override
                    public void fire() {
                        selectedVar[0] = _master.getDiameterObj();
                        selectedVarText[0] = "Diameter";
                    }
                };
                Button speedButton  = new Button("Speed"){
                    @Override
                    public void fire() {
                        selectedVar[0] = _master.getSpeedObj();
                        selectedVarText[0] = "Speed";
                    }
                };

                Button numEdgesButton = new Button("NumEdges"){
                    @Override
                    public void fire() {
                        selectedVar[0] = _master.getDiameterObj();
                        selectedVarText[0] = "NumEdges";
                    }
                };

                Int min = new Int(0);
                Int max = new Int(10);

                HBox selectorButtons = new HBox();
                HBox minMaxButtons = new HBox();
                selectorButtons.getChildren().addAll(diameterButton, speedButton, numEdgesButton);
                selectorButtons.setPadding(new Insets(15,0,0,0));
                minMaxButtons.getChildren().addAll(createVariableSetterPanel("Min", min), createVariableSetterPanel("Max", max));
                secondaryLayout.getChildren().addAll(selectorButtons, minMaxButtons);
                selectorButtons.setAlignment(Pos.CENTER);
                minMaxButtons.setAlignment(Pos.CENTER);

                HBox runPanel = new HBox();

                Button runOverVarTestButton = new Button("Run Simulations"){
                    @Override
                    public void fire() {
                        try{
                            List<TestStatistics> tests = _master.runSimulationOverVariable(min._value, max._value, 1, selectedVar[0]);
                            displayDataGraph(min._value, max._value, tests, selectedVarText[0], newWindow);
                        }catch(NullPointerException e){
                            // DO nothing // TODO display reuseable pop up input error
                        }

                    }
                };
                runPanel.getChildren().addAll(runOverVarTestButton);
                runPanel.setAlignment(Pos.CENTER);

                secondaryLayout.getChildren().addAll(runPanel);



                newWindow.show();
            }
        });

        //#endregion

        primaryStage.setScene(new Scene(rootPanel, 1000, 500));
        primaryStage.show();
    }


    private static void displayDataGraph(int depMin, int depMax, List<TestStatistics> tests, String text, Stage parentStage){
        HBox secondaryLayout = new HBox();
        Scene secondScene = new Scene(secondaryLayout, 500, 500);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Data Graph");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.APPLICATION_MODAL);

        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(parentStage);

        // Chart Business
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(text);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Test Statistics");
        final LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);

        XYChart.Series<Number, Number> accuracySeries = new XYChart.Series<>();
        accuracySeries.setName("Accuracy");
        XYChart.Series<Number, Number> recallSeries = new XYChart.Series<>();
        recallSeries.setName("Recall");
        XYChart.Series<Number, Number> specificitySeries = new XYChart.Series<>();
        specificitySeries.setName("Specificity");

        for(int i = depMin; i <= depMax; i++){
            TestStatistics stats = tests.get(i - depMin);
            accuracySeries.getData().add(new XYChart.Data<>(i, stats.accuracy()));
            recallSeries.getData().add(new XYChart.Data<>(i, stats.recall()));
            specificitySeries.getData().add(new XYChart.Data<>(i, stats.specificity()));
        } // TODO keep track of min/max for scale?

        chart.getData().addAll(accuracySeries, recallSeries, specificitySeries);
        secondaryLayout.getChildren().addAll(chart);
                // Set position of second window, related to primary window.
        newWindow.setX(parentStage.getX());
        newWindow.setY(parentStage.getY());


        newWindow.show();
    }

    private static VBox createVariableSetterPanel(String text, Int var){
        VBox panel = new VBox();
        panel.setPadding(new Insets(5, 4, 5, 0));
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

    private void drawVisuals(GraphicsContext g, double width, double height){

        g.setFill(Color.LIGHTYELLOW);
        g.fillRect(0,0,width,height);

        g.setFill(Color.BLACK);
        g.strokeRect(0,0,width,height);


        // TODO check here?

        TargetGenerator generator = _master.getTargetGenerator();
        LaserDataCollector collector = _master.getLaserDataCollector();

        Target target = generator.getBrokenTarget((int) (width/2), (int) (height/2));
        int[][] lasers = collector.getLasers();
        paintTarget(target, g);
        paintLasers(lasers, width, height, g);
    }

    private static void paintTarget(Target t, GraphicsContext g){
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

    private static void paintLasers(int[][] lasers, double width, double height, GraphicsContext g){
        g.setFill(Color.RED);
        double xOff = (width/2);
        double yOff = height/2 + height/5;
        for(int[] laser : lasers){
            double r = 6;
            g.fillOval(laser[0] - r/2 + xOff, laser[1] - r/2 + yOff, r, r);
        }
    }
}
