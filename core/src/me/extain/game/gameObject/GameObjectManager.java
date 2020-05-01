package me.extain.game.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Server;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.network.Packets.SendObjectsPacket;
import me.extain.game.network.Packets.UpdatePacket;

public class GameObjectManager {

    public ArrayList<GameObject> objects;
    public ArrayList<GameObject> removeObjects;
    private ObjectComparator comparator = new ObjectComparator();

    public GameObjectManager() {
        objects = new ArrayList<GameObject>();
        removeObjects = new ArrayList<GameObject>();
    }

    public void update(float deltaTime) {
        objects.sort(comparator);

        for (GameObject object : removeObjects) {
            objects.remove(object);
            Box2DHelper.setBodyToDestroy(object.getBody());
            if (object.getEyesBody() != null)
                Box2DHelper.setBodyToDestroy(object.getEyesBody());
        }

        removeObjects.clear();

        for (GameObject object : objects) {
            if (object.isDestroy()) {
                removeObjects.add(object);
                object.clearProjectiles();
            } else {
                object.update(deltaTime);
            }
        }

    }

    public void updateServer(Server server, float deltaTime) {
        for (GameObject object : removeObjects) {
            objects.remove(object);
            Box2DHelper.setBodyToDestroy(object.getBody());

            if (object.isHasEyes())
                Box2DHelper.setBodyToDestroy(object.getEyesBody());
        }

        removeObjects.clear();

        for (GameObject object : objects) {
            if (object.isDestroy()) {
                removeObjects.add(object);
                //object.clearProjectiles();
                Gdx.app.log("Server", "Removing object: " + object.getName());
            } else {
                object.update(deltaTime);
                object.updateBehaviors(deltaTime);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (GameObject object : objects) {
            object.render(batch);
        }
    }

    public void addGameObject(GameObject object) {
        this.objects.add(object);
    }

    public Player getPlayer() {
        for (GameObject object : objects) {
            if (object instanceof Player) {
                return (Player) object;
            }
        }

        return null;
    }

    public void removeObject(String name) {
        for (GameObject object : objects) {
            if (object.getName() == name) {
                removeObjects.add(object);
            }
        }
    }

    public ArrayList<GameObject> getGameObjects() {
        return objects;
    }

    private class ObjectComparator implements Comparator<GameObject> {

        @Override
        public int compare(GameObject gameObject, GameObject t1) {
            return (t1.getPosition().y - gameObject.getPosition().y) > 0 ? 1 : -1;
        }
    }

}
