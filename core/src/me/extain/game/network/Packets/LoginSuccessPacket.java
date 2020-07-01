package me.extain.game.network.Packets;

import java.util.ArrayList;
import me.extain.game.gameObject.Player.Character;

public class LoginSuccessPacket {

    public int id;
    public String username;
    public ArrayList<Character> characters;

}
