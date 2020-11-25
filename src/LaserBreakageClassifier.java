public class LaserBreakageClassifier implements  BreakageClassifier{
    private LaserDataCollector collector;
    private LaserDataAnalyzer analyzer;

    public LaserBreakageClassifier(LaserDataCollector collector, LaserDataAnalyzer analyzer){
        this.collector = collector;
        this.analyzer = analyzer;
    }

    @Override
    public boolean isBroken(Target t) {
        boolean[][] data = collector.collectDataOnTarget(t);
        return analyzer.isBroken(data);
    }

    @Override
    public boolean isWhole(Target t) {
        return !isBroken(t);
    }
}
