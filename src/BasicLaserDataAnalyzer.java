/**
 * A data analyzer the uses the first algorithm developed during the original provide;
 * aka, checks for a broken target by assuring that the target hits only a certain number of
 * consecutive lasers
 */
public class BasicLaserDataAnalyzer implements LaserDataAnalyzer{
    //private static final int GOAL_HITS = 3; // later determine by something else?
                                            // add parameters to get more information? (to constructor?)

    final private int laserSpacing;
    final private int targetDiameter;

    public BasicLaserDataAnalyzer(int targetDiameter, int spacing){
        laserSpacing = spacing;
        this.targetDiameter = targetDiameter;
    }

    /**
     * Classifies breakage by checking if only three consecutive lasers were hit
     * @param data the data collected from a simulated target drop
     * @return true if broken, else false
     */
    @Override
    public boolean isBroken(boolean[][] data) {
        int GOAL_HITS_1 = maxRequiredNumHitLasers(targetDiameter, laserSpacing);
        GOAL_HITS_1 = Math.min(GOAL_HITS_1, data[0].length);
        final int GOAL_HITS_2 = GOAL_HITS_1 - 1;
        //System.out.println(GOAL_HITS_1 + " " + GOAL_HITS_2);

        int[] lasersHitState = new int[data[0].length];

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
        if(lasersHitCount != GOAL_HITS_1 && lasersHitCount != GOAL_HITS_2 ) return true;

        // Check consecutive

        int[] lasersIndexesHit = new int[lasersHitCount];
        int count = 0;
        for(int i = 0; i < lasersHitState.length; i++){
            if(lasersHitState[i] > 0){
                lasersIndexesHit[count] = i;
                count++;
            }
        }

        for(int i = 0; i < lasersIndexesHit.length - 1; i++){
            if(lasersIndexesHit[i] + 1 != lasersIndexesHit[i + 1]) return true;
        }

        //System.out.println(Arrays.toString(lasersIndexesHit));

        return false;

    }


    /**
     * Returns the maximum number of lasers a whole target of a given diameter will hit
     * with a certain laser spacing.
     * The min number will be this value minus one.
     * @param diameter the diameter of the target
     * @param spacing the spacing of the lasers that the target will pass through
     * @return the mac number of lasers a whole target will hit
     */
    private static int maxRequiredNumHitLasers(int diameter, int spacing){
        int max =  (int) Math.round(diameter * 1.0/spacing);
        return max;
    }

    /**
     * Gets the name that the listable object should be referred to as
     *
     * @return the name of the object
     */
    @Override
    public String getName() {
        return "Basic consecutive (v1.0)";
    }
}
