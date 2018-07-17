package tiles;

import javafx.scene.image.Image;

public enum Terrain {
    BEACH("Beach"),
    DIRT("Dirt"),
    HOUSE("House"),
    FOREST("Forest"),
    LAND("Land"),
    MOUNTAIN("Mountain"),
    OCEAN("Ocean");

    private String name;

    private Image cold;
    private Image hot;
    private Image mild;

    Terrain(String name) {
        this.name = name;

        this.cold = new Image(getClass().getResource("/tiles/cold/" + name + ".png").toString(), 16, 16, false, true);
        this.hot = new Image(getClass().getResource("/tiles/hot/" + name + ".png").toString(), 16, 16, false, true);
        this.mild = new Image(getClass().getResource("/tiles/mild/" + name + ".png").toString(), 16, 16, false, true);
    }

    public static void reloadAll(int size) {
        for (Terrain terrain : values()) {
            terrain.cold = new Image(terrain.getClass().getResource("/tiles/cold/" + terrain.name + ".png").toString(), size, size, false, true);
            terrain.hot = new Image(terrain.getClass().getResource("/tiles/hot/" + terrain.name + ".png").toString(), size, size, false, true);
            terrain.mild = new Image(terrain.getClass().getResource("/tiles/mild/" + terrain.name + ".png").toString(), size, size, false, true);
        }
    }

    public Image getImage(Climate climate) {
        switch (climate) {
            case HOT:
                return hot;
            case COLD:
                return cold;
            case MILD:
                return mild;
            default:
                throw new IllegalArgumentException(climate.name());
        }
    }
}
