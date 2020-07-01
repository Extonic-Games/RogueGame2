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
import me.extain.game.ui.LoginUI;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Table labelTable, buttonTable;
    private final RogueGame context;

    private final LoginUI loginUI;
    private final TextButton loginButton;

    private Label username;

    private boolean allowedPlay = false;

    public MainMenuScreen(final RogueGame context) {
        this.context = context;

        stage = new Stage(RogueGame.getInstance().getUiViewport());
        if (Gdx.input.getInputProcessor() != stage)
            Gdx.input.setInputProcessor(stage);

        labelTable = new Table();
        buttonTable = new Table();

        loginUI = new LoginUI();

        Label title = new Label("Rogue Game", Assets.getInstance().getStatusSkin());
        TextButton playButton = new TextButton("Play", Assets.getInstance().getStatusSkin());
        TextButton optionsButton = new TextButton("Options", Assets.getInstance().getStatusSkin());
        TextButton quitButton = new TextButton("Quit", Assets.getInstance().getStatusSkin());

        loginButton = new TextButton("Login", Assets.getInstance().getStatusSkin());
        loginButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                stage.addActor(loginUI);
            }
        });

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

        loginUI.setPosition((Gdx.graphics.getWidth() / 2) - (loginUI.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (loginUI.getHeight() / 2));
        loginUI.setMovable(true);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //if (context.getClient().isConnected())
                if (allowedPlay)
                    context.getScreenManager().changeScreen("CharacterSelection");
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

        stage.addActor(loginButton);
        stage.addActor(labelTable);
        stage.addActor(buttonTable);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (RogueGame.getInstance().getAccount() != null && username == null) {
            loginButton.setVisible(false);
            username = new Label("Logged in as: " + RogueGame.getInstance().getAccount().getUsername(), Assets.getInstance().getStatusSkin());
            username.setPosition((Gdx.graphics.getWidth() / 1.5f) - username.getText().length() * 2, Gdx.graphics.getHeight() - 50);
            username.pack();
            allowedPlay = true;
            stage.addActor(username);
        }

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
