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

    /**
     * Checks if the given x y point is enclosed by the piece polygon
     * @param x the x coord
     * @param y the y coord
     * @return true is the point is on the piece
     */
    public boolean containsPoint (int x, int y){
        return isInside(points, points.size(), new Point(x, y));
    }

    //*****************************
    /*
    All code below this line is from GeeksForGeeks because I couldn't be bother to do point
    checking myself. https://www.geeksforgeeks.org/how-to-check-if-a-given-point-lies-inside-a-polygon/
     */


    // Define Infinite (Using INT_MAX
    // caused overflow problems)
    private static int INF = 10000;

    private static class Point
    {
        int x;
        int y;

        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    };

    // Given three colinear points p, q, r,
    // the function checks if point q lies
    // on line segment 'pr'
    private static boolean onSegment(Point p, Point q, Point r)
    {
        return q.x <= Math.max(p.x, r.x) &&
                q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) &&
                q.y >= Math.min(p.y, r.y);
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    private static int orientation(Point p, Point q, Point r)
    {
        int val = (q.y - p.y) * (r.x - q.x)
                - (q.x - p.x) * (r.y - q.y);

        if (val == 0)
        {
            return 0; // colinear
        }
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // The function that returns true if
    // line segment 'p1q1' and 'p2q2' intersect.
    private static boolean doIntersect(Point p1, Point q1,
                               Point p2, Point q2)
    {
        // Find the four orientations needed for
        // general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
        {
            return true;
        }

        // Special Cases
        // p1, q1 and p2 are colinear and
        // p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1))
        {
            return true;
        }

        // p1, q1 and p2 are colinear and
        // q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1))
        {
            return true;
        }

        // p2, q2 and p1 are colinear and
        // p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2))
        {
            return true;
        }

        // p2, q2 and q1 are colinear and
        // q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2))
        {
            return true;
        }

        // Doesn't fall in any of the above cases
        return false;
    }

    // Returns true if the point p lies
    // inside the polygon[] with n vertices
    private static boolean isInside(List<Point> polygon, int n, Point p)
    {
        // There must be at least 3 vertices in polygon[]
        if (n < 3)
        {
            return false;
        }

        // Create a point for line segment from p to infinite
        Point extreme = new Point(INF, p.y);

        // Count intersections of the above line
        // with sides of polygon
        int count = 0, i = 0;
        do
        {
            int next = (i + 1) % n;

            // Check if the line segment from 'p' to
            // 'extreme' intersects with the line
            // segment from 'polygon[i]' to 'polygon[next]'
            if (doIntersect(polygon.get(i), polygon.get(next), p, extreme))
            {
                // If the point 'p' is colinear with line
                // segment 'i-next', then check if it lies
                // on segment. If it lies, return true, otherwise false
                if (orientation(polygon.get(i), p, polygon.get(next)) == 0)
                {
                    return onSegment(polygon.get(i), p,
                            polygon.get(next));
                }

                count++;
            }
            i = next;
        } while (i != 0);

        // Return true if count is odd, false otherwise
        return (count % 2 == 1); // Same as (count%2 == 1)
    }


}
