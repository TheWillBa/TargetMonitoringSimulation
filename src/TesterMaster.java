import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class TesterMaster {

    // TODO move most of the logic from the GUI class to here so the SimulationApplicationGUI can just do GUI work

    // Default values
    private final Int _diameter = new Int(108); //mm
    private final Int _speed = new Int(2); //mph
    private final Int _numLasers = new Int(9);
    private final Int _spacing = new Int(35);
    private final Int _numEdges = new Int(32);

    private final Int _whole = new Int(100);
    private final Int _broken = new Int(100);

    private final Int _generatorIndex = new Int(0);
    private final Int _collectorIndex = new Int(0);
    private final Int _analyzerIndex = new Int(0);

    public TesterMaster(){
    }

    public List<TargetGenerator> masterGeneratorList(){
        List<TargetGenerator> generators = new ArrayList<TargetGenerator>();

        TargetGenerator generator0 = new RandomQuadrantGenerator(_numEdges._value, _diameter._value, _speed._value);
        generators.add(generator0);

        TargetGenerator generator1 = new ManySmallPiecesGenerator(_numEdges._value, _diameter._value, _speed._value);
        generators.add(generator1);

        // Add more options here

        return generators;
    }

    public List<LaserDataCollector> masterDataCollectorList(){
        List<LaserDataCollector> collectors = new ArrayList<>();

        LaserDataCollector collector0 = new InterruptLaserDataCollector(_numLasers._value, _spacing._value);
        collectors.add(collector0);

        LaserDataCollector collector1 = new PollingLaserDataCollector(_numLasers._value, _spacing._value);
        collectors.add(collector1);

        // Add more options here

        return collectors;
    }

    public List<LaserDataAnalyzer> masterDataAnalyzerList(){
        List<LaserDataAnalyzer> analyzers = new ArrayList<>();

        LaserDataAnalyzer analyzer0 = new BasicLaserDataAnalyzer(_diameter._value, _spacing._value);
        analyzers.add(analyzer0);

        // Add more options here

        return analyzers;
    }

    public List<TestStatistics> runSimulationOverVariable(int min, int max, int step, Int var){
        List<TestStatistics> tests = new ArrayList<>();
        int origVal = var._value;
        for(int i = min; i <= max; i++){
            var._value = i;
            TestStatistics stats = runSimulation();
            System.out.println(stats);
            tests.add(stats);
        }
        var._value = origVal;
        return tests;
    }
/*
    public List<TestStatistics> runSimulationOverDiameter(int min, int max, int step){
        return runSimulationOverVariable(min, max, step, _diameter);
    }
    public List<TestStatistics> runSimulationOverSpeed(int min, int max, int step){
        return runSimulationOverVariable(min, max, step, _speed);
    }
    public List<TestStatistics> runSimulationOverNumEdges(int min, int max, int step){
        return runSimulationOverVariable(min, max, step, _numEdges);
    }*/

    public TestStatistics runSimulation(){
        // TODO Check that all info is valid

        BreakageClassifierTester tester;
        try{
            TargetGenerator generator = getTargetGenerator();
            LaserDataCollector collector = getLaserDataCollector();
            LaserDataAnalyzer analyzer = getLaserDataAnalyzer();
            BreakageClassifier classifier = new LaserBreakageClassifier(collector, analyzer);
            tester = new BreakageClassifierTester(classifier, generator);
        }
        catch (IndexOutOfBoundsException e){
            throw new IllegalArgumentException("Index out of bounds");
        }

        return tester.test(_whole._value, _broken._value);
    }


    //#region Getters and Setters
    public void setDiameterValue(int v){
        _diameter._value = v;
    }
    public Int getDiameterObj(){
        return _diameter;
    }

    public void setSpeedValue(int v){
        _speed._value = v;
    }
    public Int getSpeedObj() {
        return _speed;
    }
    public void setNumLasersValue(int v){
        _numLasers._value = v;
    }
    public Int getNumLasersObj() {
        return _numLasers;
    }
    public void setSpacingValue(int v){
        _spacing._value = v;
    }
    public Int getSpacingObj() {
        return _spacing;
    }
    public void setNumEdgesValue(int v){
        _numEdges._value = v;
    }
    public Int getNumEdgesObj() {
        return _numEdges;
    }
    public void setWholeValue(int v){
        _whole._value = v;
    }
    public Int getWholeObj() {
        return _whole;
    }
    public void setBrokenValue(int v){
        _broken._value = v;
    }
    public Int getBrokenObj() {
        return _broken;
    }

    public Int get_generatorIndex() {
        return _generatorIndex;
    }

    public Int get_collectorIndex() {
        return _collectorIndex;
    }

    public Int get_analyzerIndex() {
        return _analyzerIndex;
    }

    public TargetGenerator getTargetGenerator(){
        return masterGeneratorList().get(_generatorIndex._value);
    }
    public LaserDataCollector getLaserDataCollector(){
        return masterDataCollectorList().get(_collectorIndex._value);
    }
    public LaserDataAnalyzer getLaserDataAnalyzer(){
        return masterDataAnalyzerList().get(_analyzerIndex._value);
    }

    //#endregion
}
