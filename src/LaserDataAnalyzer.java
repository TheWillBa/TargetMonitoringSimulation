public interface LaserDataAnalyzer {

    /**
     * Returns true if the data represents a broken target
     * @param data the data collected from a simulated target drop
     * @return true if the data represents a broken target, false if not
     */
    boolean isBroken(boolean[][] data);
}
