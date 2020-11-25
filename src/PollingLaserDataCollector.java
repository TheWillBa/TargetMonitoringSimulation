public class PollingLaserDataCollector extends AbstractLaserDataCollector{
    PollingLaserDataCollector(int numLasers, int spacing) {
        super(numLasers, spacing);
    }

    PollingLaserDataCollector(int numLasers, int spacing, int x, int y) {
        super(numLasers, spacing, x, y);
    }

    @Override
    public boolean[][] collectDataOnTarget(Target t) {

        int laserShiftNum = targetShiftPixels(t.getSpeedMph());
        int numSamples = 3 * t.getDiameter() / laserShiftNum;
        setLaserPos(-numSamples * laserShiftNum/2); // TODO deal with magic numbers

        int currentLaserIndex = 0;
        boolean[][] data = new boolean[numSamples][numLasers];
        for(int i = 0; i < numSamples; i++) {
            boolean[] current = data[i];

            int[] laserPoint = laserPoints[currentLaserIndex];
            for (Piece p : t.getPieces()) { // scan all pieces for interrupt
                if (p.containsPoint(laserPoint[0], laserPoint[1])) {
                    current[currentLaserIndex] = true;
                    break;
                }
            }
            currentLaserIndex++;
            currentLaserIndex %= numLasers;
            shiftLasers(laserShiftNum);
        }
        return data;
    }
}