package me.extain.game;

import Server.RogueGameServer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import me.extain.game.gameObject.Player.Account;
import me.extain.game.gameObject.Player.Character;
import me.extain.game.gameObject.Projectile.SwordSlash;
import me.extain.game.gameObject.item.LootBagObject;
import me.extain.game.network.Packets.*;
import org.json.Test;

import java.util.concurrent.CopyOnWriteArrayList;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.GameObjectFactory;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.gameObject.Projectile.ProjectileFactory;
import me.extain.game.gameObject.Projectile.TestProjectile;
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
        Gdx.app.postRunnable(() -> {
            if (object instanceof HelloPacketACK) {
                HelloPacketACK ack = (HelloPacketACK) object;
                Gdx.app.log("Client", ack.getMessage());
            }

            if (object instanceof MovePacket) {
                final MovePacket ack = (MovePacket) object;
                if (RogueGame.getInstance().getOtherPlayers().get(ack.id) != null) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            RogueGame.getInstance().getOtherPlayers().get(ack.id).getBody().setTransform(ack.x, ack.y, 0f);
                        }
                    });
                }
            }

            if (object instanceof NewPlayerPacket) {
                NewPlayerPacket newPlayer = (NewPlayerPacket) object;
                if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                    GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();
                    RemotePlayer remotePlayer = new RemotePlayer(new Vector2(newPlayer.serverPlayer.x, newPlayer.serverPlayer.y));
                    remotePlayer.setUsername(newPlayer.serverPlayer.username);
                    RogueGame.getInstance().getOtherPlayers().put(newPlayer.serverPlayer.id, remotePlayer);
                }
            }

            if (object instanceof PlayerDisconnected) {
                PlayerDisconnected playerDisconnected = (PlayerDisconnected) object;
                Box2DHelper.getWorld().destroyBody(RogueGame.getInstance().getOtherPlayers().get(playerDisconnected.id).getBody());
                RogueGame.getInstance().getOtherPlayers().remove(playerDisconnected.id);
            }

            if (object instanceof SendObjectsPacket) {
                SendObjectsPacket packet = (SendObjectsPacket) object;
                if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                    GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();
                    GameObject gameObject = GameObjectFactory.createObject(packet.name, new Vector2(packet.x, packet.y));
                    gameObject.setID(packet.id);
                    gameScreen.getTileMap().getGameObjectManager().addGameObject(gameObject);
                }
            }

            if (object instanceof UpdatePacket) {
                final UpdatePacket packet = (UpdatePacket) object;
                GameScreen gameScreen = null;

                if (RogueGame.getInstance().getScreenManager() != null && RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen)
                    gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();

                //System.out.println(packet.id);
                if (gameScreen != null && gameScreen.getTileMap() != null) {
                    for (final GameObject objects : gameScreen.getTileMap().getGameObjectManager().getGameObjects()) {
                        //System.out.println(packet.id);
                        Gdx.app.postRunnable(() -> {
                            if(objects.getID() == packet.id) {
                                // Lerp server position with client position. Removes jitteriness
                                objects.getBody().setTransform(objects.getPosition().lerp(new Vector2(packet.x, packet.y), 0.1f), 0);
                                objects.setHealth(packet.health);

                                if (objects.getHealth() <= 0) objects.setDestroy(true);
                            }
                        });

                    }
                }
            }

            if (object instanceof ShootPacket) {
                ShootPacket packet = (ShootPacket) object;

                if (packet.isSlash) {
                    SwordSlash slash = ProjectileFactory.getInstance().getSlash(packet.name, new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), packet.mask);
                    slash.setDamageRange(packet.damage);
                    slash.setLifeSpan(packet.lifeSpan);

                    if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                        GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();

                        if (!Box2DHelper.getWorld().isLocked())
                            gameScreen.getGameObjectManager().addGameObject(slash);
                        //System.out.println("Received shootpacket!");
                    }
                } else {
                    Projectile projectile = ProjectileFactory.getInstance().getProjectile(packet.name, new Vector2(packet.x, packet.y), new Vector2(packet.velX, packet.velY), packet.mask);
                    projectile.setDamageRange(packet.damage);
                    projectile.setLifeSpan(packet.lifeSpan);
                    if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                        GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();

                        if (!Box2DHelper.getWorld().isLocked())
                            gameScreen.getGameObjectManager().addGameObject(projectile);
                        //System.out.println("Received shootpacket!");
                    }
                }
            }

            if (object instanceof LootDropPacket) {
                LootDropPacket packet = (LootDropPacket) object;
                LootBagObject lootBag = new LootBagObject(new Vector2(packet.x, packet.y));
                lootBag.setID(packet.id);
                lootBag.addItems(packet.items);

                if (RogueGame.getInstance().getScreenManager().getCurrentScreen() instanceof GameScreen) {
                    GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();

                    gameScreen.getTileMap().getGameObjectManager().addGameObject(lootBag);
                }
        }

            if (object instanceof LoginSuccessPacket) {
                LoginSuccessPacket packet = (LoginSuccessPacket) object;
                Account account = new Account(packet.id, packet.username);
                account.setCharacters(packet.characters);

                for (Character character : packet.characters) System.out.println(character.getId() + ": " + character.getEquipItems().entrySet() + ", " + character.getInventoryItems().entrySet());

                RogueGame.getInstance().setAccount(account);
                System.out.println("Login Success!");
            }

            if (object instanceof NewCharacterAckPacket) {
                System.out.println("Character creation successful!");
                NewCharacterAckPacket packet = (NewCharacterAckPacket) object;
                RogueGame.getInstance().getAccount().addCharacter(packet.character);
                RogueGame.getInstance().getAccount().setSelectedChar(packet.character);
                RogueGame.getInstance().getScreenManager().changeScreen("Game");
            }

            if (object instanceof MessagePacket) {
                MessagePacket packet = (MessagePacket) object;
                ((GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen()).getPlayerHUD().addChatMessage(packet.username, packet.message);
            }

            if (object instanceof InventoryUpdatePacket) {
                InventoryUpdatePacket packet = (InventoryUpdatePacket) object;

                GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();

                if (gameScreen != null) {
                    if (packet.isAdded) {
                        System.out.println("Adding item: " + packet.slotID + ", " + packet.itemName);
                        gameScreen.getPlayerHUD().inventoryUI.addEntityToInventory(packet.slotID, packet.itemName);
                    }
                }
            }

            if (object instanceof PlayerStatsPacket) {
                PlayerStatsPacket packet = (PlayerStatsPacket) object;

                GameScreen gameScreen = (GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen();

                Player player = gameScreen.getTileMap().getPlayer();
                player.getPlayerStats().setLevel(packet.level);
                player.getPlayerStats().setXp(packet.xp);
                player.getPlayerStats().setAttack(packet.attack);

                System.out.println(player.getPlayerStats().getLevel());
                System.out.println(player.getPlayerStats().getXp());
            }
    });
    }
}
