package me.extain.game.gameObject.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import me.extain.game.Assets;
import me.extain.game.network.Packets.MovePacket;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.gameObject.Projectile.TestProjectile;
import me.extain.game.map.tiled.TileMap;
import me.extain.game.network.Packets.ShootPacket;
import me.extain.game.screens.GameScreen;

public class Player extends GameObject {

    private TextureAtlas atlas;

    private TextureRegion walk = null;

    private float shootTimer = 20;

    private Vector2 oldPos;

    public Player(Vector2 position) {
        super(position, Box2DHelper.createDynamicBodyCircle(position, 4f, Box2DHelper.BIT_PLAYER));

        this.setObjectName("Player");

        this.getBody().setUserData(this);

        this.setSpeed(30);

        oldPos = new Vector2(0,0);

        if (Assets.getInstance().getAssets().isLoaded("entities/player.atlas")) {
            atlas = Assets.getInstance().getAssets().get("entities/player.atlas");

            System.out.println(atlas.toString());

            walk = atlas.findRegion("player-idle");
        }

    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        this.getBody().setLinearDamping(5);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x, getSpeed());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.getBody().setLinearVelocity(-getSpeed(), this.getBody().getLinearVelocity().y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x, -getSpeed());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.getBody().setLinearVelocity(getSpeed(), this.getBody().getLinearVelocity().y);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            ((GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen()).getPlayerHUD().displayInventory();
        }

        if (shootTimer != 0) shootTimer--;

       float mouseX = Gdx.input.getX();
       float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
       RogueGame.getInstance().getUICamera().unproject(new Vector3(mouseX, mouseY, 0));

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shootTimer == 0 &&  !((GameScreen) RogueGame.getInstance().getScreenManager().getCurrentScreen()).getPlayerHUD().getInventoryUI().isInInventory(mouseX, mouseY)) {

            float targetX = this.getPosition().x - (Gdx.input.getX() - Gdx.graphics.getWidth() / 2);
            float targetY = this.getPosition().y + (Gdx.input.getY() - Gdx.graphics.getHeight() / 2);

            float dirLength = (float) Math.sqrt((targetX - this.getPosition().x) * (targetX - this.getPosition().x) + (targetY - this.getPosition().y) * (targetY - this.getPosition().y));

            float dirX = (targetX - this.getPosition().x) / dirLength * getSpeed();
            float dirY = (targetY - this.getPosition().y) / dirLength * getSpeed();

            float angle = MathUtils.radiansToDegrees * MathUtils.atan2(dirX - this.getPosition().x, dirY - this.getPosition().y);

            //projectiles.add(new TestProjectile(new Vector2(this.getPosition().x - (dirX / 3f), this.getBody().getPosition().y - (dirY / 3f)), new Vector2(-dirX, -dirY), Box2DHelper.BIT_PROJECTILES));

            ShootPacket packet = new ShootPacket();
            packet.name = "test";
            packet.x = this.getPosition().x - (dirX / 3);
            packet.y = this.getPosition().y - (dirY / 3);
            packet.velX = -dirX;
            packet.velY = -dirY;
            RogueGame.getInstance().getClient().sendUDP(packet);

            shootTimer = 20;

        }

        this.getPosition().set(this.getBody().getPosition());
        if (oldPos.x != this.getPosition().x || oldPos.y != this.getPosition().y) {
            MovePacket movePacket = new MovePacket();
            oldPos.set(this.getPosition());
            movePacket.x = oldPos.x;
            movePacket.y = oldPos.y;
            RogueGame.getInstance().getClient().sendUDP(movePacket);
        }



    }



    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        if (walk != null)
            batch.draw(walk, this.getPosition().x - 8, this.getPosition().y - 5);
    }

    public void setPosition(float x, float y) {
        this.getPosition().set(x, y);
    }
}
