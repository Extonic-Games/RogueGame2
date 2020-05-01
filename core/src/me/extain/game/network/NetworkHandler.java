package me.extain.game.network;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import Server.ServerPlayer;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.network.Packets.HelloPacket;
import me.extain.game.network.Packets.HelloPacketACK;
import me.extain.game.network.Packets.JoinPacket;
import me.extain.game.network.Packets.MovePacket;
import me.extain.game.network.Packets.MovePacketACK;
import me.extain.game.network.Packets.NewPlayerPacket;
import me.extain.game.network.Packets.Packet;
import me.extain.game.network.Packets.PlayerDisconnected;
import me.extain.game.network.Packets.RequestObjects;
import me.extain.game.network.Packets.SendObjectsPacket;
import me.extain.game.network.Packets.ShootPacket;
import me.extain.game.network.Packets.UpdatePacket;

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
    }

}
