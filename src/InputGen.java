import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InputGen {
    public static void main(String[] args) throws IOException {
        int maxIterations = 100;
        int step = 1;
        double rStep = 0.001;

//        generateData(maxIterations, step);
        generateDataRadius(0.5, rStep, maxIterations);

        // Werkt:
        // Eén drievoudig snijpunt en de andere snijpnten WEL op de eindpunten van halfcirkels
        //Circle[] circles = new Circle[]{new Circle(0.2, 0.2, 0.2), new Circle(0.6, 0.2, 0.2), new Circle(0.4, 0.4, 0.2)};

        // Werkt:
        // Eén drievoudig snijpunt, en de andere snijpunten NIET op de eindpunten van halfcirkels
        //Circle[] circles = new Circle[]{new Circle(0.2, 0.2, 0.2), new Circle(0.4, 0.4, 0.2)/*, new Circle(0.35, 0.45, 0.1)*/};

        // Werkt:
        // Normaal geval
        //Circle[] circles = new Circle[]{new Circle(0.2, 0.2, 0.2), new Circle(0.5, 0.3, 0.3), new Circle(0.2, 0.45, 0.2)};

        // Werkt:
        // Twee snijpunten in eindpunten halfcirkels:
        //Circle[] circles = new Circle[]{new Circle(0.2, 0.2, 0.2), new Circle(0.4, 0.4, 0.2)};

        // Werkt:
        //Overlappende cirkels
        //Circle[] circles = new Circle[]{new Circle(0.2, 0.2, 0.2), new Circle(0.2, 0.2, 0.2)};

        // Werkt: geen snijpunten
        // Cirkel in de andere, met één snijpunt
        //Circle[] circles = new Circle[]{new Circle(0.5, 0.2, 0.2), new Circle(0.5, 0.4, 0.4)};

        //Main.sweepLine(circles);
    }

    public static void generateData(int maxIterations, int step) throws IOException {
        FileWriter bfWriter = new FileWriter("./plots/compares/bruteForceCompares.txt");
        FileWriter nslWriter = new FileWriter("./plots/compares/naiveSweeplineCompares.txt");
        FileWriter slWriter = new FileWriter("./plots/compares/sweepLineCompares.txt");


        FileWriter bfWriterT = new FileWriter("./plots/times/bruteForceTime.txt");
        FileWriter nslWriterT = new FileWriter("./plots/times/naiveSweeplineTime.txt");
        FileWriter slWriterT = new FileWriter("./plots/times/sweepLineTime.txt");
        int[] intersects;
        double[] times;
        int nrCircles;

        for (int count = 0; count < maxIterations; count++) {
            if (count % 50 == 0)
                System.out.println("Calculating iteration " + count + "...");
            nrCircles = count * step;
            intersects = countIntersectsRadius(nrCircles, count * step);
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
        System.out.println("Finished calculating all " + maxIterations + " iterations.");
    }


    public static void generateDataRadius(double maxRadius, double step, int N) throws IOException {
        FileWriter bfWriter = new FileWriter("./plots/compares/bruteForceCompares.txt");
        FileWriter nslWriter = new FileWriter("./plots/compares/naiveSweeplineCompares.txt");
        FileWriter slWriter = new FileWriter("./plots/compares/sweepLineCompares.txt");


        FileWriter bfWriterT = new FileWriter("./plots/times/bruteForceTime.txt");
        FileWriter nslWriterT = new FileWriter("./plots/times/naiveSweeplineTime.txt");
        FileWriter slWriterT = new FileWriter("./plots/times/sweepLineTime.txt");
        int[] intersects;
        double[] times;

        for (double r = 0.01; r < maxRadius; r += step) {
            r = Main.myRound(r, 3);
            if (r % 0.5 == 0)
                System.out.println("Calculating iteration " + r + "...");
            intersects = countIntersectsRadius(N, r);
            //times = timeAlgs();
            bfWriter.write(r + "," + intersects[0] + System.lineSeparator());
            nslWriter.write(r + "," + intersects[1] + System.lineSeparator());
            slWriter.write(r + "," + intersects[2] + System.lineSeparator());

            //bfWriterT.write(count + "," + times[0] + System.lineSeparator());
            //nslWriterT.write(count + "," + times[1] + System.lineSeparator());
            //slWriterT.write(count + "," + times[2] + System.lineSeparator());
        }

        bfWriter.close();
        nslWriter.close();
        slWriter.close();

        bfWriterT.close();
        nslWriterT.close();
        slWriterT.close();
        System.out.println("Finished calculating all " + maxRadius + " iterations.");
    }

    public static int[] countIntersects(int N) throws IOException {
        Circle[] circles = generateStackedCircles(N);
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

    public static int[] countIntersectsRadius(int N, double r) throws IOException {
        Circle[] circles = generateCirclesRadius(N, r);
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

    public static Circle[] generateAdjacentCircles(int N) {
        Circle[] circles = new Circle[N];
        Circle c;
        double x, y, r, max;
        r  = 1.0/ N;
        x = max = 0;
        y = 0.5;

        for (int i = 0; i < N; i++) {
            x = max + 0.02;
            circles[i] = new Circle(Main.myRound(x,2), Main.myRound(y,2), Main.myRound(r,2));
            max = x + 2 * r;
        }
        return circles;
    }
    public static Circle[] generateStackedCircles(int N) {
        Circle[] circles = new Circle[N];
        Circle c;
        double x, y, r, max;
        r = 1.0 / N;
        y = max = 0;
        x = 0.5;

        for (int i = 0; i < N; i++) {
            y = max + 0.02;
            circles[i] = new Circle(Main.myRound(x, 2), Main.myRound(y, 2), Main.myRound(r, 2));
            max = y + 2 * r;
        }
        return circles;
    }

    public static Circle[] generateCirclesRadius(int N, double r) {
        Circle[] circles = new Circle[N];
        double x, y;
        double minVal = 0.2;
        double maxVal = 0.8;

        for (int i = 0; i < N; i++) {
            Random rand = new Random();
            x = minVal + (maxVal - minVal) * rand.nextDouble();
            y = minVal + (maxVal - minVal) * rand.nextDouble();
            circles[i] = new Circle(Main.myRound(x,2), Main.myRound(y,2), Main.myRound(r,2));
        }
        return circles;
    }
}
