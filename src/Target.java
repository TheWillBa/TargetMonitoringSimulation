import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class Target {
    private final List<Piece> pieces;

    public Target(){
        pieces = new ArrayList<>();
    }

    public List<Piece> getPieces() {
        return pieces;
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
