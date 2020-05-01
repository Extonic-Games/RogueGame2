package Server;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import me.extain.game.Physics.Box2DHelper;

public class ServerPlayer {

    public int id;
    public float x, y;

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

}
