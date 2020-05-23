package Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.HashMap;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.gameObject.Projectile.ProjectileFactory;
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

    private HashMap<Integer, Player> players;
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
            Player player2 = new Player(new Vector2(joinPacket.player.x, joinPacket.player.y));
            player2.setID(joinPacket.player.id);
            server.sendToAllExceptTCP(connection.getID(), newPlayerPacket);

            for (Player player : players.values()) {
                NewPlayerPacket packet2 = new NewPlayerPacket();
                ServerPlayer serverPlayer = new ServerPlayer();
                serverPlayer.setPosition(player.getPosition().x, player.getPosition().y);
                serverPlayer.setID(player.getID());
                packet2.serverPlayer = serverPlayer;
                connection.sendTCP(packet2);
            }

            players.put(connection.getID(), player2);
            RogueGameServer.getInstance().getServerWorld().gameObjectManager2().addGameObject(player2);

            for (GameObject gameObject : RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects()) {
                if (!(gameObject instanceof Projectile)) {
                    SendObjectsPacket sendObjectsPacket = new SendObjectsPacket();
                    sendObjectsPacket.id = gameObject.getID();
                    sendObjectsPacket.name = gameObject.getName();
                    sendObjectsPacket.health = gameObject.getHealth();
                    sendObjectsPacket.x = gameObject.getPosition().x;
                    sendObjectsPacket.y = gameObject.getPosition().y;
                    server.sendToUDP(joinPacket.player.id, sendObjectsPacket);
                }
            }
        }

        if (object instanceof MovePacket) {
            MovePacket movePacket = (MovePacket) object;
            players.get(connection.getID()).getBody().setTransform(movePacket.x, movePacket.y, 0f);
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
                if (!(gameObject instanceof Projectile)) {
                    SendObjectsPacket sendObjectsPacket = new SendObjectsPacket();
                    sendObjectsPacket.id = gameObject.getID();
                    sendObjectsPacket.name = gameObject.getName();
                    sendObjectsPacket.health = gameObject.getHealth();
                    sendObjectsPacket.x = gameObject.getPosition().x;
                    sendObjectsPacket.y = gameObject.getPosition().y;
                    server.sendToUDP(requestObjects.id, sendObjectsPacket);
                }
            }
        }

        if (object instanceof ShootPacket) {
            ShootPacket packet = (ShootPacket) object;
            Projectile projectile = ProjectileFactory.getInstance().getProjectile(packet.name, new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), packet.mask);
            RogueGameServer.getInstance().getServerWorld().getGameObjectManager().getGameObjects().add(projectile);
            server.sendToAllUDP(packet);
        }
    }

    @Override
    public void idle(Connection connection) {

    }
}
