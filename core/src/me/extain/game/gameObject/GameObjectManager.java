package me.extain.game.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Server;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import me.extain.game.Assets;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Player.RemotePlayer;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.network.Packets.SendObjectsPacket;
import me.extain.game.network.Packets.UpdatePacket;

public class GameObjectManager {

    public CopyOnWriteArrayList<GameObject> objects;
    public ArrayList<GameObject> removeObjects;
    private ObjectComparator comparator = new ObjectComparator();

    public GameObjectManager() {
        objects = new CopyOnWriteArrayList<>();
        removeObjects = new ArrayList<>();
    }

    public void update(float deltaTime) {
        objects.sort(comparator);

        for (final GameObject object : removeObjects) {
            objects.remove(object);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (object.getBody() != null)
                        Box2DHelper.getWorld().destroyBody(object.getBody());
                    if (object.getEyesBody() != null)
                        Box2DHelper.getWorld().destroyBody(object.getEyesBody());
                }
            });

        }

        removeObjects.clear();

        for (GameObject object : objects) {
            if (object.isDestroy()) {
                removeObjects.add(object);
            } else {
                object.update(deltaTime);
            }
        }

    }

    public void updateServer(Server server, float deltaTime) {
        for (GameObject object : removeObjects) {
            objects.remove(object);

            if (Box2DHelper.getWorld() != null && !Box2DHelper.getWorld().isLocked()) {
                Box2DHelper.getWorld().destroyBody(object.getBody());

                if (object.isHasEyes())
                    Box2DHelper.getWorld().destroyBody(object.getEyesBody());
            }
        }

        removeObjects.clear();

        for (GameObject object : objects) {
            if (object.isDestroy()) {
                removeObjects.add(object);
            } else {
                if (!(object instanceof Projectile))
                object.updateServer(deltaTime);
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

    public CopyOnWriteArrayList<GameObject> getGameObjects() {
        return objects;
    }

    private class ObjectComparator implements Comparator<GameObject> {

        @Override
        public int compare(GameObject gameObject, GameObject t1) {
            return (t1.getPosition().y - gameObject.getPosition().y) > 0 ? 1 : -1;
        }
    }

}
