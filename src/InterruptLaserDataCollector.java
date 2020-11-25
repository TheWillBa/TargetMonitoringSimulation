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
