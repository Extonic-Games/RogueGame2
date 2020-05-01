package me.extain.game.map.dungeonGen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.room.AbstractRoomGenerator;
import com.github.czyzby.noise4j.map.generator.room.AbstractRoomGenerator.Room;
import com.github.czyzby.noise4j.map.generator.room.RoomType;
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator;

import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObjectManager;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.map.BSP.Leaf;
import me.extain.game.map.Tile;
import me.extain.game.screens.GameScreen;

public class DungeonMap {

    public Tile[][] map, mapObj, map2;

    public int width, height;

    private Grid gridObj, grid2;



    public Leaf leaf;

    public GameObjectManager gameObjectManager;
    private GameScreen screen;

    private Pixmap pixmap;
    private Texture texture;

    public DungeonMap(GameScreen screen, int width, int height) {
        this.screen = screen;
        this.map = new Tile[(int) width][(int) height];
        this.mapObj = new Tile[width][height];
        this.map2 = new Tile[width][height];
        this.width = width;
        this.height = height;

        gridObj = new Grid(width, height);
        grid2 = new Grid(width, height);

        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        gameObjectManager = new GameObjectManager();

        //leaf = new Leaf(0, 0, width, height);
        //leaf.create();

        //placeRooms();

        test2();
    }

    private void test2() {
        final me.extain.game.map.dungeonGen.Grid grid = new me.extain.game.map.dungeonGen.Grid(width, height);

        final MyDungeonGen dungeonGen = new MyDungeonGen();
        dungeonGen.generate(grid);

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (grid.get(x, y) == 0.5f) {
                    map[x][y] = new Tile(Assets.getInstance().getAssets().get("tiles/dungeon-tiles2.atlas", TextureAtlas.class).findRegion("stone_floor"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.4f) {
                    map[x][y] = new Tile(Assets.getInstance().getAssets().get("tiles/dungeon-tiles2.atlas", TextureAtlas.class).findRegion("stone_bwall"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.3f) {
                    map[x][y] = new Tile(Assets.getInstance().getAssets().get("tiles/dungeon-tiles2.atlas", TextureAtlas.class).findRegion("stone_lwall"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.1f) {
                    map[x][y] = new Tile(Assets.getInstance().getAssets().get("tiles/dungeon-tiles2.atlas", TextureAtlas.class).findRegion("stone_rwall"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.6f) {
                    map[x][y] = new Tile(Assets.getInstance().getAssets().get("tiles/dungeon-tiles2.atlas", TextureAtlas.class).findRegion("stone_wall1"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.7f) {
                    map[x][y] = new Tile(Assets.getInstance().getAssets().get("tiles/dungeon-tiles2.atlas", TextureAtlas.class).findRegion("stone_lcorner"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.8f) {
                    map[x][y] = new Tile(Assets.getInstance().getAssets().get("tiles/dungeon-tiles2.atlas", TextureAtlas.class).findRegion("stone_rcorner"), new Vector2(x, y), false);
                }
            }
        }

        //gameObjectManager.addGameObject(new Player(new Vector2(dungeonGen.getPlayerX() * 16, dungeonGen.getPlayerY() * 16)));

    }


 /*   private void test() {

        final Grid grid = new Grid(width, height);

        final DungeonGenerator dungeonGenerator = new DungeonGenerator();
        //dungeonGenerator.setRoomGenerationAttempts(200);
        dungeonGenerator.setMaxRoomSize(9);
        dungeonGenerator.setTolerance(2);
        dungeonGenerator.setMinRoomSize(5);
        dungeonGenerator.setWindingChance(-1f);
        dungeonGenerator.setCorridorThreshold(0.9f);
        dungeonGenerator.setRandomConnectorChance(0f);
        dungeonGenerator.addRoomType(new StartRoom(grid2));
        dungeonGenerator.generate(grid);

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (grid.get(x, y) == 0.9f && grid.get(x - 1, y) == 1f) { // left of corridor
                    gridObj.set(x - 1, y, 0.3f);
                }
                if (grid.get(x, y) == 0.9f && grid.get(x + 1, y) == 1f) { // Right of corridor
                    gridObj.set(x + 1, y, 0.1f);
                }

                if (grid.get(x, y) == 0.9f && grid.get(x, y - 1) == 1f) { // Bottom of corridor
                    gridObj.set(x, y - 1, 0.4f);
                }

                if (grid.get(x, y) == 0.9f && grid.get(x, y + 1) == 1f) { // Top of corridor
                    gridObj.set(x, y + 1, 0.6f);
                }
            }
        }

        System.out.println(dungeonGenerator.getRoomTypes());

        final Color color = new Color();
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                final float cell = 1 - grid.get(x,y);
                color.set(cell, cell, cell, 1f);
                pixmap.drawPixel(x, y, Color.rgba8888(color));
                if (grid.get(x, y) == 0.5) {
                    map[x][y] = new Tile(Tile.ground, new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.9f) {
                    map[x][y] = new Tile(Tile.ground, new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.3f) {
                    map[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("right-wall"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.4f) {
                    map[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("bottom-wall"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.1f) {
                    map[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("left-wall"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.6f) {
                    map[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("wall-stone"), new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 1) {
                    //map[x][y] = new Tile(Tile.wall, new Vector2(x, y), false);
                }

                if (grid.get(x, y) == 0.2f) {
                    map[x][y] = new Tile(Tile.ground1, new Vector2(x, y), false);
                }
            }
        }

        for (int x = 0; x < gridObj.getWidth(); x++) {
            for (int y = 0; y < gridObj.getHeight(); y++) {
                if (grid.get(x, y) != 0.9f) {
                        if (gridObj.get(x, y) == 0.3f) {
                            mapObj[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("right-wall"), new Vector2(x, y), false);
                        }

                        if (gridObj.get(x, y) == 0.4f) {
                            mapObj[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("bottom-wall"), new Vector2(x, y), false);
                        }

                        if (gridObj.get(x, y) == 0.1f) {
                            mapObj[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("left-wall"), new Vector2(x, y), false);
                        }

                        if (gridObj.get(x, y) == 0.6f) {
                            mapObj[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("wall-stone"), new Vector2(x, y), false);
                        }
                } else {
                    gridObj.set(x, y, 0f);
                }
            }
        }

        for (int x = 0; x < grid2.getWidth(); x++) {
            for (int y = 0; y < grid2.getHeight(); y++) {
                if (grid.get(x, y) != 0.9f) {
                    if (grid2.get(x, y) == 0.3f) {
                        map2[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("right-wall"), new Vector2(x, y), true);
                    }

                    if (grid2.get(x, y) == 0.4f) {
                        map2[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("bottom-wall"), new Vector2(x, y), true);
                    }

                    if (grid2.get(x, y) == 0.1f) {
                        map2[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("left-wall"), new Vector2(x, y), true);
                    }

                    if (grid2.get(x, y) == 0.6f) {
                        map2[x][y] = new Tile(context.getAssetManager().get("tiles/dungeon_tiles.atlas", TextureAtlas.class).findRegion("wall-stone"), new Vector2(x, y), true);
                    }
                }else {
                    grid2.set(x, y, 0f);
                }
            }
        }

        texture = new Texture(pixmap);
        pixmap.dispose();

        gameObjectManager.addGameObject(new Player(context, null, new Vector2(10, 10)));

    } */

    public void update(float deltaTime) {
        gameObjectManager.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = map[x][y];
                //Tile tile2 = mapObj[x][y];
                //Tile tile3 = map2[x][y];
                if (tile != null)
                    batch.draw(tile.getTileTexture(), x * 16, y * 16);
/*                if (tile2 != null)
                    batch.draw(tile2.getTileTexture(), x * 16, y * 16);
                if (tile3 != null)
                    batch.draw(tile3.getTileTexture(), x * 16, y * 16); */
            }
        }

        //batch.draw(texture, 10f, 10f);

        gameObjectManager.render(batch);
    }

    public Player getPlayer() {
        return gameObjectManager.getPlayer();
    }

}
