package me.extain.game.network.Packets;

import java.util.HashMap;

import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;

public class SendObjectsPacket {

    public int id;
    public String name;
    public float x, y;
    public float health;
    public float damage;
    public float maxDamage;

}
