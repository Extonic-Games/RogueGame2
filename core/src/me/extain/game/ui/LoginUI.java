package me.extain.game.ui;

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
        super("Account Login", Assets.getInstance().getStatusSkin(), "default");


        Table buttonTable = new Table();

        setWidth(300);
        setHeight(300);

        Label usernameLabel = new Label("Username:", Assets.getInstance().getStatusSkin());
        usernameLabel.setPosition(-20, 40);

        Label passLabel = new Label("Password:", Assets.getInstance().getStatusSkin());
        passLabel.setPosition(-20, 55);

        TextField username = new TextField("", Assets.getInstance().getStatusSkin());
        username.setMaxLength(10);
        username.setPosition(100, 50);
        username.setAlignment(Align.center);

        TextField password = new TextField("", Assets.getInstance().getStatusSkin());
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        password.setAlignment(Align.center);
        password.setPosition(100, 60);

        TextButton loginButton = new TextButton("Login", Assets.getInstance().getStatusSkin());

        TextButton closeButton = new TextButton("Close", Assets.getInstance().getStatusSkin());


        buttonTable.add(closeButton).center();
        buttonTable.add(loginButton).center();

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
                    packet.id = RogueGame.getInstance().getClient().getID();
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

        this.add(usernameLabel).center();
        this.row();
        this.add(username).center();
        this.row();
        this.add(passLabel).center();
        this.row();
        this.add(password).center();
        this.row();
        this.row();
        this.add(buttonTable);
    }
}
