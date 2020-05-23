package me.extain.game.gameObject.Behaviors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import Server.RogueGameServer;
import me.extain.game.gameObject.GameObject;
import me.extain.game.network.Packets.UpdatePacket;

public class ChaseBehavior implements Behaviors {

    public GameObject object, target;
    public Vector2 velocity;

    public ChaseBehavior(GameObject object) {
        this.object = object;

        velocity = new Vector2();
    }

    @Override
    public void update(float deltaTime) {
        if (target != null) {
            velocity.x = (target.getPosition().x - object.getPosition().x + object.getSpeed()) * deltaTime;
            velocity.y = (target.getPosition().y - object.getPosition().y + object.getSpeed()) * deltaTime;

            object.move(velocity);

            UpdatePacket packet = new UpdatePacket();
            packet.name = object.getName();
            packet.id = object.getID();
            packet.x = object.getPosition().x;
            packet.y = object.getPosition().y;
            packet.health = object.getHealth();
            RogueGameServer.getInstance().getServer().sendToAllUDP(packet);

            //System.out.println("Sent object move packet");
        }

    }

    @Override
    public void setTarget(GameObject object) {
        this.target = object;
    }

    public GameObject getObject() {
        return this.object;
    }
}
