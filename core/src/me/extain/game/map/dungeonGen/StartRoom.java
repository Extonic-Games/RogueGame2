package me.extain.game.map.dungeonGen;

import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.room.AbstractRoomGenerator;
import com.github.czyzby.noise4j.map.generator.room.RoomType;

public class StartRoom implements RoomType {

    private Grid gridObj;

    public StartRoom(Grid grid) {
        this.gridObj = grid;
    }

    private AbstractRoomGenerator.Room room;

    @Override
    public void carve(AbstractRoomGenerator.Room room, Grid grid, float value) {
        //DefaultRoomType.CASTLE.carve(room, grid, 1f);

        int topX = room.getX();
        int topY = room.getY();
        int tRightX = topX + room.getWidth();
        int RightY = topY;



        for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                grid.set(x, y, 0.5f);
            }
        }

        for (int x1 = 0; x1 < grid.getWidth(); x1++) {
            for (int y1 = 0; y1 < grid.getHeight(); y1++) {

                for (int x = topX; x < tRightX; x++) { // Top
                    if (grid.get(x, topY + room.getHeight()) != 0.9f)
                        gridObj.set(x, topY + room.getHeight(), 0.6f);
                }


                for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) { // Left
                    if (grid.get(room.getX() - 1, y) != 0.9f || grid.get(room.getX() - 1, y) != 0.6f)
                        gridObj.set(room.getX() - 1, y, 0.3f);
                }

                for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) { // Bottom
                    if (grid.get(x, room.getY() - 1) != 0.9f || grid.get(x, room.getY() - 1) != 0.6f)
                        gridObj.set(x, room.getY() - 1, 0.4f);
                }

                for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) { // Right
                    if (grid.get(room.getX() + room.getWidth(), y) != 0.9f || grid.get(room.getX() + room.getWidth(), y) != 0.6f)
                        gridObj.set((room.getX()) + room.getWidth(), y, 0.1f);

                    if (grid.get(room.getX() - 1, room.getY() - 1) != 0.9f) {
                        gridObj.set(room.getX() - 1, room.getY() - 1, 1f); // Bottom Left Corner
                    }

                    if (grid.get(room.getX() - 1, room.getY() + room.getHeight()) != 0.9f) {
                        gridObj.set(room.getX() - 1, room.getY() + room.getHeight(), 0.3f); // Top Left Corner
                    }

                    if (grid.get(room.getX() + room.getWidth() - 1, room.getY() - 1) != 0.9f) {
                        gridObj.set(room.getX() + room.getWidth(), room.getY() - 1, 1f); // Bottom Right corner
                    }

                    if (grid.get(room.getX() + room.getWidth(), room.getY() + room.getHeight()) != 0.9f) {
                        gridObj.set(room.getX() + room.getWidth(), room.getY() + room.getHeight(), 0.1f); // Top Right corner
                    }
                }
            }
        }
    }

    @Override
    public boolean isValid(AbstractRoomGenerator.Room room) {
        return true;
    }

    public AbstractRoomGenerator.Room getRoom() {
        return room;
    }
}
