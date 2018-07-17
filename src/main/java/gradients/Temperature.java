package gradients;

public class Temperature {
    private double[] values;

    public Temperature(double temperature, int center, int yMax) {

        this.values = new double[yMax];
        for (int i = 0; i < yMax; i++) {
            values[i] = temperature - Math.abs(i - center) / 3;
        }
    }

    public double[] getValues() {
        return values;
    }
}
