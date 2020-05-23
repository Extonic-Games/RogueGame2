package me.extain.game.gameObject.Projectile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.gameObject.GameObject;

public class Projectile extends GameObject {

    private float damageRange;
    private float maxDamage;
    private float minDamage;

    private TextureRegion texture;

    private Vector2 velocity;

    public short projectileMask = Box2DHelper.BIT_PROJECTILES;

    private float lifeSpan = 6 * 16;

    public Sprite sprite;


    public Projectile(Vector2 position, Body body) {
        super(position, body);

        this.minDamage = 2;
        this.maxDamage = 5;

        if (getBody() != null)
            this.getBody().setUserData(this);
        this.setObjectName("Projectile");

        this.damageRange = MathUtils.random(maxDamage - minDamage + 1);
    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        if (lifeSpan != 0) lifeSpan--;

        if (lifeSpan == 0) setDestroy(true);

        if (velocity != null && this.getBody() != null)  {
            this.getBody().setLinearDamping(2f);
            this.getBody().setLinearVelocity(velocity);

            this.getPosition().set(this.getBody().getPosition());
        }

        float angle = MathUtils.radiansToDegrees * MathUtils.atan2(this.getVelocity().y, this.getVelocity().x);

        if (sprite != null && this.getBody() != null) {
            sprite.setRotation(angle);
            this.getBody().setTransform(this.getPosition(), this.getBody().getAngle() * angle);
            sprite.setPosition(this.getPosition().x - 8, this.getPosition().y - 8);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
            sprite.draw(batch);
    }

    public void createSprite() {
        sprite = new Sprite(texture);
    }

    public void setDestroy(boolean destroy) {
        this.isDestroy = destroy;
    }

    public void setLifeSpan(float lifeSpan) {
        this.lifeSpan = lifeSpan * 16;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    public void setMaxDamage(float maxDamage) {
        this.maxDamage = maxDamage;
    }

    public void setMinDamage(float minDamage) {
        this.minDamage = minDamage;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public float getDamageRange() {
        return this.damageRange;
    }

    public void setDamageRange(float damage) {
        this.damageRange = damage;
    }

    public float getMaxDamage() {
        return this.maxDamage;
    }

    public float getMinDamage() {
        return this.minDamage;
    }

    public boolean getDestroy() {
        return this.isDestroy;
    }

    public TextureRegion getTexture() {
        return this.texture;
    }
}
