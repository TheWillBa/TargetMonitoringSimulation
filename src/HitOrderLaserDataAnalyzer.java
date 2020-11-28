import java.lang.reflect.Array;
import java.util.Arrays;

public class HitOrderLaserDataAnalyzer extends AbstractLaserDataAnalyzer{
    public HitOrderLaserDataAnalyzer(int spacing, int targetDiameter) {
        super(spacing, targetDiameter);
    }

    /**
     * Returns true if the data represents a broken target
     *
     * @param data the data collected from a simulated target drop
     * @return true if the data represents a broken target, false if not
     */
    @Override
    public boolean isBroken(boolean[][] data) {
        int numLasers = data[0].length;
        int GOAL_HITS_1 = requiredNumHitLasers(targetDiameter, laserSpacing, numLasers);

        int[] lasersIndexHitOrder = new int[GOAL_HITS_1 * 2];

        int count = 0;
        for (int i = 0; i < data.length - 1; i++) {
            boolean[] row = data[i];
            boolean[] nextRow = data[i+1];
            for (int j = 0; j < row.length; j++) {
                if (row[j] != nextRow[j]) {
                    try{
                        lasersIndexHitOrder[count] = j;
                        count++;
                    }
                    catch (IndexOutOfBoundsException e){ // if there are too many hits return true
                        return true;
                    }
                }
            }
        }

        //System.out.println(Arrays.toString(lasersIndexHitOrder));

        // check correct order for hits
        int midIndex = lasersIndexHitOrder[0]; // the middle is the first hit
        for(int i = 0; i < lasersIndexHitOrder.length/2; i++){
            int ind = (int) Math.ceil(i/2.0);
            if(lasersIndexHitOrder[i] != midIndex + ind &&
                    lasersIndexHitOrder[i] != midIndex - ind) return true;

            if(lasersIndexHitOrder[lasersIndexHitOrder.length - 1 - i] != midIndex + ind &&
                    lasersIndexHitOrder[lasersIndexHitOrder.length - 1 - i] != midIndex - ind) return true;
        }

        return false;
    }

    /**
     * Gets the name that the listable object should be referred to as
     *
     * @return the name of the object
     */
    @Override
    public String getName() {
        return "Hit Order (v1.2)";
    }
}
