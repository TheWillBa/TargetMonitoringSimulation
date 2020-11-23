public abstract class AbstractLaserDataCollector implements LaserDataCollector {
    private int numLasers;
    private int frameWidth;

    AbstractLaserDataCollector(int numLasers, int frameWidth){
        this.frameWidth = frameWidth;
        this.numLasers = numLasers;
    }
}
