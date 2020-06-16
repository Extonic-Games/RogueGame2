package me.extain.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.network.AES;
import me.extain.game.network.Packets.LoginUserPacket;

public class LoginUI extends Window {


    /*   TODO: Add a callback if login was successful or not and display the message in the login window          */


    public LoginUI() {
        super("Account Login", Assets.getInstance().getDefaultSkin(), "default");


        setWidth(300);
        setHeight(300);

        Label usernameLabel = new Label("Username:", Assets.getInstance().getDefaultSkin());
        usernameLabel.setPosition(-20, 40);

        Label passLabel = new Label("Password:", Assets.getInstance().getDefaultSkin());
        passLabel.setPosition(-20, 55);

        TextField username = new TextField("", Assets.getInstance().getDefaultSkin());
        username.setMaxLength(10);
        username.setPosition(100, 50);
        username.setAlignment(Align.center);

        TextField password = new TextField("", Assets.getInstance().getDefaultSkin());
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        password.setAlignment(Align.center);
        password.setPosition(100, 60);

        TextButton loginButton = new TextButton("Login", Assets.getInstance().getDefaultSkin());
        loginButton.setPosition(-20, 90);
        loginButton.setWidth(10);
        loginButton.setHeight(10);

        TextButton closeButton = new TextButton("Close", Assets.getInstance().getDefaultSkin());
        closeButton.setPosition(-20, 90);
        closeButton.setWidth(10);
        closeButton.setHeight(10);

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                remove();
            }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Gdx.app.postRunnable(() -> {
                    LoginUserPacket packet = new LoginUserPacket();
                    packet.email = "test@test.com";
                    packet.id = 1;
                    packet.username = username.getText();
                    String pass = password.getText();
                    String enc = AES.encrypt(pass, "ssshh");
                    packet.password = enc;
                    System.out.println(enc);
                    RogueGame.getInstance().getClient().sendTCP(packet);
                });

                remove();
            }
        });

        this.add(usernameLabel);
        this.row();
        this.add(username);
        this.row();
        this.add(passLabel);
        this.row();
        this.add(password);
        this.row();
        this.row();
        this.add(closeButton);
        this.add(loginButton);
    }
}
