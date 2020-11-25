public interface TargetGenerator {

    /**
     * Returns a Target that should be classified as 'broken' centered at 0, 0
     * @return a 'broken' target
     */
    Target getBrokenTarget();

    /**
     * Returns a Target that should be classified as 'broken' with the given x, y offset applied
     * @return a 'broken' target
     */
    Target getBrokenTarget(int x, int y);

    /**
     * Returns a Target that should be classified as whole centered at 0, 0
     * @return a 'whole' target
     */
    Target getWholeTarget();

    /**
     * Returns a Target that should be classified as whole with the given x, y offset applied
     * @return a 'whole' target
     */
    Target getWholeTarget(int x, int y);

    // Make abstract class for whole target because it is the easiest one and can be reused; aka
    // the methods of making broken targets are more interesting


}
