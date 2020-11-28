public abstract class AbstractLaserDataCollector implements LaserDataCollector {
    protected int numLasers;
    protected int spacing;
    protected int[][] laserPoints;
    private final int xOffset;
    private final int yOffset;

    /**
     * Creates a laser data collections with a given number of lasers spaced
     * a given spacing. The middle laser is centered at (0,0)
     * @param numLasers the number of lasers
     * @param spacing the spacing of the lasers
     */
    AbstractLaserDataCollector(int numLasers, int spacing){
        this.spacing = spacing;
        this.numLasers = numLasers;
        laserPoints = new int[numLasers][2];
        xOffset = 0;
        yOffset = 0;
        initializeLasers(0, 0);
    }

    /**
     * Creates a laser data collections with a given number of lasers spaced
     * a given spacing. The middle laser is centered at (x,y)
     * @param numLasers the number of lasers
     * @param spacing the spacing of the lasers
     * @param x the x offset
     * @param y the y offset
     */
    AbstractLaserDataCollector(int numLasers, int spacing, int x, int y){
        this.spacing = spacing;
        this.numLasers = numLasers;
        laserPoints = new int[numLasers][2];
        xOffset = x;
        yOffset = y;
        initializeLasers(x, y);
    }

    /**
     * Calculates the target dropping speed in mm/ms, with a min speed of 1 mm/ms
     * @return the speed in mm/ms
     */
    protected static int targetShiftPixels(int targetSpeedMph){
        int speed =  (int) (targetSpeedMph * 5280 * 12 * 2.54 /* to cm/hr */
                * 10 /* to mm/hr */
                * (1.0 / 3600) /* to mm/s*/
                * (1.0 / 1000) /* to mm/ms*/);

        return speed > 0 ? speed : 1;
    }

    /**
     * Initializes the laser point locations with a given x, y offsett
     * @param x the offset on the x axis
     * @param y the offset on the y axis
     */
    private void initializeLasers(int x, int y){
        if(numLasers % 2 == 1) {
            for (int i = 0; i < numLasers; i++) {
                laserPoints[i] = new int[]{-spacing * (numLasers - (numLasers % 2)) / 2 + i * spacing + x, y};
            }
        }
        else{
            for (int i = 0; i < numLasers; i++) {
                laserPoints[i] = new int[]{-spacing * (numLasers - (numLasers % 2)) / 2 + i * spacing + x + spacing/2, y};
            }
        }
    }

    /**
     * Sets all of the lasers to a given y location
     * @param y the new y pos
     */
    protected void setLaserPos(int y){
        for(int[] laserPoint : laserPoints){
            laserPoint[1] = y + yOffset;
        }
    }

    /**
     * Adds the given value to the y location of all the lasers
     * @param a the amount to shift by
     */
    protected void shiftLasers(int a){
        for(int[] laserPoint : laserPoints){
            laserPoint[1] += a;
        }
    }


    public int[][] getLasers(){
        return laserPoints;
    }
}
