package me.extain.game.gameObject.Projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Hashtable;

import me.extain.game.Assets;
import me.extain.game.Physics.Box2DHelper;
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
            ProjectileWrapper wrapper = json.readValue(ProjectileWrapper.class, jsonValue);

            projectiles.put(wrapper.name, wrapper);
        }
    }

    public Projectile getProjectile(String name, Vector2 position, Vector2 velocity, short mask) {
        ProjectileWrapper wrapper = projectiles.get(name);

        if (wrapper != null) {
            Projectile projectile = new Projectile(position, Box2DHelper.createDynamicBodyCircle(position, 2.5f, mask));
            projectile.setObjectName(wrapper.name);
            projectile.setMinDamage(wrapper.damage);
            projectile.setMaxDamage(wrapper.maxDamage);
            projectile.setLifeSpan(wrapper.lifespan);
            TextureAtlas atlas = Assets.getInstance().getAssets().get("projectiles/" + wrapper.atlas);
            projectile.setTexture(atlas.findRegion(name));
            projectile.setVelocity(velocity);

            projectile.createSprite();

            return projectile;
        } else {
            wrapper = projectiles.get("Test");

            Projectile projectile = new Projectile(position, Box2DHelper.createDynamicBodyCircle(position, 2.5f, mask));
            projectile.setObjectName(wrapper.name);
            projectile.setMinDamage(wrapper.damage);
            projectile.setMaxDamage(wrapper.maxDamage);
            projectile.setLifeSpan(wrapper.lifespan);
            TextureAtlas atlas = Assets.getInstance().getAssets().get("projectiles/" + wrapper.atlas);
            projectile.setTexture(atlas.findRegion(name));
            projectile.setVelocity(velocity);

            projectile.createSprite();

            return projectile;
        }
    }



}
