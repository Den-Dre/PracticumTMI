public class Stopwatch {
    private long startTime;

    public Stopwatch() {
        this.startTime = System.nanoTime();
    }

    public void reset() {
        this.startTime = System.nanoTime();
    }

    // Return the elapsed time since creation of this Stopwatch in milliseconds
    public double getElapsedTime() {
        return (System.nanoTime() - startTime) / 1000.0;
    }
}
