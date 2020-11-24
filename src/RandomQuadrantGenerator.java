import java.util.Random;

public class RandomQuadrantGenerator extends AbstractTargetGenerator{

    public RandomQuadrantGenerator(int numEdges, int targetDiameter){
        super(numEdges, targetDiameter);
    }

    @Override
    public Target getBrokenTarget() {
        return getBrokenTarget(0, 0);
    }

    @Override
    public Target getBrokenTarget(int xOrig, int yOrig) {
        double numPieces = 4.0;

        Target t = new Target();
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
}
