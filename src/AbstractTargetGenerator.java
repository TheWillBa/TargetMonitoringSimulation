import java.util.Arrays;

public abstract class AbstractTargetGenerator implements TargetGenerator{

    private final int numEdges; // How circular is the target?
    private final int targetRadius;

    protected AbstractTargetGenerator(int numEdges, int targetRadius){
        this.targetRadius = targetRadius;
        this.numEdges = numEdges;
    }

    @Override
    public Target getWholeTarget(){
        Target t = new Target();
        Piece p = new Piece();
        for(int i = 0; i < numEdges; i++){
            int x = (int) (Math.cos(i * 2 * Math.PI / numEdges) * targetRadius);
            int y = (int) (Math.sin(i * 2 * Math.PI / numEdges) * targetRadius);
            //System.out.print(x + 200 + " " );
            //System.out.print(y + 200 + "\n");
            p.addPoint(x, y);
            //System.out.println(Arrays.toString(p.getPoint(i)));
        }
        t.addPiece(p);
        return t;
    }
}
