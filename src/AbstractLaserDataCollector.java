public abstract class AbstractLaserDataCollector implements LaserDataCollector {
    protected int numLasers;
    protected int spacing;
    protected int[][] laserPoints;

    AbstractLaserDataCollector(int numLasers, int spacing){
        this.spacing = spacing;
        this.numLasers = numLasers;
        laserPoints = new int[numLasers][2];
        initializeLasers(0, 0);
    }

    AbstractLaserDataCollector(int numLasers, int spacing, int x, int y){
        this.spacing = spacing;
        this.numLasers = numLasers;
        laserPoints = new int[numLasers][2];

        initializeLasers(x, y);
    }

    /**
     * Initializes the laser point locations with a given x, y offsett
     * @param x the offset on the x axis
     * @param y the offset on the y axis
     */
    private void initializeLasers(int x, int y){
        for(int i = 0; i < numLasers; i++){
            laserPoints[i] = new int[]{-spacing * (numLasers - (numLasers % 2))/2 + i * spacing + x,y};
            //System.out.println("( " + laserPoints[i][0] + ", " + laserPoints[i][1] + ")");
        }
    }
}
