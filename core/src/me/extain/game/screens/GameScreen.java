package me.extain.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

import Server.ServerPlayer;
import io.socket.client.Socket;
import me.extain.game.Assets;
import me.extain.game.gameObject.GameObjectFactory;
import me.extain.game.gameObject.GameObjectManager;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.gameObject.item.Item;
import me.extain.game.gameObject.item.LootBagObject;
import me.extain.game.network.Packets.JoinPacket;
import me.extain.game.network.Packets.SendObjectsPacket;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.PlayerHUD;
import me.extain.game.gameObject.item.ItemFactory;
import me.extain.game.map.dungeonGen.DungeonMap;
import me.extain.game.map.tiled.TileMap;
import me.extain.game.map.tiled.TiledMapRenderer;

public class GameScreen implements Screen {

    private RogueGame context;

    private SpriteBatch batch;

    private TileMap tileMap;

    private Box2DHelper box2DHelper = new Box2DHelper();

    private DungeonMap dungeonMap;

    private PlayerHUD playerHUD;

    private TiledMapRenderer tiledMapRenderer;

    private Player player;

    public GameScreen(RogueGame context) {
        this.context = context;
    }

    private GameObjectManager gameObjectManager;

    @Override
    public void show() {

        batch = new SpriteBatch();

        box2DHelper.createWorld();

        ItemFactory.instantiate();
        GameObjectFactory.instantiate();

        gameObjectManager = new GameObjectManager();

        tileMap = new TileMap("map2.tmx");

        tiledMapRenderer = new TiledMapRenderer(tileMap, batch);

        player = new Player(RogueGame.getInstance().getAccount().getSelectedChar(), tileMap.getPlayerSpawn());
        player.setUsername(RogueGame.getInstance().getAccount().getUsername());

        tileMap.getGameObjectManager().addGameObject(player);

        context.getCamera().position.set(player.getPosition(), 0);

        JoinPacket joinPacket = new JoinPacket();

        ServerPlayer player2 = new ServerPlayer();

        player.setPosition(player.getPosition().x, player.getPosition().y);


        player2.setID(context.getClient().getID());
        player.setID(context.getClient().getID());


        player2.username = player.getUsername();

        player2.setPosition(player.getPosition().x, player.getPosition().y);

        joinPacket.player = player2;
        joinPacket.selectedChar = context.getAccount().getSelectedChar();
        context.getClient().sendTCP(joinPacket);

        if (playerHUD == null && Assets.getInstance().getAssets().isLoaded("skins/statusui/statusui.atlas")) {
            playerHUD = new PlayerHUD(context.getUICamera(), RogueGame.getInstance().getUiViewport(), player);
            if (player.getEquipItems().size() > 0) {

                for (Map.Entry<Integer, Item> entry : player.getEquipItems().entrySet()) {
                    if (entry.getValue() != null)
                        playerHUD.getInventoryUI().addItemToEquip(entry.getKey(), entry.getValue().getItemTypeID());
                }
            }
            if (player.getInventoryItems().size() > 0) {
                for (Map.Entry<Integer, Item> entry : player.getInventoryItems().entrySet()) {
                    if (entry.getValue() != null)
                        playerHUD.getInventoryUI().addEntityToInventory(entry.getKey(), entry.getValue().getItemTypeID());
                }
            }

            InputMultiplexer multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(playerHUD.getStage());
            Gdx.input.setInputProcessor(multiplexer);
        }
    }

    public void update(float delta) {
        box2DHelper.step();

            if (!playerHUD.isChatVis()) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.T)) playerHUD.showChat(true);
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) playerHUD.showPauseUI();
            } else {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) playerHUD.showChat(false);
            }

            if (tileMap != null)
                tileMap.update(delta);

            gameObjectManager.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        context.getCamera().update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (batch != null) {
            batch.setProjectionMatrix(context.getCamera().combined);

            if (tileMap.getGameObjectManager().getPlayer() != null)
                context.getCamera().position.lerp(new Vector3(tileMap.getGameObjectManager().getPlayer().getPosition(), 0), 0.05f);

            tiledMapRenderer.setView(context.getCamera());
            //tileMap.render();
            tiledMapRenderer.render();


            batch.begin();
            //tileMap.renderObjects(batch);
            //dungeonMap.render(batch);

            gameObjectManager.render(batch);

            for (HashMap.Entry<Integer, RemotePlayer> entry : RogueGame.getInstance().getOtherPlayers().entrySet()) {
                entry.getValue().render(batch);
            }

            //gameObjectManager.render(batch);

            batch.end();

            box2DHelper.render(context.getCamera());

            if (playerHUD != null)
                playerHUD.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (playerHUD != null)
            playerHUD.resize(width, height);
    }

    @Override
    public void pause() {
        playerHUD.pause();
    }

    @Override
    public void resume() {
        playerHUD.resume();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (batch != null)
            batch.dispose();
        if (tileMap != null)
            tileMap.dispose();
    }

    public PlayerHUD getPlayerHUD() {
        return playerHUD;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }
}
