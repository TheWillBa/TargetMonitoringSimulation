import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.Arrays;

public class LengthHitLaserDataAnalyzer extends AbstractLaserDataAnalyzer{
    public LengthHitLaserDataAnalyzer(int spacing, int targetDiameter) {
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

        int[] lasersHitState = new int[numLasers];

        for (boolean[] row : data) {
            for (int j = 0; j < row.length; j++) {
                if (row[j]) {
                    lasersHitState[j]++;
                }
            }
        }

        System.out.println(Arrays.toString(lasersHitState));

        // Counts how many lasers were hit

        int lasersHitCount = 0;
        for(int state : lasersHitState){
            if(state > 0){
                lasersHitCount++;
            }
        }

        if(lasersHitCount != GOAL_HITS_1) return true;

        // Check consecutive

        int[] lasersIndexesHit = new int[lasersHitCount];
        int count = 0;
        for(int i = 0; i < lasersHitState.length; i++){
            if(lasersHitState[i] > 0){
                lasersIndexesHit[count] = i;
                count++;
            }
        }

        if (checkConsecutive(lasersIndexesHit)) return true;

        int leftEdgeHits = lasersHitState[lasersIndexesHit[0]];
        int middleHits = lasersHitState[lasersIndexesHit[1]];
        int rightEdgeHits = lasersHitState[lasersIndexesHit[2]];

        // Check for circle shape

        return middleHits < leftEdgeHits || middleHits < rightEdgeHits;
    }

    /**
     * Gets the name that the listable object should be referred to as
     *
     * @return the name of the object
     */
    @Override
    public String getName() {
        return "Time Hit (v1.1)";
    }
}
