public abstract class AbstractLaserDataAnalyzer implements LaserDataAnalyzer{
    protected final int laserSpacing;
    protected final int targetDiameter;

    public AbstractLaserDataAnalyzer(int spacing, int targetDiameter) {
        laserSpacing = spacing;
        this.targetDiameter = targetDiameter;
    }

    /**
     * Returns the maximum number of lasers a whole target of a given diameter will hit
     * with a certain laser spacing if the target hits in the middle of the lasers
     * @param diameter the diameter of the target
     * @param spacing the spacing of the lasers that the target will pass through
     * @return the mac number of lasers a whole target will hit
     */
    protected static int requiredNumHitLasers(int diameter, int spacing, int numLasers){
        int max;
        if(numLasers % 2 == 1){ // if there are an odd number of lasers
            max = 1 + 2 * ((diameter/2) / (spacing));
        }
        else{ // even number of lasers
            if(diameter < spacing){ // edge case, small target slips through lasers
                max = 0;
            }
            else {
                max = 2 + 2 * (((diameter - spacing) / 2) / spacing);
            }
        }
        System.out.println("MAX: " + max);
        return max;
    }

    /**
     * Returns true if the ints in the given array are consecutive.
     * For example: [3, 4, 5] will return true, but [1, 2, 6] will not
     * @param arr the array to check
     * @return
     */
    protected static boolean checkConsecutive(int[] arr){
        for(int i = 0; i < arr.length - 1; i++){
            if(arr[i] + 1 != arr[i + 1]) return true;
        }

        return false;
    }
}
