public class TestConfiguration {
    final private TestStatistics stats;
    final private int diameter;
    final private int speed;
    final private int numLasers;
    final private int numEdges;
    final private int spacing;


    public TestConfiguration(TestStatistics stats, int diameter, int speed, int numLasers, int numEdges, int spacing) {
        this.stats = stats;
        this.diameter = diameter;
        this.speed = speed;
        this.numLasers = numLasers;
        this.numEdges = numEdges;
        this.spacing = spacing;
    }
}
