package me.extain.game.network;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Scaling;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import Server.ServerPlayer;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.gameObject.item.Item;
import me.extain.game.gameObject.item.WeaponStats;
import me.extain.game.network.Packets.*;
import me.extain.game.gameObject.Player.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class NetworkHandler {

    static public final int port = 5045;

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Packet.class);
        kryo.register(HelloPacket.class);
        kryo.register(HelloPacketACK.class);
        kryo.register(NewPlayerPacket.class);
        kryo.register(SendObjectsPacket.class);
        kryo.register(RequestObjects.class);
        kryo.register(UpdatePacket.class);
        kryo.register(ShootPacket.class);
        kryo.register(ServerPlayer.class);
        kryo.register(JoinPacket.class);
        kryo.register(MovePacket.class);
        kryo.register(MovePacketACK.class);
        kryo.register(PlayerDisconnected.class);
        kryo.register(MessagePacket.class);
        kryo.register(ArrayList.class);
        kryo.register(LootDropPacket.class);
        kryo.register(LoginUserPacket.class);
        kryo.register(LoginSuccessPacket.class);
        kryo.register(Character.class);
        kryo.register(ArrayList.class);
        kryo.register(Item.class);
        kryo.register(NewCharacterPacket.class);
        kryo.register(NewCharacterAckPacket.class);
        kryo.register(Array.class);
        kryo.register(Object[].class);
        kryo.register(InventoryUpdatePacket.class);
        kryo.register(HashMap.class);
        kryo.register(DelayedRemovalArray.class);
        kryo.register(IntArray.class);
        kryo.register(int[].class);
        kryo.register(Color.class);
        kryo.register(Scaling.class);
        kryo.register(Touchable.class);
        kryo.register(WeaponStats.class);
    }

}
