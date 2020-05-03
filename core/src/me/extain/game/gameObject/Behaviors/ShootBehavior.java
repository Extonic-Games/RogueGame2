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

            float dirX = (targetX - object.getPosition().x) / dirLength * 20;
            float dirY = (targetY - object.getPosition().y) / dirLength * 20;

            object.shoot(ProjectileFactory.getInstance().getProjectile(projectile, new Vector2(object.getPosition().x, object.getPosition().y), new Vector2(dirX, dirY), Box2DHelper.BIT_ENEMYPROJ));
            shootTimer = object.getShootTimer();

            System.out.println(projectile);
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
