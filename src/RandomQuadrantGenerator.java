import java.util.Random;

/**
 * A target generator that creates broken targets by splitting a whole target into semi-random
 * quadrants. The numEdges of a target must be divisible by 4.
 */
public class RandomQuadrantGenerator extends AbstractTargetGenerator{


    /**
     * Creates a RandomQuadrantGenerator object
     * @param numEdges the number of edges a target will have; must be divisible by 4
     * @param targetDiameter the diameter that each target will have in millimeters
     */
    public RandomQuadrantGenerator(int numEdges, int targetDiameter, int targetSpeedMph){
        super(numEdges, targetDiameter, targetSpeedMph);
    }


    @Override
    public Target getBrokenTarget(int xOrig, int yOrig) {
        double numPieces = 4.0;

        Target t = new Target(targetSpeedMph, targetRadius*2);
        Piece p = new Piece();
        Random rng = new Random();
        int count = 0;

        // Shift the pieces a small random offset
        int xOff = (int) ((rng.nextDouble() - 0.5) * targetRadius);
        int yOff = (int) ((rng.nextDouble() - 0.5) * targetRadius);

        for(int i = 0; i < numEdges; i++){
            int x = (int) (Math.cos(i * 2 * Math.PI / numEdges) * targetRadius);
            int y = (int) (Math.sin(i * 2 * Math.PI / numEdges) * targetRadius);
            p.addPoint(x + xOff + xOrig, y + yOff + yOrig);
            count++;
            if(count % (numEdges/numPieces) == 0){
                // TODO Use polar coords to pick rand instead of box to move to n-section targets?
                double a = (Math.PI / numPieces) + Math.PI / 2 *  Math.floor(i / (numEdges / numPieces));
                //System.out.println(a);
                int xSign = (int) Math.signum(Math.cos(a));
                int ySign = (int) Math.signum(Math.sin(a));
                //System.out.println("X: " + xSign + " Y: " + ySign);
                int randX = (int) (rng.nextDouble() * targetRadius / 2 * xSign);
                int randY = (int) (rng.nextDouble() * targetRadius / 2 * ySign);
                p.addPoint(randX + xOff + xOrig, randY + yOff + yOrig);
                t.addPiece(p);

                // Reset these values to make new piece
                p = new Piece();
                xOff = (int) ((rng.nextDouble() - 0.5) * targetRadius / 2);
                yOff = (int) ((rng.nextDouble() - 0.5) * targetRadius / 2);
            }
        }
        // TODO add small scattered pieces for noise (make a separate class to extend with a noise function?)

        return t;
    }

    /**
     * Gets the name that the listable object should be referred to as
     *
     * @return the name of the object
     */
    @Override
    public String getName() {
        return "Random Quadrant";
    }
}
