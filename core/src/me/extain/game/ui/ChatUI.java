package me.extain.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.network.Packets.MessagePacket;

import java.util.ArrayList;

public class ChatUI extends Window {

    private ArrayList<String> messages;
    private Table textArea;
    private ScrollPane scrollPane;
    private TextField textField;
    private Table container;


    public ChatUI() {
        super("Chat Window", Assets.getInstance().getStatusSkin(), "chatBox");

        setWidth(400);
        setHeight(280);

        textArea = new Table();
        container = new Table();

        scrollPane = new ScrollPane(textArea);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setFillParent(true);
        scrollPane.setFlickScroll(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFillParent(true);

        container.add(scrollPane).width(3500f).height(180f).pad(5f).right();
        container.row();

        container.pack();



        textField = new TextField("", Assets.getInstance().getStatusSkin(), "chatBox");
        textField.setWidth(350f);
        textField.setHeight(30f);

        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField chat, char key) {
                if (isVisible()) {
                    if (key == '\n' || key == '\r') {
                        if (textField.getText() != "") {
                            textArea.row();
                            StringBuilder message = new StringBuilder();
                            message.append(textField.getText());

                            textField.setText("");
                            MessagePacket packet = new MessagePacket();
                            packet.id = RogueGame.getInstance().getAccount().getId();
                            packet.username = RogueGame.getInstance().getAccount().getUsername();
                            packet.message = message.toString();

                            RogueGame.getInstance().getClient().sendUDP(packet);

                            getStage().unfocus(textField);
                        }
                    }
                }
            }
        });

        this.row();
        this.add(container).padTop(5f);
        this.row();
        this.add(textField);
        this.row();

    }


    public void addMessage(String message) {
        textArea.row();
        TextArea chatArea = new TextArea(message, Assets.getInstance().getStatusSkin(), "chatBox"); //Creates a chatArea with the message
        chatArea.setHeight(100);
        chatArea.setDisabled(true);
        textArea.add(chatArea).height(30).width(350).pad(2f).left();
    }
}
