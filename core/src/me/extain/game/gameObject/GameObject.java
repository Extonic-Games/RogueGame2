package me.extain.game.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.IntMap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import me.extain.game.Assets;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.Behaviors.Behaviors;
import me.extain.game.gameObject.Player.Player;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.map.tiled.TileMap;
import me.extain.game.screens.GameScreen;

public class GameObject {

    private Screen context;
    private TileMap map;
    private Vector2 position;
    private Body body;

    private int id;

    private float speed;

    private String objectName;

    private TextureAtlas atlas;
    private float health, maxHealth;

    public float shootTimer = 40;

    public boolean isDestroy = false;

    private Animation<TextureRegion> walk = null;
    private TextureRegion idle = null;
    protected TextureRegion currentTexture = null;

    private Body eyesBody;

    private float size;

    private ArrayList<Behaviors> behaviors = new ArrayList<Behaviors>();

    public static IntMap<GameObject> idMap;

    private boolean isBlink = false;

    private int blinkTimer = 20;

    private float stateTime;

    private boolean isMoving, hasMoved;
    private Vector2 oldPos = new Vector2();

    private float alpha = 0.0f;

    private boolean hasEyes = false;

    private boolean isFlip = false;

    private ParticleEffect effect;

    public GameObject(Vector2 position, Body body) {
        this.position = position;
        this.body = body;
        this.speed = 10;
        this.maxHealth = 30;
        this.health = maxHealth;
        idMap = new IntMap<>();
        this.id = MathUtils.random(1000);
        idMap.put(id, this);
        //this.objectName = "GameObject";

        if (idle != null) {
            this.currentTexture = idle;
        }


        if (body != null)
            this.body.setUserData(this);

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/particle-effect.particle"), Gdx.files.internal("particles"));
        effect.scaleEffect(0.2f);
        effect.start();
    }

    public GameObject(GameObjectWrapper wrapper, Vector2 positon, Body body) {
        this.objectName = wrapper.name;
        this.position = positon;
        this.body = body;
        this.body.setUserData(this);
        this.speed = 10f;
        this.maxHealth = wrapper.health;
        this.health = wrapper.health;
        this.size = wrapper.size;
        TextureAtlas atlas = Assets.getInstance().getAssets().get("entities/" + wrapper.atlas);
        this.atlas = atlas;
        this.setWalk(new Animation<TextureRegion>(0.4f, atlas.findRegions(this.objectName + "_walk"), Animation.PlayMode.LOOP));
        this.setIdle(atlas.findRegion(this.objectName + "_idle"));
    }

    public void update(float deltaTime) {

        alpha += deltaTime;

        if (effect != null && effect.isComplete()) {
            effect.reset();
            effect.scaleEffect(0.2f);
        }

        stateTime += deltaTime;

        if (getBody() != null)
            this.getBody().setLinearDamping(5f);

        if (isMoving && walk != null) {
            currentTexture = walk.getKeyFrame(stateTime);
        } else {
            currentTexture = idle;
        }

        if (getBody() != null)
            if (this.getBody().getLinearVelocity().x == 0 && this.getBody().getLinearVelocity().y == 0) {
                isMoving = false;
            }

        if (getBody() != null)
            this.getPosition().set(this.getBody().getPosition());
        if (eyesBody != null) eyesBody.setTransform(this.getPosition(), 0f);

        if (oldPos.x != this.getPosition().x || oldPos.y != this.getPosition().y) {
            oldPos.set(this.getPosition());
            hasMoved = true;
            isMoving = true;
        }
    }

    public void updateServer(float deltaTime) {

        this.getBody().setLinearDamping(5f);

        this.getPosition().set(this.getBody().getPosition());
        if (eyesBody != null) eyesBody.setTransform(this.getPosition(), 0f);

        if (behaviors.size() != 0)
            updateBehaviors(deltaTime);


        if (oldPos.x != this.getPosition().x || oldPos.y != this.getPosition().y) {
            oldPos.set(this.getPosition());
            hasMoved = true;
        }
    }

    public void render(SpriteBatch batch) {

        if (currentTexture != null && walk != null && idle != null) {

            if (blinkTimer == 0) {
                blinkTimer = 20;
                isBlink = false;
            }

            if (isBlink && blinkTimer != 0) {
                batch.setColor((float) Math.abs(Math.sin(alpha)), 0, 0, 1);
                batch.draw(currentTexture, this.getPosition().x - size / 2, this.getPosition().y - size / 3, size, size);
                batch.setColor(Color.WHITE);

                if (effect != null) {
                    effect.setPosition(this.getPosition().x, this.getPosition().y);
                    effect.draw(batch);
                }
                blinkTimer--;
            } else {
                batch.draw(currentTexture, this.getPosition().x - size / 2, this.getPosition().y - size / 3, size, size);
            }
        }
    }

    public void updateBehaviors(float deltaTime) {
        for (Behaviors behavior : behaviors) {
            behavior.update(deltaTime);
        }
    }

    public void setBehaviorTarget(GameObject object) {
        for (Behaviors behavior : behaviors) {
            behavior.setTarget(object);
        }
    }

    public void setWalk(Animation<TextureRegion> region) {
        this.walk = region;
    }

    public void setIdle(TextureRegion region) {
        this.idle = region;
    }

    public void onHit(GameObject object, float damage) {
        if (!(object instanceof Projectile)) {
            //object.takeDamage(damage);

            isBlink = true;
            if (effect != null)
                effect.start();
        }
    }

    public void createEyes() {
        eyesBody = Box2DHelper.createSensorCircle(this.position, 80f, Box2DHelper.BIT_ENEMY_SENSOR);
        eyesBody.setUserData(this);
        hasEyes = true;
    }

    public void move(Vector2 velocity) {
        this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x + velocity.x, this.getBody().getLinearVelocity().y + velocity.y);
        isMoving = true;
    }

    public GameObject getByID(int id) {
        GameObject object = idMap.get(id);

        if (object == null) System.out.println("No gameobject with that id exists");

        return object;
    }

    public void addBehavior(Behaviors behavior) {
        this.behaviors.add(behavior);
    }

    public void removeBehavior(Behaviors behavior) { this.behaviors.remove(behavior); }

    public ArrayList<Behaviors> getBehaviors() {
        return behaviors;
    }

    public boolean isDestroy() {
        return this.isDestroy;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() { return maxHealth; }

    public void takeDamage(float damage) {
        health -= damage;
    }

    public GameScreen getContext() {
        return (GameScreen) context;
    }

    public TileMap getMap() {
        return map;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 pos) {
        this.position.set(pos);
    }

    public void setPosition(float x, float y) {
        this.setPosition(new Vector2(x,  y));
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void setMaxHealth(float maxHealth) { this.maxHealth = maxHealth; }

    public void setAtlas(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getName() {
        return objectName;
    }

    public float getShootTimer() {
        return shootTimer;
    }

    public void setShootSpeed(float shootSpeed) {
        this.shootTimer = shootSpeed;
    }

    public Body getEyesBody() {
        return eyesBody;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public boolean isHasEyes() {
        return hasEyes;
    }

    public boolean isFlip() {
        return isFlip;
    }

    public void setFlip(boolean flip) {
        this.isFlip = flip;
    }

    public void setDestroy(boolean destroy) {
        this.isDestroy = destroy;
    }

}
