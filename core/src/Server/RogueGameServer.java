package Server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;

import me.extain.game.gameObject.GameObjectFactory;
import me.extain.game.gameObject.GameObjectManager;
import me.extain.game.network.NetworkHandler;

public class RogueGameServer extends ApplicationAdapter {

    private Server server;

    private boolean isRunning = false;

    private ServerNetworkListener serverNetworkListener;

    private GameObjectManager gameObjectManager;

    private static RogueGameServer instance = null;

    private ServerWorld serverWorld;

    public static RogueGameServer getInstance() {
        if (instance == null) return instance = new RogueGameServer();
        else return instance;
    }

    @Override
    public void create() {
        isRunning = true;
        instance = this;
        gameObjectManager = new GameObjectManager();
        server = new Server();
        this.serverWorld = new ServerWorld(server);

        serverNetworkListener = new ServerNetworkListener(server);
        server.start();

        NetworkHandler.register(server);

        try {
            server.bind(NetworkHandler.port, 5055);
            System.out.println("Server is now running...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.addListener(serverNetworkListener);
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        serverWorld.render(Gdx.graphics.getDeltaTime());
    }

    public void update(float delta) {
        serverWorld.update();
    }

    public Server getServer() {
        return server;
    }

    public ServerWorld getServerWorld() {
        return serverWorld;
    }

 /*   public void run() {
        double ns = 1000000000.0 / 60.0;
        double delta = 0;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                update((float) delta);
                delta--;
            }
        }
    } */
}

