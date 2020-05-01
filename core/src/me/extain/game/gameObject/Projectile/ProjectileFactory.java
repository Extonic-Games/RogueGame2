package me.extain.game.gameObject.Projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Hashtable;

import me.extain.game.Assets;
import me.extain.game.RogueGame;
import me.extain.game.screens.GameScreen;

public class ProjectileFactory {

    private Hashtable<String, ProjectileWrapper> projectiles;

    private final String ProjectileScript = "projectiles/projectiles.json";
    private static ProjectileFactory instance = null;

    public static ProjectileFactory getInstance() {
        if (instance == null) instance = new ProjectileFactory();

        return instance;
    }

    public ProjectileFactory() {
        Json json = new Json();
        ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(ProjectileScript));
        projectiles = new Hashtable<>();

        for (JsonValue jsonValue : list) {
            ProjectileWrapper wrapper = new ProjectileWrapper();
            wrapper.name = jsonValue.getString("name");
            wrapper.damage = jsonValue.getInt("damage");
            wrapper.maxDamage = jsonValue.getInt("maxDamage");
            wrapper.lifeSpan = jsonValue.getInt("lifespan");
            wrapper.atlas = Assets.getInstance().getAssets().get("projectiles/" + jsonValue.getString("atlas"));

            projectiles.put(wrapper.name, wrapper);
        }
    }

    public Projectile getProjectile(String name, Vector2 position, Vector2 velocity, Body body) {
        ProjectileWrapper wrapper = projectiles.get(name);

        Projectile projectile = new Projectile(position, body);
        projectile.setObjectName(wrapper.name);
        projectile.setMinDamage(wrapper.damage);
        projectile.setMaxDamage(wrapper.maxDamage);
        projectile.setLifeSpan(wrapper.lifeSpan);
        projectile.setTexture(wrapper.atlas.findRegion(name).getTexture());
        projectile.setVelocity(velocity);
        projectile.createSprite();

        return projectile;
    }



}
