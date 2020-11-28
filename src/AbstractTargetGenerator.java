import java.util.Arrays;

/**
 * An abstract class that provides default functionality for creating a whole target.
 */
public abstract class AbstractTargetGenerator implements TargetGenerator{

    protected final int numEdges; // How circular is the target?
    protected final int targetRadius;
    protected final int targetSpeedMph;

    /**
     * Creates a target generator for targets with a given amount of edges and
     * a given diameter in millimeters
     * @param numEdges number of edges for the 'circular' target
     * @param targetDiameter the diameter of the target in millimeters
     */
    protected AbstractTargetGenerator(int numEdges, int targetDiameter, int targetSpeedMph){
        this.targetRadius = targetDiameter/2;
        this.numEdges = numEdges;
        this.targetSpeedMph = targetSpeedMph;
    }

    /**
     * Gets a target that should be classified as whole with center (0,0)
     * @return a representation of a whole target
     */
    @Override
    public Target getWholeTarget() {
        return getWholeTarget(0, 0);
    }

    /**
     * Gets a target that should be classified as whole with center (x, y)
     * @param xOrig the x center of the target
     * @param yOrig the y center of the target
     * @return a representation of a whole target
     */
    @Override
    public Target getWholeTarget(int xOrig, int yOrig){ // TODO move shift to constructor?
        Target t = new Target(targetSpeedMph, targetRadius*2);
        Piece p = new Piece();
        for(int i = 0; i < numEdges; i++){
            int x = (int) (Math.cos(i * 2 * Math.PI / numEdges) * targetRadius);
            int y = (int) (Math.sin(i * 2 * Math.PI / numEdges) * targetRadius);

            p.addPoint(x + xOrig, y + yOrig);
        }
        t.addPiece(p);
        return t;
    }

    /**
     * Gets a broken target centered at (0,0)
     * @return a broken target
     */
    @Override
    public Target getBrokenTarget(){
        return getBrokenTarget(0,0);
    }

    protected static void addNoise(Target t){
        // Add some noise pieces to the given target
    }
}
