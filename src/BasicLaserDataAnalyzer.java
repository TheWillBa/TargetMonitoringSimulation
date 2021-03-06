/**
 * A data analyzer the uses the first algorithm developed during the original provide;
 * aka, checks for a broken target by assuring that the target hits only a certain number of
 * consecutive lasers
 */
public class BasicLaserDataAnalyzer extends AbstractLaserDataAnalyzer{

    public BasicLaserDataAnalyzer(int targetDiameter, int spacing){
        super(spacing, targetDiameter);
    }

    /**
     * Classifies breakage by checking if only three consecutive lasers were hit
     * @param data the data collected from a simulated target drop
     * @return true if broken, else false
     */
    @Override
    public boolean isBroken(boolean[][] data) {
        int numLasers = data[0].length;
        int GOAL_HITS_1 = requiredNumHitLasers(targetDiameter, laserSpacing, numLasers);
        GOAL_HITS_1 = Math.min(GOAL_HITS_1, numLasers);
        //final int GOAL_HITS_2 = GOAL_HITS_1 - 1;


        int[] lasersHitState = new int[numLasers];

        for (boolean[] row : data) {
            for (int j = 0; j < row.length; j++) {
                if (row[j]) {
                    lasersHitState[j] = 1;
                }
            }
        }

        int lasersHitCount = 0;
        for(int state : lasersHitState){
            lasersHitCount += state;
        }

        //System.out.println(lasersHitCount);
        if(lasersHitCount != GOAL_HITS_1/* && lasersHitCount != GOAL_HITS_2 */) return true;

        // Check consecutive

        int[] lasersIndexesHit = new int[lasersHitCount];
        int count = 0;
        for(int i = 0; i < lasersHitState.length; i++){
            if(lasersHitState[i] > 0){
                lasersIndexesHit[count] = i;
                count++;
            }
        }

        return checkConsecutive(lasersIndexesHit);

    }


    /**
     * Gets the name that the listable object should be referred to as
     * @return the name of the object
     */
    @Override
    public String getName() {
        return "Basic consecutive (v1.0)";
    }
}
