package worlds;

import gradients.Temperature;
import tiles.Origin;
import tiles.Tile;

import java.util.concurrent.ThreadLocalRandom;

public class World {
    private Tile[][] matrix;

    public World(int xMax, int yMax, int origins, double temp) {
        this.matrix = new Tile[xMax][yMax];

        Temperature temperature = new Temperature(temp, ThreadLocalRandom.current().nextInt(yMax/2, yMax), yMax);

        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                matrix[x][y] = new Tile(x, y, temperature.getValues()[y]);
            }
        }

        for (int i = 1; i < origins + 1; i++) {
            int rx = ThreadLocalRandom.current().nextInt(0, xMax);
            int ry = ThreadLocalRandom.current().nextInt(0, yMax);

            Origin origin = new Origin(i, rx, ry, temperature.getValues()[ry]);
            origin.generateLands(matrix, origin, ThreadLocalRandom.current().nextDouble(0.25, 0.95));
            if (origin.hasChildren()) matrix[rx][ry] = origin;
        }

        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                matrix[x][y].generateBeaches(matrix);
                matrix[x][y].generateMountains();
            }
        }
    }

    public World newInstance(int xMax, int yMax, int nOrig, double temp) {
        return new World(xMax, yMax, nOrig, temp);
    }

    public Tile[][] getMatrix() {
        return matrix;
    }
}
