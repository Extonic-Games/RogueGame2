package me.extain.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.network.AES;
import me.extain.game.network.Packets.LoginUserPacket;

public class PauseUI extends Window {


    public PauseUI() {
        super("Pause Menu", Assets.getInstance().getStatusSkin(), "default");

        setWidth(300);
        setHeight(300);

        TextButton resumeButton = new TextButton("Resume", Assets.getInstance().getStatusSkin());
        resumeButton.setPosition(-20, 90);

        TextButton quitButton = new TextButton("Quit", Assets.getInstance().getStatusSkin());
        quitButton.setPosition(-20, 90);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Gdx.app.exit();
            }
        });

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                setVisible(false);
            }
        });

        this.row();
        this.add(resumeButton);
        this.row();
        this.add(quitButton);

    }
}
