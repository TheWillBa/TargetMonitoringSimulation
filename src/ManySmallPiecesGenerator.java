import java.util.Random;

public class ManySmallPiecesGenerator extends AbstractTargetGenerator{


    /**
     * Creates a target generator for targets with a given amount of edges and
     * a given diameter in millimeters
     *
     * @param numEdges       number of edges for the 'circular' target
     * @param targetDiameter the diameter of the target in millimeters
     * @param targetSpeedMph
     */
    protected ManySmallPiecesGenerator(int numEdges, int targetDiameter, int targetSpeedMph) {
        super(numEdges, targetDiameter, targetSpeedMph);
    }

    @Override
    public Target getBrokenTarget(int x, int y) {
        Target t = new Target(targetSpeedMph, targetRadius * 2);
        Random r = new Random();
        int numPieces = r.nextInt(8) + 7;
        for(int i = 0; i < numPieces; i++){
            int xC = r.nextInt(targetRadius * 4) - (targetRadius * 2); // can change constants here
            int yC = r.nextInt(targetRadius * 4) - (targetRadius * 2);
            int maxRadius = (int) (targetRadius * r.nextDouble() / 1.5);
            int numSmallPoints = 5; // change later?

            Piece p = new Piece();
            for(int j = 0; j < numSmallPoints; j++){
                int xA = (int) (2 * (r.nextDouble() - 0.5) * maxRadius + xC);
                int yA = (int) (2 * (r.nextDouble() - 0.5) * maxRadius + yC);
                p.addPoint(xA + x, yA + y);
            }

            t.addPiece(p);
        }
        return t;
    }

    /**
     * Gets the name that the listable object should be referred to as
     *
     * @return the name of the object
     */
    @Override
    public String getName() {
        return "Many Small Pieces";
    }
}
