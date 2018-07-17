package tiles;

import java.util.List;

public enum Climate {
    COLD,
    HOT,
    MILD;

    public static Climate getClimate(List<Tile> neighbours, double temp, double distance) {
        temp -= neighbours.stream().filter(x -> x.getClimate() == Climate.COLD).count();
        temp += neighbours.stream().filter(x -> x.getClimate() == Climate.HOT).count();
        temp += distance / 5 - 5;

        if (temp < 0) {
            return COLD;
        } else if (temp >= 0 && temp < 25) {
            return MILD;
        } else if (temp >= 25) {
            return HOT;
        }
        throw new IllegalArgumentException(String.valueOf(temp));
    }
}
