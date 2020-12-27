package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int state, period;

    public SawToothGenerator(int period) {
        state = -1;
        this.period = period;
    }

    public double next() {
        state = (state + 1) % this.period;
        return 2.0 / period * state - 1;
    }




}
