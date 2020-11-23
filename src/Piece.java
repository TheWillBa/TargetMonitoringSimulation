import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

class Piece {
    private final List<Integer> xList;
    private final List<Integer> yList;

    public Piece(){
        xList = new ArrayList<>();
        yList = new ArrayList<>();
    }

    public List<Integer> getXs() {
        return xList;
    }

    public List<Integer> getYs() {
        return yList;
    }

    public void addPoint(int x, int y){
        xList.add(x);
        yList.add(y);
    }

    public int[] getPoint(int i){
        return new int[]{xList.get(i), yList.get(i)};
    }

    /**
     * Checks if the given x y point is enclosed by the piece polygon
     * @param x the x coord
     * @param y the y coord
     * @return true is the point is on the piece
     */
    public boolean containsPoint (int x, int y){
        throw new NotImplementedException();
    }


}
