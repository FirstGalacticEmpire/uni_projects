package pandemic.pathFinding;
import pandemic.map.Position;
import pandemic.map.Tile;
import java.util.*;

/**
 *  A* pathfinding class.
 *  Loosely based on code found on github.
 *  I'm so sorry, but I lost the source.
 *  Implementation of teleports is my own.
 *  Algorithms and
 *  Heuristic inspired by:
 *  https://stackoverflow.com/questions/14428331/a-admissible-heuristics-on-a-grid-with-teleporters
 */
public class PathFinder {
    private ArrayList<ArrayList<Tile>> tiles;
    private Tile currentPathFinderTile;
    private Position endPosition;
    private int maxWidth;
    private int maxHeight;
    private int tileType;

    private static ArrayList<Tile> listOfTeleports;
    private final TileScoreComparator tileScoreComparator = new TileScoreComparator();

    /**
     * PathFinder Constructor.
     * @param listOfTeleports
     * @param tileType
     */
    public PathFinder(ArrayList<Tile> listOfTeleports, int tileType) {
        PathFinder.listOfTeleports = listOfTeleports;
        this.tileType = tileType;
    }


    /**
     * @param tiles map array
     * @param startPosition from where
     * @param endPosition to where
     * @param tileType on what type of tiles
     * @param shopType what type of shop are we looking for
     * @return empty list if no route found, otherwise route to the endPosition, consisting of
     * coordinates.
     */
    public ArrayList<Position> getRoute(ArrayList<ArrayList<Tile>> tiles, Position startPosition, Position endPosition, int tileType, int shopType) {
        ArrayList<ArrayList<Tile>> tiles_copy;

        //hard copy

        tiles_copy = new ArrayList<>(tiles.size() * tiles.get(0).size());
        for (int x = 0; x < tiles.size(); x++) {
            tiles_copy.add(new ArrayList<>());
            for (int y = 0; y < tiles.get(0).size(); y++) {
                tiles_copy.get(x).add(new Tile(x, y));
            }
        }
        Collections.copy(tiles_copy, tiles);


        this.tiles = tiles_copy;
        this.maxWidth = tiles_copy.size();
        this.maxHeight = tiles_copy.get(0).size();
        this.endPosition = endPosition;

        resetAllTiles(this.tiles);
        PriorityQueue<Tile> queue = new PriorityQueue<>(tileScoreComparator);
        queue.add(tiles_copy.get(startPosition.getX()).get(startPosition.getY()));

        boolean routeAvailable = false;

        while (!queue.isEmpty()) {
            do {
                if (queue.isEmpty()) break;
                currentPathFinderTile = queue.remove();
            } while (!currentPathFinderTile.isOpen());

            Tile previousTile = null;
            if (currentPathFinderTile.isTeleport()) {
                currentPathFinderTile.setOpen(false);
                previousTile = currentPathFinderTile;
                currentPathFinderTile = currentPathFinderTile.getGoalTeleportTile();

            }
            currentPathFinderTile.setOpen(false);

            int currentX = currentPathFinderTile.getX();
            int currentY = currentPathFinderTile.getY();
            int currentScore = currentPathFinderTile.getScore();

            if (currentPathFinderTile.getX() == endPosition.getX() && currentPathFinderTile.getY()
                    == endPosition.getY()) {
                // at the end, return path
                routeAvailable = true;
                break;
            }



            //this could have been done in a better way.
            int smallestScore = 9999999;
            for (int x = -1; x <= 1; x += 2) {
                int nextX = currentX + x;

                if (validTile(nextX, currentY, tileType, shopType)) {
                    int score = getScoreOfTile(tiles_copy.get(nextX).get(currentY), currentScore);
                    if (score < smallestScore) {
                        smallestScore = score;
                    }
                    Tile thisTile = tiles_copy.get(nextX).get(currentY);
                    thisTile.setScore(score);
                    queue.add(thisTile);
                    if (previousTile != null) {
                        thisTile.setParent(previousTile);
                    } else {
                        thisTile.setParent(currentPathFinderTile);
                    }
                }
            }

            for (int y = -1; y <= 1; y += 2) {
                int nextY = currentY + y;
                if (validTile(currentX, nextY, tileType, shopType)) {
                    int score = getScoreOfTile(tiles_copy.get(currentX).get(nextY), currentScore);

                    if (score < smallestScore) {
                        smallestScore = score;
                    }

                    Tile thisTile = tiles_copy.get(currentX).get(nextY);
                    thisTile.setScore(score);
                    queue.add(thisTile);

                    if (previousTile != null) {
                        thisTile.setParent(previousTile);
                    } else {
                        thisTile.setParent(currentPathFinderTile);
                    }
                }
            }


        }

        if (routeAvailable) {
            return getPath(currentPathFinderTile);
        } else {
            return new ArrayList<>();
        }
    }

    private void resetAllTiles(ArrayList<ArrayList<Tile>> tilesToReset) {
        for (ArrayList<Tile> row : tilesToReset) {
            for (Tile tile : row) {
                tile.setOpen(true);
                tile.setParent(null);
                tile.setScore(0);
            }
        }
    }

    private ArrayList<Position> getPath(Tile currentTile) {
        ArrayList<Position> path = new ArrayList<>();
        while (currentTile != null) {
            for (Tile position : listOfTeleports) {
                if (currentTile.getX() == position.getX() && currentTile.getY() == position.getY()) {
                    path.add(new Position(position.getGoalTeleportTile().getX(), position.getGoalTeleportTile().getY()));
                }
            }
            path.add(new Position(currentTile.getX(), currentTile.getY()));
            currentTile = currentTile.getParent();

        }
        Collections.reverse(path);
        return path;
    }

    private static Tile getClosestTeleport(Position currentPosition, ArrayList<Tile> listOfTeleports) {
        Tile closest = null;
        double distance = 99999;
        for (Tile position : listOfTeleports) {
            double new_distance = Math.abs(currentPosition.getX() - position.getX()) + Math.abs(currentPosition.getY() - position.getY());
            if (new_distance < distance) {
                closest = position;
                distance = new_distance;
            }
        }
        return closest;
    }

    private int distanceScoreAway(Tile currentTile) {
        try {
            int x = currentTile.getX();
            int y = currentTile.getY();
            Tile closestTeleport = getClosestTeleport(new Position(x, y), listOfTeleports);

            int a = Math.abs(endPosition.getX() - currentTile.getX()) + Math.abs(endPosition.getY() - currentTile.getY());

            int b = Math.abs(closestTeleport.getX() - x) + Math.abs(closestTeleport.getY() - y);
            int c = Math.abs(closestTeleport.getGoalTeleportTile().getY() - endPosition.getY()) +
                    Math.abs(closestTeleport.getGoalTeleportTile().getX() - endPosition.getX());

            b += c;
            return Math.min(a, b);
        } catch (NullPointerException ignored) {
            //Map without teleports.
            return Math.abs(endPosition.getX() - currentTile.getX()) + Math.abs(endPosition.getY() - currentTile.getY());
        }
    }

    private int getScoreOfTile(Tile tile, int currentScore) {
        int guessScoreLeft = distanceScoreAway(tile);
        int extraMovementCost = 0;
        if (tile.isOccupied() && !tile.isTeleport()) {
            extraMovementCost += 2;
        }
        int movementScore = currentScore + 1;
        return guessScoreLeft + movementScore + extraMovementCost;
    }

    private boolean validTile(int nextX, int nextY, int tileType, int shopType) {
        if (nextX >= 0 && nextX < maxWidth) {
            if (nextY >= 0 && nextY < maxHeight) {
                Tile tile = tiles.get(nextX).get(nextY);
                if (this.tileType == 0) {
                    if (tile.isTeleport()) {
                        return tile.isOpen();
                    } else if (tile.isShop() && shopType == 0) {
                        return tile.isOpen();
                    } else if (tile.isFactory() && shopType == 1) {
                        return tile.isOpen();
                    } else {
                        return tile.isOpen() &&
                                tile.getTileType() == this.tileType;
                    }
                } else if (this.tileType == 1) {
                    if (tile.isShop() && shopType == 0) {
                        return tile.isOpen();
                    } else if (tile.isFactory() && shopType == 1) {
                        return tile.isOpen();
                    } else {
                        return tile.isOpen() &&
                                tile.getTileType() == this.tileType;
                    }
                }
            }
        }
        return false;
    }
}