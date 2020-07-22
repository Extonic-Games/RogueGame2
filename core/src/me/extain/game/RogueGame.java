package me.extain.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.HashMap;

import me.extain.game.gameObject.Player.Account;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.network.NetworkHandler;
import me.extain.game.network.Packets.HelloPacket;
import me.extain.game.network.Packets.HelloPacketACK;
import me.extain.game.network.Packets.JoinPacket;
import me.extain.game.network.Packets.MovePacket;
import me.extain.game.network.Packets.MovePacketACK;
import me.extain.game.network.Packets.Packet;
import me.extain.game.network.Packets.SendObjectsPacket;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.screens.CharacterSelectionScreen;
import me.extain.game.screens.GameScreen;
import me.extain.game.screens.MainMenuScreen;
import me.extain.game.screens.ScreenManager;

public class RogueGame extends ApplicationAdapter {

	//private Sprite otherPlayer;

	private Assets assets;

	public static RogueGame instance = null;

	private ScreenManager screenManager;

	private OrthographicCamera camera, uiCamera;
	private Viewport viewPort, uiViewport;

	private Client client;

	private HashMap<Integer, RemotePlayer> otherPlayers;

	private ClientNetworkListener clientNetworkListener;

	private Account account;

	private boolean isCharSelected = false;

	public static RogueGame getInstance() {
		if (instance == null) return new RogueGame();
		else return instance;
	}

	@Override
	public void create () {
		instance = this;
		otherPlayers = new HashMap<>();

		client = new Client(1000000, 1000000);
		clientNetworkListener = new ClientNetworkListener(client);

		NetworkHandler.register(client);


		client.start();

		client.addListener(clientNetworkListener);

		try {
			//146.71.78.64 //localhost
			client.connect(5000, "localhost", NetworkHandler.port, 5055);
		} catch (IOException e) {
			e.printStackTrace();
		}


		assets = new Assets();

		camera = new OrthographicCamera(800, 600);
		viewPort = new ExtendViewport(800, 600);
		uiCamera = new OrthographicCamera(800,  600);
		uiViewport = new ExtendViewport(800, 600);

		camera.zoom = 0.2f;

		camera.setToOrtho(false, 800, 600);

		viewPort.setCamera(camera);
		uiViewport.setCamera(uiCamera);

		screenManager = new ScreenManager();
		screenManager.addScreen("Game", new GameScreen(this));
		screenManager.addScreen("MainMenu", new MainMenuScreen(this));
		screenManager.addScreen("CharacterSelection", new CharacterSelectionScreen(this));
		screenManager.changeScreen("MainMenu");
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public OrthographicCamera getUICamera() {
		return uiCamera;
	}

	public Viewport getViewPort() {
		return viewPort;
	}

	public Viewport getUiViewport() {
		return uiViewport;
	}

	public void show() {
		//screenManager.getCurrentScreen().show();
	}

	public void render(float delta) {
		screenManager.renderScreen(delta);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewPort.update(width, height);
		uiViewport.update(width, height);
	}

	public void update(float deltaTime) {
		camera.update();
		uiCamera.update();
		assets.update();
	}

	@Override
	public void render () {
		super.render();
		update(Gdx.graphics.getDeltaTime());
		render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose () {
		super.dispose();
		assets.dispose();
		screenManager.dispose();
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public Client getClient() {
		return client;
	}

	public HashMap<Integer, RemotePlayer> getOtherPlayers() {
		return otherPlayers;
	}

	public boolean isCharSelected() {
		return isCharSelected;
	}

	public void setIsCharSelected(boolean isChar) {
		this.isCharSelected = isChar;
	}
}
