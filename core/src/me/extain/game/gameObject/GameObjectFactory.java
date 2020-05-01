package me.extain.game.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;

import me.extain.game.Assets;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.Behaviors.Behaviors;
import me.extain.game.gameObject.Behaviors.ChaseBehavior;
import me.extain.game.gameObject.Behaviors.ShootBehavior;
import me.extain.game.map.tiled.TileMap;
import me.extain.game.screens.GameScreen;

public class GameObjectFactory {

    private static HashMap<String, GameObjectWrapper> gameObjects;
    //private ArrayList<GameObjectWrapper> objects;

    public static GameObjectFactory instantiate() {
        JsonValue json = new JsonReader().parse(Gdx.files.internal("entities/test.json").readString());
        //json.addClassTag("Wrapper", GameObjectWrapper.class);
        //json.setIgnoreUnknownFields(true);

        JsonValue objects = json.get("GameObjects");


        for (JsonValue object : objects.iterator()) {
            String projectile = null;
            ArrayList<String> behaviors = new ArrayList<String>();

            for (JsonValue behavior : object.get("Behaviors").iterator()) {
                behaviors.add(behavior.getString("name"));
                if (behavior.has("projectile"))
                    projectile = behavior.getString("projectile");
            }

            return new GameObjectFactory(object.getString("name"), object.getString("atlas"), object.getInt("health"), object.getString("type"), behaviors, projectile);
        }

        //GameObjectFactory factory = json.fromJson(GameObjectFactory.class, Gdx.files.internal("entities/test.json").readString());

        //System.out.println(json.fromJson(GameObjectFactory.class, Gdx.files.internal("entities/test.json").readString()));

        return null;
    }

    public GameObjectFactory(String name, String atlas, float health, String type, ArrayList<String> behaviors, String projectile) {
        GameObjectWrapper wrapper = new GameObjectWrapper();
        gameObjects = new HashMap<String, GameObjectWrapper>();

        //context.getAssetManager().load("entities/" + atlas, TextureAtlas.class);

        wrapper.name = name;
        if (Assets.getInstance().getAssets().isLoaded("entities/" + atlas))
            wrapper.atlas = Assets.getInstance().getAssets().get("entities/" + atlas);
        else {
            Assets.getInstance().getAssets().load("entities/" + atlas, TextureAtlas.class);
            Assets.getInstance().getAssets().finishLoading();
            wrapper.atlas = Assets.getInstance().getAssets().get("entities/" + atlas);
        }
        wrapper.health = health;
        wrapper.type = type;
        wrapper.behaviors = behaviors;

        if (projectile != null) wrapper.projectile = projectile;
        else wrapper.projectile = "Test";

        gameObjects.put(name, wrapper);
    }

    public static GameObject createObject(String name, Vector2 pos) {
        GameObjectWrapper wrapper = gameObjects.get(name);

        GameObject object = new GameObject(pos, Box2DHelper.createDynamicBodyCircle(pos, 4f, Box2DHelper.BIT_ENEMY));
        object.setAtlas(wrapper.atlas);
        object.setWalk(new Animation<TextureRegion>(0.4f, wrapper.atlas.findRegions("walk"), Animation.PlayMode.LOOP));
        object.setIdle(wrapper.atlas.findRegion("idle"));
        object.setHealth(wrapper.health);
        object.setObjectName(wrapper.name);
        object.setID(MathUtils.random(1000));

        for (int i = 0; i < wrapper.behaviors.size(); i++) {
            if (wrapper.behaviors.get(i).equalsIgnoreCase("chase")) {
                object.addBehavior(new ChaseBehavior(object));
            } else if (wrapper.behaviors.get(i).equalsIgnoreCase("shoot")) {
                ShootBehavior shootBehavior = new ShootBehavior(object);
                shootBehavior.setProjectile(wrapper.projectile);
                object.addBehavior(shootBehavior);
            }
        }
        object.createEyes();

        return object;
    }
}
