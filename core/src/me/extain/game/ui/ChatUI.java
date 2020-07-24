package me.extain.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.network.Packets.MessagePacket;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ChatUI extends Window {

    private ArrayList<String> messages;
    private Table textArea;
    private ScrollPane scrollPane;
    private TextField textField;
    private Table container;
    private Table table;


    public ChatUI() {
        super("", Assets.getInstance().getStatusSkin(), "chatBox");

        setWidth(400);
        setHeight(280);

        textArea = new Table();
        container = new Table();
        table = new Table();

        scrollPane = new ScrollPane(textArea);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(true,false);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setFillParent(true);
        scrollPane.setFlickScroll(true);
        scrollPane.setOverscroll(false, false);

        container.add(scrollPane).width(350f).height(180f).pad(1f).right();
        container.row();

        container.pack();

        table.add(container);



        textField = new TextField("", Assets.getInstance().getStatusSkin(), "chatBox");
        textField.setWidth(350f);
        textField.setHeight(20f);
        textField.setVisible(false);

        textField.setTextFieldFilter((textField, c) -> {

            if (c == '[' || c == ']') return false;

            return true;
        });

        textField.setTextFieldListener((chat, key) -> {
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
                        textField.setVisible(false);
                    }
                }
            }
        });

        table.row();
        table.add(textField).left();

        this.add(table);

    }

    public void showTextBox(boolean vis) {
        textField.setVisible(vis);
        if (vis)
            getStage().setKeyboardFocus(textField);
    }

    public boolean isTextBox() {
        return textField.isVisible();
    }


    public void addMessage(String message) {
        textArea.row();
        Label chatArea = new Label(message, Assets.getInstance().getStatusSkin(), "chatBox");
        chatArea.setHeight(100);
        chatArea.setWrap(true);
        textArea.add(chatArea).height(20).width(350).pad(1f).left();
        scrollPane.layout();
        scrollPane.scrollTo(0, 0, 0, 0);
    }
}
