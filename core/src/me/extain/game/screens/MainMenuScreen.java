package me.extain.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.network.AES;
import me.extain.game.network.Packets.LoginUserPacket;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Table labelTable, buttonTable;
    private final RogueGame context;



    public MainMenuScreen(final RogueGame context) {
        this.context = context;

        stage = new Stage(RogueGame.getInstance().getUiViewport());
        Gdx.input.setInputProcessor(stage);

        labelTable = new Table();
        buttonTable = new Table();

        Label title = new Label("Rogue Game", Assets.getInstance().getDefaultSkin(), "default");
        TextButton playButton = new TextButton("Play", Assets.getInstance().getDefaultSkin(), "default");
        TextButton optionsButton = new TextButton("Options", Assets.getInstance().getDefaultSkin(), "default");
        TextButton quitButton = new TextButton("Quit", Assets.getInstance().getDefaultSkin(), "default");

        //button.align(Align.center);

        labelTable.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 100);
        buttonTable.setSize(400, 400);
        buttonTable.setPosition(Gdx.graphics.getWidth() - 600, Gdx.graphics.getHeight() - 500);

        //buttonTable.debug();
        //button.setPosition(Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() / 2);

        labelTable.add(title).align(Align.top);
        buttonTable.add(playButton).align(Align.top).width(200).height(50).padBottom(10).colspan(2).center();
        buttonTable.row();
        buttonTable.add(optionsButton).align(Align.top).width(200).height(50).padBottom(10).colspan(2).center();
        buttonTable.row();
        buttonTable.add(quitButton).align(Align.top).width(200).height(50).padBottom(10).colspan(2).center();

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //if (context.getClient().isConnected())
                    context.getScreenManager().changeScreen("Game");
                //else Gdx.app.log("Client", "You are not connected to the server!");
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });

        stage.addActor(labelTable);
        stage.addActor(buttonTable);
    }

    @Override
    public void show() {
        Gdx.app.postRunnable(() -> {
            LoginUserPacket packet = new LoginUserPacket();
            packet.email = "test@test.com";
            packet.id = 1;
            packet.username = "Admin";
            String password = "AdminPass";
            String enc = AES.encrypt(password, "ssshh");
            packet.password = enc;
            System.out.println(enc);
            RogueGame.getInstance().getClient().sendTCP(packet);
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
