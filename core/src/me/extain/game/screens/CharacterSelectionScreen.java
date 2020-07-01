package me.extain.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.Player.Account;
import me.extain.game.gameObject.Player.Character;
import me.extain.game.network.Packets.NewCharacterPacket;

import java.util.ArrayList;

public class CharacterSelectionScreen implements Screen {

    private Stage stage;

    private final RogueGame context;

    private Table charTable;

    public CharacterSelectionScreen(RogueGame context) {
        this.context = context;

        this.stage = new Stage(context.getUiViewport());

        charTable = new Table();

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (context.getAccount() != null) {

            if (context.getAccount().getCharacters().size() >= 0) {
                for (int i = 0; i < context.getAccount().getCharacters().size(); i++) {
                    TextButton button = new TextButton("Character: " + i, Assets.getInstance().getStatusSkin());
                    int finalI = i;
                    button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);

                            RogueGame.getInstance().getAccount().setSelectedChar(context.getAccount().getCharacters().get(finalI));
                            context.getScreenManager().changeScreen("Game");
                        }
                    });

                    if (i % 4 == 0) {
                        charTable.row();
                    }

                    charTable.add(button);
                }
            }

                TextButton button = new TextButton("New Character", Assets.getInstance().getStatusSkin());
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);

                        NewCharacterPacket packet = new NewCharacterPacket();
                        packet.accountID = RogueGame.getInstance().getAccount().getId();

                        RogueGame.getInstance().getClient().sendTCP(packet);

                        button.setTouchable(Touchable.disabled);
                    }
                });
                button.setPosition(50, 50);
                button.center();
                stage.addActor(button);
        }

        charTable.setPosition((Gdx.graphics.getWidth() / 2) - (charTable.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (charTable.getHeight() / 2));
        charTable.setVisible(true);
        charTable.validate();

        stage.addActor(charTable);
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

    }
}
