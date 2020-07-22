package me.extain.game.gameObject.Projectile;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SwordSlash extends Projectile {

    private float lifeSpan;

    public SwordSlash(Vector2 pos, Body body) {
        super(pos, body);

        this.getBody().setUserData(this);

        this.getBody().setTransform(this.getBody().getPosition(), 0);
    }

    public void update(float deltaTime) {

        if (lifeSpan != 0) lifeSpan --;

        if (lifeSpan == 0) this.setDestroy(true);

        float angle = MathUtils.atan2(this.getVelocity().y, this.getVelocity().x) * MathUtils.radiansToDegrees;

        this.getPosition().set(this.getBody().getPosition());

        this.getBody().setTransform(this.getPosition(), angle * MathUtils.degreesToRadians);

        sprite.setPosition(this.getBody().getPosition().x - 8, this.getBody().getPosition().y - 8);
        sprite.setRotation(this.getBody().getAngle() * MathUtils.radiansToDegrees - 45);
    }

    @Override
    public void setLifeSpan(float lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    @Override
    public void createSprite() {
        super.createSprite();

        sprite.setRotation(45);
        sprite.setPosition(this.getBody().getPosition().x - 8, this.getBody().getPosition().y - 8);
    }

    @Override
    public void render(SpriteBatch batch) {
        renderWithOutline(batch, sprite);
    }
}
