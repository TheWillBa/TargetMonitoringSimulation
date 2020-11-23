public interface LaserDataCollector {

    /**
     * Simulated dropping a target to collect relevent classification data
     * @param t
     * @return
     */
    boolean[][] collectDataOnTarget(Target t);
}
