package me.extain.game.map.tiled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Server;

import java.util.ArrayList;
import java.util.Vector;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.GameObjectFactory;
import me.extain.game.gameObject.GameObjectManager;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.screens.GameScreen;

public class TileMap {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private MapLayers mapLayers;

    private TileMapHelper tmHelper = new TileMapHelper();
    private TiledMapRenderer tiledMapRenderer;

    private Vector2 playerSpawn, slimeSpawn;

    private Array<Body> bodies;

    private GameObjectManager gameObjectManager;

    private GameObjectFactory factory;

    public TileMap(String tileMap) {
        this.map = new TmxMapLoader().load("maps/" + tileMap);
        this.mapLayers = map.getLayers();

        gameObjectManager = new GameObjectManager();
        factory = GameObjectFactory.instantiate();

        parseObjects();

        this.renderer = new OrthogonalTiledMapRenderer(map);

        bodies = tmHelper.buildShapes(map, 16);
    }

    public TileMap(String tileMapData, int fake) {

        FileHandle mapData = Gdx.files.external("RogueGame/mapData.tmx");
        mapData.writeString(tileMapData, false);
        this.map = new TmxMapLoader(new ExternalFileHandleResolver()).load("RogueGame/mapData.tmx");

        this.mapLayers = map.getLayers();

        gameObjectManager = new GameObjectManager();
        factory = GameObjectFactory.instantiate();

        parseObjects();

        this.renderer = new OrthogonalTiledMapRenderer(map);

        bodies = tmHelper.buildShapes(map, 16);
    }

    private void parseObjects() {
        MapObjects objects = mapLayers.get("Objects").getObjects();

        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (object.getName().equalsIgnoreCase("player_spawn")) {
                    playerSpawn = new Vector2(rectangle.x, rectangle.y);
                    //gameObjectManager.addGameObject(new Player(context, this, playerSpawn));
                    System.out.println("Found Player_Spawn at: " + rectangle.x + " , " + rectangle.y);
                }

                if (object.getName().equalsIgnoreCase("slime_spawn")) {
                    slimeSpawn = new Vector2(rectangle.x, rectangle.y);
                    gameObjectManager.addGameObject(factory.createObject("slime", slimeSpawn));
                    //System.out.println("Found slime spawn!");
                } else if (object.getName().equalsIgnoreCase("octo_spawn")) {
                    slimeSpawn = new Vector2(rectangle.x, rectangle.y);
                    gameObjectManager.addGameObject(factory.createObject("octo", slimeSpawn));
                    //System.out.println("Found slime spawn!");
                }
            }
        }
    }

    public void update(float deltaTime) {
        gameObjectManager.update(deltaTime);
    }

    public void updateServer(Server server, float deltaTime) {
        gameObjectManager.updateServer(server, deltaTime);
    }

    public void render() {
        this.renderer.setView(RogueGame.getInstance().getCamera());
        this.renderer.render();

    }

    public void renderObjects(SpriteBatch batch) {
        //gameObjectManager.render(batch);
    }

    public void dispose() {
        this.map.dispose();
        for (Body body : bodies) {
            Box2DHelper.getWorld().destroyBody(body);
        }
    }

    public TiledMap getMap() {
        return map;
    }

    public void setMap(TiledMap tileMap) {
        this.map = tileMap;
    }

    public OrthogonalTiledMapRenderer renderer() {
        return renderer;
    }

    public MapLayers getLayers() {
        return mapLayers;
    }

    public Vector2 getPlayerSpawn() {
        return playerSpawn;
    }

    public Vector2 getSlimeSpawn() {
        return slimeSpawn;
    }

    public Array<Body> getBodies() {
        return bodies;
    }

    public Player getPlayer() {
        return gameObjectManager.getPlayer();
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

}
