package me.extain.game.network.Packets;

import Server.ServerPlayer;
import me.extain.game.gameObject.Player.Character;
import me.extain.game.gameObject.Player.Player;

public class JoinPacket {

    public ServerPlayer player;
    public Character selectedChar;

}
