import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Represents a clay target as a list of pieces, as well as contains
 * other target specific information
 */
public class Target {
    private final List<Piece> pieces;
    private final int speedMph;
    private final int diameter;

    public Target(int speedMph, int diameter){
        pieces = new ArrayList<>();
        this.speedMph = speedMph;
        this.diameter = diameter;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public int getDiameter() {
        return diameter;
    }

    public int getSpeedMph() {
        return speedMph;
    }

    public void addPiece(Piece p){
        pieces.add(p);
    }

    public boolean containsPoint(int x, int y){
        for(Piece p : pieces){
            if(p.containsPoint(x, y)) return true;
        }

        return false;
    }


}
