package pandemic.pathFinding;

import pandemic.map.Tile;

import java.util.Comparator;

/**
 * Comparator of tiles scores.
 */
public class TileScoreComparator implements Comparator<Tile> {
    @Override
    public int compare(Tile o1, Tile o2) {
        return o1.getScore() - o2.getScore();
    }
}