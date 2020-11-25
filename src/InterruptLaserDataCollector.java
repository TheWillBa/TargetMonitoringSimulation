public class InterruptLaserDataCollector extends AbstractLaserDataCollector{

    /**
     * Collects the target data each millis as if being dropped through lasers
     * @param numLasers the number of lasers
     * @param spacing the spacing of the lasers in mm
     */
    InterruptLaserDataCollector(int numLasers, int spacing) {
        super(numLasers, spacing);
    }

    /**
     * Collects the target data each millis as if being dropped through lasers accounting for an x, y offset
     * @param numLasers the number of lasers
     * @param spacing the spacing of the lasers in mm
     * @param x
     * @param y
     */
    InterruptLaserDataCollector(int numLasers, int spacing, int x, int y) {
        super(numLasers, spacing, x, y);
    }

    /**
     * Calculates the target dropping speed in mm/ms, with a min speed of 1 mm/ms
     * @return the speed in mm/ms
     */
    private static int targetShiftPixels(int targetSpeedMph){
        int speed =  (int) (targetSpeedMph * 5280 * 12 * 2.54 /* to cm/hr */
                * 10 /* to mm/hr */
                * (1.0 / 3600) /* to mm/s*/
                * (1.0 / 1000) /* to mm/ms*/);

        return speed > 0 ? speed : 1;
    }

    /**
     * Sets all of the lasers to a given y location
     * @param y the new y pos
     */
    private void setLaserPos(int y){
        for(int[] laserPoint : laserPoints){
            laserPoint[1] = y; // TODO account for offset
        }
    }

    /**
     * Adds the given value to the y location of all the lasers
     * @param a the amount to shift by
     */
    private void shiftLasers(int a){
        for(int[] laserPoint : laserPoints){
            laserPoint[1] += a;
        }
    }

    @Override
    public boolean[][] collectDataOnTarget(Target t) {

        int laserShiftNum = targetShiftPixels(t.getSpeedMph());
        int numSamples = 3 * t.getDiameter() / laserShiftNum;
        setLaserPos(-numSamples * laserShiftNum/2); // TODO deal with magic numbers

        boolean[][] data = new boolean[numSamples][numLasers];
        for(int i = 0; i < numSamples; i++){
            boolean[] current = data[i];

            for(int j = 0; j < numLasers; j++){
                int[] laserPoint = laserPoints[j];

                for(Piece p : t.getPieces()){ // scan all pieces for interrupt
                    if(p.containsPoint(laserPoint[0], laserPoint[1])){
                        current[j] = true;
                        break;
                    }
                }
            }
            shiftLasers(laserShiftNum);
        }
        return data;
    }
}
