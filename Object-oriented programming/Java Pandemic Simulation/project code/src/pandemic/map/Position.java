package pandemic.map;

/**
 * Class position storing data about coordinates.
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Position constructor.
     * @param x
     * @param y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return Position description.
     */
    @Override
    public String toString() {
        return "(" +
                x +
                "," + y +
                ')';
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
