package tiles;

import java.util.HashSet;
import java.util.Set;

public class Origin extends Tile {
    private int id;

    private Set<Tile> children = new HashSet<>();

    public Origin(int id, int x, int y, double temp) {
        super(x, y, temp);
        this.id = id;
    }

    public Set<Tile> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public void addChild(Tile child) {
        this.children.add(child);
    }

    public int getId() {
        return id;
    }
}
