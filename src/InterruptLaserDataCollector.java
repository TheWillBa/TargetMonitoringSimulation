public class InterruptLaserDataCollector extends AbstractLaserDataCollector{
    private double targetSpeedMph;

    /**
     * Collects the target data each millis as if being dropped through lasers
     * @param numLasers the number of lasers
     * @param spacing the spacing of the lasers
     * @param targetSpeedMph the speed the targets fly at
     */
    InterruptLaserDataCollector(int numLasers, int spacing, double targetSpeedMph) {
        super(numLasers, spacing);
        this.targetSpeedMph = targetSpeedMph;
        System.out.println(targetShiftPixels());
    }

    InterruptLaserDataCollector(int numLasers, int spacing, double targetSpeedMph, int x, int y) {
        super(numLasers, spacing, x, y);
        this.targetSpeedMph = targetSpeedMph;
    }

    /**
     * Calculates the target dropping speed in mm/ms, with a min speed of 1 mm/ms
     * @return the speed in mm/ms
     */
    private int targetShiftPixels(){
        int speed =  (int) (targetSpeedMph * 5280 * 12 * 2.54 /* to cm/hr */
                * 10 /* to mm/hr */
                * (1.0 / 3600) /* to mm/s*/
                * (1.0 / 1000) /* to mm/ms*/);

        return speed > 0 ? speed : 1;
    }

    private void setLaserPos(int y){
        for(int[] laserPoint : laserPoints){
            laserPoint[1] = y;
        }
    }

    private void shiftLasers(){
        for(int[] laserPoint : laserPoints){
            laserPoint[1] += targetShiftPixels();
        }
    }

    @Override
    public boolean[][] collectDataOnTarget(Target t) {
        setLaserPos(-150); // TODO deal with magic numbers
        int numSamples = 300 / targetShiftPixels(); // 300mm is about 1 foot
        boolean[][] data = new boolean[300][numLasers];
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
            shiftLasers();
        }

        return data;
    }
}
