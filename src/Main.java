import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;
import static java.lang.StrictMath.sqrt;

enum Type {
    START, END
}
enum Orientation {
    UPPER, LOWER
}

public class Main {

    // Global variable to count number of intersect-calls in algorithms
    public static Counter c = new Counter();

    public static void main(String[] args) throws IOException {
//        System.out.println(Arrays.toString(intersect(new HalfCircle(0.5, 0.4, 0.2, null, Orientation.UPPER),
//                                                     new HalfCircle(0.7, 0.6, 0.3, null, Orientation.LOWER))));
        processInput();

//        RedBlackBST<TipTuple, HalfCircle> active = new RedBlackBST<>();
//        active.put(new TipTuple(1, new HalfCircle(1, 1, 1, Type.START, Orientation.LOWER)), new HalfCircle(1,1,1,Type.START,Orientation.LOWER) );
//        active.put(new TipTuple(2, new HalfCircle(2,2,2,Type.START,Orientation.LOWER)), new HalfCircle(2,2,2,Type.START,Orientation.LOWER));
//        System.out.println(active.floor(new TipTuple(2, new HalfCircle(2,2,2,Type.START,Orientation.LOWER))).getHalfCircle().toString());

    }

    public static void processInput() throws IOException {
        String[] input = readInput();
        int alg = Integer.parseInt(input[0]);
        int N = Integer.parseInt(input[1]);

        String[] parts;
        Circle[] circles = new Circle[N];
        for (int i=2; i < N+2; i++) {
            parts = input[i].split(" ");
            circles[i-2] = new Circle(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]),Double.parseDouble(parts[2]));
            }

        if (alg == 1) {
            bruteForce(circles);
        } else if (alg == 2) {
            naiveSweepLine(circles);
        } else {
            sweepLine(circles);
        }
    }

    // Reads the input file "in.txt" and returns it contents in a string.
    public static String[] readInput() throws IOException {
        File file = new File("in.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> returnString = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null)
            returnString.add(line);
        br.close();
        return returnString.toArray(new String[0]);
    }

    public static void bruteForce(Circle[] circles) throws IOException {
        Stopwatch sw = new Stopwatch();
        c.reset();
        List<Point> resPoints = new ArrayList<>();
        Circle c1, c2;
        Point[] points;
        for (int i=0; i<circles.length; i++) {
            c1 = circles[i];
            for (int j = i+1; j<circles.length; j++) {
                c2 = circles[j];
                points = intersect(c1, c2);
                if (points.length > 0)
                    Collections.addAll(resPoints, points);
            }
        }
        double elapsedTime = sw.getElapsedTime();
        generateOutput(resPoints, elapsedTime);
    }

    public static void naiveSweepLine(Circle[] circles) throws IOException {
        Stopwatch sw = new Stopwatch();
        c.reset();
        List<Point> resPoints = new ArrayList<>();
        List<Point> eventPoints = getEventPoints(circles);
        eventPoints.sort(new pointComparator());
        // Use treeMap? https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html
       List<Circle> active = new ArrayList<>();
       // TreeMap<Point, Point> active = new TreeMap<>(/*new pointComparator()*/);
/*       for (Point p : eventPoints) {
           if (p.getType() == Type.START) {
               for (Map.Entry<Point, Point> entry : active.entrySet()) {
                    Point[] intersecting = intersect(entry.getParent(), p.getParent());
                    if (intersecting.length > 0) {
                        Collections.addAll(resPoints, intersecting);
                    }
               }
               active.put(p, p);
           } else { // p.getType() == Type.END
                active.remove(p);
           }
       }*/ // TODO: Onderstaande methode werkt, vervang dit door bovenstaande die gebruikmaakt van een TreeMap (en pas aan naar TreeMap<Double, Circle> denk ik).
        for (Point p : eventPoints) {
            if (p.getType() == Type.START) {
                for (Circle candidate : active) {
                   Point[] intersecting = intersect(candidate, p.getParent());
                   if (intersecting.length > 0) {
                       Collections.addAll(resPoints, intersecting);
                   }
                }
                active.add(p.getParent());
            } else {
                active.remove(p.getParent());
            }
        }
       double elapsedTime = sw.getElapsedTime();
       generateOutput(resPoints, elapsedTime);
    }

    public static void sweepLine(Circle[] circles) throws IOException {
        Stopwatch sw = new Stopwatch();
        c.reset();

        List<Point> resPoints = new ArrayList<>();
        List<Point> eventPoints = getEventPoints(circles);
        eventPoints.sort(new pointComparator());
        TreeMap<TipTuple, HalfCircle> active = new TreeMap<>();
//        RedBlackBST<TipTuple, HalfCircle> active1 = new RedBlackBST<>();
        HalfCircle boven, onder, current;
        Point[] intersecting;

        /*
         TODO: In de input van het voorbeeld wordt er nog één snijpunt niet gevonden, nl.:\\
         0.41071428571428564 0.6794592695045965 (snijpunt F op screenshot GeoGebra)\\
         Dit komt omdat op het punt dat eq3 zijn boven semicircle wordt toegevoegd, enkel eq4 zijn LOWER circle er nog in zit, en zijn UPPER circle niet meer.
         Hierdoor wordt eq3 zijn bovencirkel vergeleken met de UPPER circle van eq2 om snijpunten te vinden, wat niets oplevert.
        */
        for (Point p : eventPoints) {
            // Start van nieuwe cirkel: voeg twee halfcirkels toe
            if (p.getType() == Type.START) {

                // ------------- Bovenste halfcirkel ---------------------
                double upperTip = myRound(p.getParent().getY() + p.getParent().getRadius(), 4);
                current = new HalfCircle(p.getParent().getX(),p.getParent().getY(),p.getParent().getRadius(),Type.START,Orientation.UPPER);
                TipTuple upperTipTuple = new TipTuple(upperTip, current);
                active.put(upperTipTuple, current);

                try {
                    boven = active.higherEntry(upperTipTuple).getValue();
                    intersecting = intersect(boven, current);
                    if (intersecting.length > 0)
                        Collections.addAll(resPoints, intersecting);
                } catch (NullPointerException ignored) {}

                try {
                    onder = active.lowerEntry(upperTipTuple).getValue();
                    intersecting = intersect(onder, current);
                    if (intersecting.length > 0)
                        Collections.addAll(resPoints, intersecting);
                } catch (NullPointerException ignored) {}


                // ------------- Onderste halfcirkel ---------------------
                double lowerTip = myRound(p.getParent().getY() - p.getParent().getRadius(), 4);
                current = new HalfCircle(p.getParent().getX(),p.getParent().getY(),p.getParent().getRadius(),Type.START,Orientation.LOWER);
                TipTuple lowerTipTuple = new TipTuple(lowerTip, current);
                active.put(lowerTipTuple, current);

                try {
                    boven = active.higherEntry(lowerTipTuple).getValue();
                    intersecting = intersect(boven, current);
                    if (intersecting.length > 0)
                        Collections.addAll(resPoints, intersecting);
                } catch (NullPointerException ignored) {}

                try {
                    onder = active.lowerEntry(lowerTipTuple).getValue();
                    intersecting = intersect(onder, current);
                    if (intersecting.length > 0)
                        Collections.addAll(resPoints, intersecting);
                } catch (NullPointerException ignored) {}

            } else { // p.getType() == Type.END
                // Als dit punt het einde van een cirkel is: verwijder de twee bijhorende halfcirkels

                // ------------- Bovenste halfcirkel ---------------------
                double upperTip = myRound(p.getParent().getY() + p.getParent().getRadius(), 4);
                current = new HalfCircle(p.getParent().getX(),p.getParent().getY(),p.getParent().getRadius(),Type.END,Orientation.UPPER);
                TipTuple upperTipTuple = new TipTuple(upperTip, current);

                try {
                    boven = active.higherEntry(upperTipTuple).getValue();
                    onder = active.lowerEntry(upperTipTuple).getValue();
                    intersecting = intersect(boven, onder);
                    if (intersecting.length > 0)
                        Collections.addAll(resPoints, intersecting);
                } catch (NullPointerException ignored) {}
                    active.remove(upperTipTuple);

                // ------------- Onderste halfcirkel ---------------------
                double lowerTip = myRound(p.getParent().getY() - p.getParent().getRadius(), 4);
                current = new HalfCircle(p.getParent().getX(),p.getParent().getY(),p.getParent().getRadius(),Type.END,Orientation.LOWER);
                TipTuple lowerTipTuple = new TipTuple(lowerTip, current);

                try {
                    boven = active.higherEntry(lowerTipTuple).getValue();
                    onder = active.lowerEntry(lowerTipTuple).getValue();
                    intersecting = intersect(boven, onder);
                    if (intersecting.length > 0)
                        Collections.addAll(resPoints, intersecting);
                } catch (NullPointerException ignored) {}
                    active.remove(lowerTipTuple);
            }
        }
        double elapsedTime = sw.getElapsedTime();
        generateOutput(resPoints, elapsedTime);
    }


static class pointComparator implements Comparator<Point> {
    @Override
    public int compare(Point point, Point other) {
        if (point.getX() > other.getX()) {
          return 1;
        } else if (point.getX() < other.getX()) {
            return -1;
        } else if (point.getX() == other.getX()) {
            if (other.getType() == Type.START && point.getType() == Type.END) { // end of point's circle and start of other's
               return 1;
            } else if (other.getType() == Type.END && point.getType() == Type.START) {
                return -1;
            } else {
                //System.out.println("This case is not handled in the pointComparator");
                return 0;
            }
        } else {
            //System.out.println("This case is not handled in the pointComparator");
            return 0;
        }
    }
}

    public static double myRound(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException("Negatieve precisie gevraagd.");
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<Point> getEventPoints(Circle[] circles) {
        List<Point> eventPoints = new ArrayList<>();
        for (Circle c : circles) {
//            eventPoints.add(new Point(Math.round((c.getX() - c.getRadius()) * 1.00)/ 1.00, c.getY(), Type.START, c));
//            eventPoints.add(new Point(Math.round((c.getX() + c.getRadius()) * 1.00) / 1.00, c.getY(), Type.END, c));
            eventPoints.add(new Point(myRound(c.getX() - c.getRadius(), 4), c.getY(), Type.START, c));
            eventPoints.add(new Point(myRound(c.getX() + c.getRadius(), 4) , c.getY(), Type.END, c));
        }
        return eventPoints;
    }

    public static void generateOutput(List<Point> points, double elapsedTime) throws IOException {
        FileWriter myWriter = new FileWriter("uit.txt");
        for (Point p : points)
            myWriter.write(p.getX() + " " + p.getY() + System.lineSeparator());
        myWriter.write(System.lineSeparator());
        myWriter.write(Double.toString(elapsedTime));
        myWriter.close();
    }

    // Calculate intersection points of two circles.
    // Based on: https://gist.github.com/jupdike/bfe5eb23d1c395d8a0a1a4ddd94882ac
    public static Point[] intersect(Circle c1, Circle c2) {
        double r1 = c1.getRadius();
        double r2 = c2.getRadius();
        double x1 = c1.getX();
        double x2 = c2.getX();
        double y1 = c1.getY();
        double y2 = c2.getY();
        double centerX = x1 - x2;
        double centerY = y1 - y2;
        double R = sqrt(centerX * centerX + centerY * centerY);

        c.inc();
        if (!(abs(r1 - r2) <= R && R <= r1 + r2))
            return new Point[0]; // no intersection points


        double R2 = R*R;
        double R4 = R2*R2;
        double a = (r1*r1 - r2*r2) /  (2 * R2);
        double r2r2 = (r1*r1 - r2*r2);
        double c = Math.sqrt(2 * (r1*r1 + r2*r2) / R2 - (r2r2 * r2r2) / R4 - 1);

        double fx = (x1+x2) / 2 + a * (x2 - x1);
        double gx = c * (y2 - y1) / 2;
        double ix1 = fx + gx;
        double ix2 = fx - gx;

        double fy = (y1+y2) / 2 + a * (y2 - y1);
        double gy = c * (x1 - x2) / 2;
        double iy1 = fy + gy;
        double iy2 = fy - gy;

        // Om dubbele snijpunten te vinden mits rekenfouten
        if (abs(ix1 - ix2) < 1e-7 && abs(iy1 - iy2) < 1e-7)
            return new Point[]{new Point(ix1, iy1)};

        return new Point[]{new Point(ix1, iy1), new Point(ix2, iy2)};
    }

    public static Point[] intersect(HalfCircle c1, HalfCircle c2) {
        if (c1.getRadius() == c2.getRadius() && c1.getX() == c2.getX() && c1.getY() == c2.getY())
            return new Point[0];

        double r1 = c1.getRadius();
        double r2 = c2.getRadius();
        double x1 = c1.getX();
        double x2 = c2.getX();
        double y1 = c1.getY();
        double y2 = c2.getY();
        double centerX = x1 - x2;
        double centerY = y1 - y2;
        double R = sqrt(centerX * centerX + centerY * centerY);

        if (!(abs(r1 - r2) <= R && R <= r1 + r2))
            return new Point[0]; // no intersection points

        c.inc();

        double R2 = R*R;
        double R4 = R2*R2;
        double a = (r1*r1 - r2*r2) /  (2 * R2);
        double r2r2 = (r1*r1 - r2*r2);
        double c = Math.sqrt(2 * (r1*r1 + r2*r2) / R2 - (r2r2 * r2r2) / R4 - 1);

        double fx = (x1+x2) / 2 + a * (x2 - x1);
        double gx = c * (y2 - y1) / 2;
        double ix1 = fx + gx;
        double ix2 = fx - gx;

        double fy = (y1+y2) / 2 + a * (y2 - y1);
        double gy = c * (x1 - x2) / 2;
        double iy1 = fy + gy;
        double iy2 = fy - gy;

        Point[] solutions = new Point[]{new Point(ix1, iy1), new Point(ix2, iy2)};

        // Om dubbelect snijpunten te detecteren mits rekenfouten
        if (abs(ix1 - ix2) < 1e-7 && abs(iy1 - iy2) < 1e-7)
           solutions = new Point[]{new Point(ix1, iy1)};

        Orientation o1 = c1.getOrientation();
        Orientation o2 = c2.getOrientation();
        if (o1 == Orientation.UPPER && o2 == Orientation.UPPER)
            return Arrays.stream(solutions).filter(p -> p.getY() <= min(c1.getY() + c1.getRadius(), c2.getY() + c2.getRadius()) && p.getY() >= max(c1.getY(), c2.getY())).toArray(Point[]::new);
        else if (o1 == Orientation.UPPER && o2 == Orientation.LOWER)
            return Arrays.stream(solutions).filter(p -> p.getY() <  min(c1.getY() + c1.getRadius(), c2.getY()) && p.getY() >  max(c1.getY(), c2.getY() - c2.getRadius())).toArray(Point[]::new);
        else if (o1 == Orientation.LOWER && o2 == Orientation.UPPER)
            return Arrays.stream(solutions).filter(p -> p.getY() <  min(c1.getY(), c2.getY() + c2.getRadius()) && p.getY() >  max(c1.getY() - c1.getRadius(), c2.getY())).toArray(Point[]::new);
        else if (o1 == Orientation.LOWER && o2 == Orientation.LOWER)
            return Arrays.stream(solutions).filter(p -> p.getY() <= min(c1.getY(), c2.getY()) && p.getY() >= max(c1.getY() - c1.getRadius(), c2.getY() - c2.getRadius())).toArray(Point[]::new);
        //else
        //   throw new OperationNotSupportedException("Not a valid combination of semicircle `Orientation`s");

        return solutions;
    }

    public static String printActive(TreeMap<TipTuple, HalfCircle> treeMap) {
        String res = "";
        Set<Map.Entry<TipTuple, HalfCircle>> entries = treeMap.entrySet();
        for (Map.Entry<TipTuple, HalfCircle> entry : entries) {
            res = res.concat(entry.getKey() + " -> " + entry.getValue()).concat("\n");
        }
        return res;
    }

}


class Circle {
    double x, y, radius;
    Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getRadius() {
        return this.radius;
    }
    public String toString() {
        return "x: " + this.x + ", y: " + this.y + ", radius: " + this.radius;
    }
}


class HalfCircle {
    double x, y, radius;
    Type type;
    Orientation orientation;
    HalfCircle(double x, double y, double radius, Type type, Orientation orientation) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.type = type;
        this.orientation = orientation;
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getRadius() {
        return this.radius;
    }
    public Orientation getOrientation() {
        return this.orientation;
    }
    public String toString() {
        return "x: " + this.x + ", y: " + this.y + ", radius: " + this.radius + ". Type: " + this.orientation;
    }
}

class Point implements Comparable<Point> {
    double x, y;
    Type type;
    Circle parent;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Point(double x, double y, Type t, Circle c) {
        this.x = x;
        this.y = y;
        this.type = t;
        this.parent = c;
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public Type getType() { return this.type; }
    public Circle getParent() { return this.parent; }
    public String toString() {
        return "x: " + this.x + ", y: " + this.y + ", Type: " + this.type;
    }

    public int compareTo(Point otherPoint) {
       return (int) (this.getX() - otherPoint.getX());
    }
}

class TipTuple implements Comparable<TipTuple> {
    double tip;
    HalfCircle halfCircle;

    TipTuple(double tip, HalfCircle c) {
        this.tip = tip;
        this.halfCircle = c;
    }
    public double getTip() {return this.tip;}
    public HalfCircle getHalfCircle() {return this.halfCircle;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipTuple tipTuple = (TipTuple) o;
        return Double.compare(tipTuple.tip, tip) == 0 &&
                Objects.equals(halfCircle, tipTuple.halfCircle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tip, halfCircle);
    }

    @Override
    public int compareTo(TipTuple o) {
       if (this.tip == o.tip) {
           return (int) Math.signum(this.getHalfCircle().getX() - o.getHalfCircle().getX());
       }
       return  (int) Math.signum(this.getTip() - o.getTip());
    }

    public String toString() {
        return "<Tip: " + this.tip + ", HC: " + this.halfCircle.toString() + ">";
    }
}
