package me.extain.game.gameObject.Behaviors;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import me.extain.game.Physics.Box2DHelper;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.Projectile.Projectile;
import me.extain.game.gameObject.Projectile.ProjectileFactory;
import me.extain.game.gameObject.Projectile.TestProjectile;

public class ShootBehavior implements Behaviors {

    public GameObject object, target;
    public float shootTimer;
    private String projectile;

    public ShootBehavior(GameObject object) {
        this.object = object;
        this.shootTimer = object.getShootTimer();
    }

    @Override
    public void update(float deltaTime) {

        if (shootTimer != 0) shootTimer--;

        if (shootTimer == 0 && target != null) {
            float playerX = target.getPosition().x;
            float playerY = target.getPosition().y;

            float targetX = playerX;
            float targetY = playerY;

            float dirLength = (float) Math.sqrt((targetX - object.getPosition().x) * (targetX - object.getPosition().x) + (targetY - object.getPosition().y) * (targetY - object.getPosition().y));

            float dirX = (targetX - object.getPosition().x) / dirLength * 40;
            float dirY = (targetY - object.getPosition().y) / dirLength * 40;

            if (projectile != null) object.shoot(ProjectileFactory.getInstance().getProjectile(projectile, new Vector2(object.getPosition().x- (dirX / 3f), object.getBody().getPosition().y - (dirY / 3f)), new Vector2(dirX, dirY), Box2DHelper.createDynamicBodyCircle(object.getPosition(), 2.5f, Box2DHelper.MASK_ENEMYPROJ)));
            else  object.shoot(ProjectileFactory.getInstance().getProjectile("Test", new Vector2(object.getPosition().x, object.getBody().getPosition().y), new Vector2(dirX, dirY), Box2DHelper.createDynamicBodyCircle(object.getPosition(), 2.5f, Box2DHelper.MASK_ENEMYPROJ)));
            shootTimer = object.getShootTimer();
        }
    }

    public void setProjectile(String projectile) {
        this.projectile = projectile;
    }

    @Override
    public void setTarget(GameObject object) {
        this.target = object;
    }
}
