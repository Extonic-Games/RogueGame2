package me.extain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.json.Test;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.GameObjectFactory;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.gameObject.Projectile.ProjectileFactory;
import me.extain.game.gameObject.Projectile.TestProjectile;
import me.extain.game.network.Packets.HelloPacket;
import me.extain.game.network.Packets.HelloPacketACK;
import me.extain.game.network.Packets.MovePacket;
import me.extain.game.network.Packets.MovePacketACK;
import me.extain.game.network.Packets.NewPlayerPacket;
import me.extain.game.network.Packets.PlayerDisconnected;
import me.extain.game.network.Packets.SendObjectsPacket;
import me.extain.game.network.Packets.ShootPacket;
import me.extain.game.network.Packets.UpdatePacket;
import me.extain.game.screens.GameScreen;

public class ClientNetworkListener extends Listener {

    private Client client;

    public ClientNetworkListener(Client client) {
        this.client = client;
    }

    public void connected(Connection connection) {
        HelloPacket packet = new HelloPacket();
        //packet.setMessage("Hello there!");
        client.sendTCP(packet);
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof HelloPacketACK) {
            HelloPacketACK ack = (HelloPacketACK) object;
            Gdx.app.log("Client", ack.getMessage());
        }

        if (object instanceof MovePacket) {
            MovePacket ack = (MovePacket) object;
            if (RogueGame.getInstance().getOtherPlayers().get(ack.id) != null) {
                RogueGame.getInstance().getOtherPlayers().get(ack.id).setPosition(ack.x, ack.y);
            } else {
                if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                    RemotePlayer remotePlayer = new RemotePlayer(new Vector2(ack.x, ack.y));
                    RogueGame.getInstance().getOtherPlayers().put(ack.id, remotePlayer).setPosition(ack.x, ack.y);
                }
            }
        }

        if (object instanceof NewPlayerPacket) {
            NewPlayerPacket newPlayer = (NewPlayerPacket) object;
            if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();
                RemotePlayer remotePlayer = new RemotePlayer(new Vector2(newPlayer.serverPlayer.x, newPlayer.serverPlayer.y));
                RogueGame.getInstance().getOtherPlayers().put(newPlayer.serverPlayer.id, remotePlayer);
            }
        }

        if (object instanceof PlayerDisconnected) {
            PlayerDisconnected playerDisconnected = (PlayerDisconnected) object;
            Box2DHelper.setBodyToDestroy(RogueGame.getInstance().getOtherPlayers().get(playerDisconnected.id).getBody());
            RogueGame.getInstance().getOtherPlayers().remove(playerDisconnected.id);
        }

        if (object instanceof SendObjectsPacket) {
            SendObjectsPacket packet = (SendObjectsPacket) object;
            if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();
                GameObject gameObject = GameObjectFactory.createObject(packet.name, new Vector2(packet.x, packet.y));
                gameScreen.getTileMap().getGameObjectManager().addGameObject(gameObject);
            }
        }

        if (object instanceof UpdatePacket) {
            UpdatePacket packet = (UpdatePacket) object;
            if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();

                //System.out.println(packet.id);
                    if (gameScreen.getTileMap() != null) {
                        for (GameObject objects : gameScreen.getTileMap().getGameObjectManager().getGameObjects()) {
                            if (objects.getByID(packet.id) != null) {
                                objects.getByID(packet.id).setHealth(packet.health);
                                objects.getByID(packet.id).setPosition(packet.x, packet.y);
                                packet = null;
                            }
                        }
                    }
            }
        }

        if (object instanceof ShootPacket) {
            ShootPacket packet = (ShootPacket) object;
            Projectile projectile = ProjectileFactory.getInstance().getProjectile(packet.name, new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), Box2DHelper.BIT_PROJECTILES);
             if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();
                gameScreen.getGameObjectManager().addGameObject(projectile);
                //System.out.println("Received shootpacket!");
            }
        }
    }
}
