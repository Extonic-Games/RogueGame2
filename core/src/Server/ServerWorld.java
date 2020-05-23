package Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.esotericsoftware.kryonet.Server;

import java.util.HashMap;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.GameObjectManager;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.map.tiled.TileMap;
import me.extain.game.network.Packets.SendObjectsPacket;

public class ServerWorld {

    public TileMap tileMap;
    private Server server;
    private GameObjectManager gameObjectManager;
    private Box2DHelper box2DHelper;

    public ServerWorld(Server server) {
        this.server = server;
        gameObjectManager = new GameObjectManager();
        box2DHelper = new Box2DHelper();
        box2DHelper.createWorld();
        tileMap = new TileMap("map2.tmx");
        System.out.println(tileMap.getMap().toString());
    }

    public void update() {
        tileMap.updateServer(server, Gdx.graphics.getDeltaTime());

        gameObjectManager.updateServer(server, Gdx.graphics.getDeltaTime());

        box2DHelper.step();
    }

    public void render(float delta) {
    }

    public GameObjectManager getGameObjectManager() {
        return tileMap.getGameObjectManager();
    }

    public GameObjectManager gameObjectManager2() {
        return gameObjectManager;
    }

}
