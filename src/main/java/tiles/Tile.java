package tiles;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Tile {
    final private int x;
    final private int y;

    private double temp;
    private double distance;

    private Terrain terrain;
    private Climate climate;

    private Origin origin;

    public Tile(int x, int y, double temp) {
        this.x = x;
        this.y = y;

        this.temp = temp;

        this.terrain = Terrain.OCEAN;
        this.climate = Climate.MILD;
    }

    public void generateLands(Tile[][] matrix, Origin origin, double pb) {
        this.origin = origin;
        this.distance = distanceWith(origin);

        List<Tile> neighbours = getAllNeighbours(matrix);
        if (neighbourProbability(neighbours, pb / 1.5, 0.25, 0.9, Terrain.LAND)) {
            this.terrain = Terrain.FOREST;
        } else {
            this.terrain = Terrain.LAND;
        }
        for (Tile neighbour : neighbours) {
            if (neighbour.terrain == Terrain.OCEAN && neighbourProbability(neighbours, pb, 0.6, 0.85, Terrain.OCEAN)) {
                origin.addChild(neighbour);
                neighbour.generateLands(matrix, origin, pb - ThreadLocalRandom.current().nextDouble(0.02, 0.05));
            }
        }
        this.climate = Climate.getClimate(neighbours, temp, distance);
    }

    public void generateBeaches(Tile[][] matrix) {
        if (terrain != Terrain.OCEAN) {
            if (getAllNeighbours(matrix).stream().filter(x -> x.terrain == Terrain.OCEAN).count() > 1) {
                if (ThreadLocalRandom.current().nextDouble(0.0, 0.5) + 0.25 * getAllNeighbours(matrix)
                        .stream()
                        .filter(x -> x.terrain == Terrain.BEACH)
                        .count() > 0.3) {
                    this.terrain = Terrain.BEACH;
                }
            }
        }
    }

    public void generateMountains() {
        if (terrain != Terrain.OCEAN) {
            if (distance < ThreadLocalRandom.current().nextInt(0, 10)) {
                this.terrain = Terrain.MOUNTAIN;
            }
        }
    }

    public double distanceWith(Tile other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    private boolean neighbourProbability(List<Tile> tiles, double pb, double in, double ts, Terrain condition) {
        double tpb = pb + (tiles.stream().filter(x -> x.terrain != condition).count() * in);
        if (tpb > 1.0) tpb = 0.99;

        return ThreadLocalRandom.current().nextDouble(tpb, 1.0) > ts;
    }

    private List<Tile> getNESWNeighbours(Tile[][] matrix) {
        List<Tile> neighbours = new ArrayList<>();

        if (y + 1 < matrix[x].length) neighbours.add(matrix[x][y + 1]);
        if (x + 1 < matrix.length) neighbours.add(matrix[x + 1][y]);
        if (y - 1 >= 0) neighbours.add(matrix[x][y - 1]);
        if (x - 1 >= 0) neighbours.add(matrix[x - 1][y]);

        return neighbours;
    }

    private List<Tile> getAllNeighbours(Tile[][] matrix) {
        List<Tile> neighbours = getNESWNeighbours(matrix);

        if (x + 1 < matrix.length && y + 1 < matrix[x].length) neighbours.add(matrix[x + 1][y + 1]);
        if (x + 1 < matrix.length && y - 1 >= 0) neighbours.add(matrix[x + 1][y - 1]);
        if (x - 1 >= 0 && y - 1 >= 0) neighbours.add(matrix[x - 1][y - 1]);
        if (x - 1 >= 0 && y + 1 < matrix[x].length) neighbours.add(matrix[x - 1][y + 1]);

        return neighbours;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getTemp() {
        return temp;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Climate getClimate() {
        return climate;
    }

    public Origin getOrigin() {
        return origin;
    }

    public ImageView getImageView() {
        return new ImageView(terrain.getImage(climate));
    }

    @Override
    public String toString() {
        return climate + " " + terrain + "@" + x + "/" + y;
    }
}
