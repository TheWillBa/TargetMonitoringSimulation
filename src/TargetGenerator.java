public interface TargetGenerator {

    /**
     * Returns a Target that should be classified as 'broken'
     * @return a 'broken' target
     */
    Target getBrokenTarget();

    /**
     * Returns a Target that should be classified as whole
     * @return a 'whole' target
     */
    Target getWholeTarget();

    // Make abstract class for whole target because it is the easiest one and can be reused; aka
    // the methods of making broken targets are more interesting

}
