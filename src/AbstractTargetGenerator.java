import java.util.Arrays;

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

    @Override
    public Target getWholeTarget() {
        return getWholeTarget(0, 0);
    }

    @Override
    public Target getWholeTarget(int xOrig, int yOrig){
        Target t = new Target(targetSpeedMph, targetRadius*2);
        Piece p = new Piece();
        for(int i = 0; i < numEdges; i++){
            int x = (int) (Math.cos(i * 2 * Math.PI / numEdges) * targetRadius);
            int y = (int) (Math.sin(i * 2 * Math.PI / numEdges) * targetRadius);
            //System.out.print(x + 200 + " " );
            //System.out.print(y + 200 + "\n");
            p.addPoint(x + xOrig, y + yOrig);
            //System.out.println(Arrays.toString(p.getPoint(i)));
        }
        t.addPiece(p);
        return t;
    }
}
