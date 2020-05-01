package Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.HashMap;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.gameObject.Projectile.TestProjectile;
import me.extain.game.network.Packets.HelloPacket;
import me.extain.game.network.Packets.HelloPacketACK;
import me.extain.game.network.Packets.JoinPacket;
import me.extain.game.network.Packets.MovePacket;
import me.extain.game.network.Packets.MovePacketACK;
import me.extain.game.network.Packets.NewPlayerPacket;
import me.extain.game.network.Packets.PlayerDisconnected;
import me.extain.game.network.Packets.RequestObjects;
import me.extain.game.network.Packets.SendObjectsPacket;
import me.extain.game.network.Packets.ShootPacket;

public class ServerNetworkListener extends Listener {

    private HashMap<Integer, ServerPlayer> players;
    private Server server;

    public ServerNetworkListener(Server server) {
        this.server = server;
        players = new HashMap<>();
    }

    public void connected(Connection c) {

    }

    public void disconnected(Connection c) {
        players.remove(c.getID());
        PlayerDisconnected playerDisconnected = new PlayerDisconnected();
        playerDisconnected.id = c.getID();
        server.sendToAllExceptTCP(c.getID(), playerDisconnected);
        System.out.println("Player: " + c.getID() + " has disconnected!");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof HelloPacket) {
            HelloPacket helloPacket = (HelloPacket) object;
            System.out.println(helloPacket.getMessage());

            HelloPacketACK helloPacketACK = new HelloPacketACK();
            connection.sendTCP(helloPacketACK);

            System.out.println("PlayerID: " + connection.getID());
        }

        if (object instanceof JoinPacket) {
            JoinPacket joinPacket = (JoinPacket) object;

            NewPlayerPacket newPlayerPacket = new NewPlayerPacket();
            newPlayerPacket.serverPlayer = joinPacket.player;
            server.sendToAllExceptTCP(connection.getID(), newPlayerPacket);

            for (ServerPlayer player : players.values()) {
                NewPlayerPacket packet2 = new NewPlayerPacket();
                packet2.serverPlayer = player;
                connection.sendTCP(packet2);
            }

            players.put(connection.getID(), joinPacket.player);

            for (GameObject gameObject : RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects()) {
                SendObjectsPacket sendObjectsPacket = new SendObjectsPacket();
                sendObjectsPacket.id = gameObject.getID();
                sendObjectsPacket.name = gameObject.getName();
                sendObjectsPacket.health = gameObject.getHealth();
                sendObjectsPacket.x = gameObject.getPosition().x;
                sendObjectsPacket.y = gameObject.getPosition().y;
                sendObjectsPacket.projectile = "test";
                server.sendToTCP(joinPacket.player.id, sendObjectsPacket);
            }
        }

        if (object instanceof MovePacket) {
            MovePacket movePacket = (MovePacket) object;
            players.get(connection.getID()).setPosition(movePacket.x, movePacket.y);
            //Gdx.app.log("Server", "Player has moved: " + movePacket.x + " , " + movePacket.y);

            movePacket.id = connection.getID();
            server.sendToAllExceptUDP(connection.getID(), movePacket);

 /*         MovePacketACK movePacketACK = new MovePacketACK();
            movePacketACK.x = movePacket.x;
            movePacketACK.y = movePacket.y;
            connection.sendTCP(movePacketACK); */
        }

        if (object instanceof RequestObjects) {
            RequestObjects requestObjects = (RequestObjects) object;

            for (GameObject gameObject : RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects()) {
                SendObjectsPacket sendObjectsPacket = new SendObjectsPacket();
                sendObjectsPacket.id = gameObject.getID();
                sendObjectsPacket.name = gameObject.getName();
                sendObjectsPacket.health = gameObject.getHealth();
                sendObjectsPacket.x = gameObject.getPosition().x;
                sendObjectsPacket.y = gameObject.getPosition().y;
                sendObjectsPacket.projectile = "test";
                server.sendToUDP(requestObjects.id, sendObjectsPacket);
            }
        }

        if (object instanceof ShootPacket) {
            ShootPacket packet = (ShootPacket) object;
            TestProjectile projectile = new TestProjectile(new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), Box2DHelper.BIT_PROJECTILES);
            RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects().add(projectile);
            server.sendToAllUDP(packet);
        }
    }

    @Override
    public void idle(Connection connection) {

    }
}
