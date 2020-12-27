package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int state, period;

    public StrangeBitwiseGenerator(int period) {
        state = -1;
        this.period = period;
    }

    public double next() {
        state = state + 1;
        //int weirdState = state & (state >> 3) & (state >> 8) % period;
        int weirdState = state & (state >> 7) % period;
        return 2.0 / period * weirdState - 1;
    }




}
