/**
 * Something that can produced laser sensor data (a boolean[][]) based on a given target
 */
public interface LaserDataCollector extends Listable{

    /**
     * Simulated dropping a target to collect relevant classification data
     * @param t the target to collect data from
     * @return a boolean[][] of laser data
     */
    boolean[][] collectDataOnTarget(Target t);

    /**
     * Gets the points that represent the lasers used to collect data
     * @return a int[] of laser points each represented as int[2]
     * with laser[0] = x and laser[1] = y
     */
    int[][] getLasers();
}
