package me.extain.game.gameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.IntMap;

import java.lang.reflect.Array;
import java.util.ArrayList;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.Behaviors.Behaviors;
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
    private TextureRegion currentTexture = null;

    private Body eyesBody;

    private float size;

    public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private ArrayList<Projectile> removeProjectiles = new ArrayList<Projectile>();

    private ArrayList<Behaviors> behaviors = new ArrayList<Behaviors>();

    public static IntMap<GameObject> idMap;

    private boolean isBlink = false;

    private int blinkTimer = 20;

    private float stateTime;

    private boolean isMoving, hasMoved;
    private Vector2 oldPos = new Vector2();

    private float alpha = 0.0f;

    private boolean hasEyes = false;

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
    }

    public void update(float deltaTime) {

        alpha += deltaTime;

        stateTime += deltaTime;



        this.getBody().setLinearDamping(5f);

        if (isMoving) {
            currentTexture = walk.getKeyFrame(stateTime);
        } else {
            currentTexture = idle;
        }

        if (this.getBody().getLinearVelocity().x == 0 && this.getBody().getLinearVelocity().y == 0) {
            isMoving = false;
        }

        updateBehaviors(deltaTime);


        for (Projectile projectile : removeProjectiles) {
            Box2DHelper.setBodyToDestroy(projectile.getBody());
            projectiles.remove(projectile);
        }

        removeProjectiles.clear();

        for (Projectile projectile : projectiles) {

            if (projectile.getDestroy()) {
                removeProjectiles.add(projectile);
            }
            else {
                projectile.update(deltaTime);
            }
        }

        this.getPosition().set(this.getBody().getPosition());
        if (eyesBody != null) eyesBody.setTransform(this.getPosition(), 0f);

        if (oldPos.x != this.getPosition().x || oldPos.y != this.getPosition().y) {
            oldPos.set(this.getPosition());
            hasMoved = true;
        }
    }

    public void render(SpriteBatch batch) {
        for (Projectile projectile : projectiles) {
            projectile.render(batch);
        }

        if (walk != null) {

            if (blinkTimer == 0) {
                blinkTimer = 20;
                isBlink = false;
            }

            if (isBlink && blinkTimer != 0) {
                batch.setColor((float) Math.abs(Math.sin(alpha)), 0, 0, 1);
                batch.draw(currentTexture, this.getPosition().x - size / 2, this.getPosition().y - size / 3, size, size);
                batch.setColor(Color.WHITE);
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

    public void clearProjectiles() {
        for (Projectile projectile : projectiles) {
            Box2DHelper.setBodyToDestroy(projectile.getBody());
        }

        projectiles.clear();
    }

    public void onHit(GameObject object, float damage) {
        if (!(object instanceof Projectile)) {
            object.takeDamage(damage);

            System.out.println(object.getName() + ": health: " + object.getHealth());

            isBlink = true;

            if (health <= 0) {
                this.isDestroy = true;
            }
        }
    }

    public void shoot(Projectile projectile) {
        projectiles.add(projectile);
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

}
