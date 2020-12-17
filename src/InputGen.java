import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InputGen {
    public static void main(String[] args) throws IOException {
        int N = 1000;
        int iterations = 1;
//        generateTimeData(N, iterations);
        generateData(N, iterations);
    }

    public static void generateData(int dataPoints, int step) throws IOException {
        FileWriter bfWriter = new FileWriter("./plots/compares/bruteForceCompares.txt");
        FileWriter nslWriter = new FileWriter("./plots/compares/naiveSweeplineCompares.txt");
        FileWriter slWriter = new FileWriter("./plots/compares/sweepLineCompares.txt");


        FileWriter bfWriterT = new FileWriter("./plots/times/bruteForceTime.txt");
        FileWriter nslWriterT = new FileWriter("./plots/times/naiveSweeplineTime.txt");
        FileWriter slWriterT = new FileWriter("./plots/times/sweepLineTime.txt");
        int[] intersects;
        double[] times;
        int nrCircles;

        for (int count = 0; count < dataPoints; count++) {
            if (count % 50 == 0)
                System.out.println("Calculating iteration " + count + "...");
            nrCircles = count * step;
            intersects = countIntersects(nrCircles);
            times = timeAlgs(nrCircles);
            bfWriter.write(nrCircles + "," + intersects[0] + System.lineSeparator());
            nslWriter.write(nrCircles + "," + intersects[1] + System.lineSeparator());
            slWriter.write(nrCircles + "," + intersects[2] + System.lineSeparator());

            bfWriterT.write(nrCircles + "," + times[0] + System.lineSeparator());
            nslWriterT.write(nrCircles + "," + times[1] + System.lineSeparator());
            slWriterT.write(nrCircles + "," + times[2] + System.lineSeparator());
        }

        bfWriter.close();
        nslWriter.close();
        slWriter.close();

        bfWriterT.close();
        nslWriterT.close();
        slWriterT.close();
        System.out.println("Finished calculating all " + dataPoints + " iterations.");
    }

    public static void generateTimeData(int dataPoints, int step) throws IOException {
        FileWriter bfWriter = new FileWriter("./plots/times/bruteForceTime.txt");
        FileWriter nslWriter = new FileWriter("./plots/times/naiveSweeplineTime.txt");
        FileWriter slWriter = new FileWriter("./plots/times/sweepLineTime.txt");
        double[] times;
        int nrCircles;

        for (int count = 0; count < dataPoints; count++) {
            if (count % 50 == 0)
                System.out.println("Calculating iteration " + count + "...");
            nrCircles = count * step;
            times = timeAlgs(nrCircles);
            bfWriter.write(nrCircles + "," + times[0] + System.lineSeparator());
            nslWriter.write(nrCircles + "," + times[1] + System.lineSeparator());
            slWriter.write(nrCircles + "," + times[2] + System.lineSeparator());
        }

        bfWriter.close();
        nslWriter.close();
        slWriter.close();
        System.out.println("Finished calculating all " + dataPoints + " iterations.");
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

    public static double[] timeAlgs(int N) throws IOException {
        Circle[] circles = generateCircles(N);
        double[] times = new double[3];
        Stopwatch sw = new Stopwatch();

        sw.reset();
        Main.bruteForce(circles);
        times[0] = sw.getElapsedTime();

        sw.reset();
        Main.naiveSweepLine(circles);
        times[1] = sw.getElapsedTime();

        sw.reset();
        Main.sweepLine(circles);
        times[2] = sw.getElapsedTime();

        return times;
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
