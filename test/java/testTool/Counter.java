package testTool;

/**
 * Created by xlo on 2015/8/25.
 * it's the counter
 */
public class Counter {
    int num;

    public Counter(int num) {
        this.num = num;
    }

    public synchronized void add(int value) {
        this.num += value;
    }

    public synchronized int get() {
        return this.num;
    }
}
