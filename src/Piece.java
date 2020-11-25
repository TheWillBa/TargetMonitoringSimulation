import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

class Piece {

    private final List<Point> points;

    public Piece(){
        points = new ArrayList<Point>();

    }

    public int[] getXs() {
        int[] xs = new int[points.size()];
        for(int i = 0; i < points.size(); i++){
            xs[i] = points.get(i).x;
        }
        return xs;
    }

    public int[] getYs() {
        int[] ys = new int[points.size()];
        for(int i = 0; i < points.size(); i++){
            ys[i] = points.get(i).y;
        }
        return ys;
    }

    public void addPoint(int x, int y){
        points.add(new Point(x, y));
    }

    private Point getPoint(int i){
        return points.get(i);
    }

    public List<Point> getPoints(){
        return points;
    }

    /**
     * Checks if the given x y point is enclosed by the piece polygon
     * @param x the x coord
     * @param y the y coord
     * @return true is the point is on the piece
     */
    public boolean containsPoint (int x, int y){

        for(Point p : points){
            if(p.x == x && p.y == y) return true;
        }
        return pnpoly(getXs(), getYs(), x, y);
    }

    public static class Point
    {
        int x;
        int y;

        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    };



    //***********************************
    /*
    The code from G4G has some problems, so this works better
    https://stackoverflow.com/questions/8721406/how-to-deter
    mine-if-a-point-is-inside-a-2d-convex-polygon#:~:text=As
    suming%20that%20your%20point%20is,point%20is%20inside%20
    the%20polygon.
     */
    static boolean pnpoly(int[] vertx, int[] verty, int testx, int testy)
    {
        int nvert = vertx.length;
        int i, j;
        boolean c = false;
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( ((verty[i]>testy) != (verty[j]>testy)) &&
                    (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                c = !c;
        }
        return c;
    }


}
