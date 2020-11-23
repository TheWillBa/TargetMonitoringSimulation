/**
 * An interface for a class that can classify a target as
 * whole or broken
 */
public interface BreakageClassifier {

    /**
     * Returns true if the target is 'broken'
     * @param t a target
     * @return true if the target is broken, false if not
     */
    boolean isBroken(Target t);
}
