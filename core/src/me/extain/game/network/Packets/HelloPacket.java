package me.extain.game.network.Packets;

public class HelloPacket extends Packet {

    private String message;

    public HelloPacket() {
        this.setPacketID(1);
        this.message = "Hello Server!";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String string) {
        this.message = message;
    }

}
