import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InputGen {
    public static void main(String[] args) throws IOException {
        generateData(1000, 1);
    }

    public static void generateData(int dataPoints, int step) throws IOException {
        FileWriter bfWriter = new FileWriter("bruteForce.txt");
        FileWriter nslWriter = new FileWriter("naiveSweepline.txt");
        FileWriter slWriter = new FileWriter("sweepLine.txt");
        int[] intersects;
        int nrCircles;

        for (int count = 0; count < dataPoints; count++) {
            nrCircles = count * step;
            intersects = countIntersects(nrCircles);
            bfWriter.write(nrCircles + "," + intersects[0] + System.lineSeparator());
            nslWriter.write(nrCircles + "," + intersects[1] + System.lineSeparator());
            slWriter.write(nrCircles + "," + intersects[2] + System.lineSeparator());
        }

        bfWriter.close();
        nslWriter.close();
        slWriter.close();
    }

    public static int[] countIntersects(int N) throws IOException {
        Circle[] circles = generateCircles(N);
        int[] counts = new int[3];

        Main.c.reset();
        Main.bruteForce(circles);
        counts[0] = Main.c.getCount();

        Main.c.reset();
        Main.naiveSweepLine(circles);
        counts[1] = Main.c.getCount();

        Main.c.reset();
        Main.sweepLine(circles);
        counts[2] = Main.c.getCount();

        return counts;
    }

    public static Circle[] generateCircles(int N) {
        Circle[] circles = new Circle[N];
        Circle c;
        double x, y, r;
        double minVal = 0.2;
        double maxVal = 0.8;

        for (int i = 0; i < N; i++) {
            Random rand = new Random();
            x = minVal + (maxVal - minVal) * rand.nextDouble();
            y = minVal + (maxVal - minVal) * rand.nextDouble();
            r = minVal + (maxVal - minVal) * rand.nextDouble();
            circles[i] = new Circle(Main.myRound(x,2), Main.myRound(y,2), Main.myRound(r,2));
        }
        return circles;
    }
}
