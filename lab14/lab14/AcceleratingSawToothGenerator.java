package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private double rate;
    private int period, state;

    public AcceleratingSawToothGenerator(int period, double rate) {
        this.period = period;
        this.rate = rate;
        state = 0;
    }

    @Override
    public double next() {
        double n = 2.0 / this.period * state - 1;
        state = (state + 1) % this.period;
        if (state == 0) {
            this.period = (int) (this.period * this.rate);
        }
        return n;
    }
}
