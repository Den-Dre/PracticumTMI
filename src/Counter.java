public class Counter {
    int count;
    Counter() {
       this.count = 0;
    }

    public void inc() {
        this.count = this.count + 1;
    }

    public void reset() {
        this.count = 0;
    }

    public int getCount() {
        return this.count;
    }
}
