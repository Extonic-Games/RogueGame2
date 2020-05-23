package me.extain.game.gameObject.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import me.extain.game.Assets;
import me.extain.game.Physics.Box2DHelper;
import me.extain.game.RogueGame;
import me.extain.game.gameObject.GameObject;
import me.extain.game.map.tiled.TileMap;
import me.extain.game.screens.GameScreen;

public class RemotePlayer extends GameObject {

    private TextureAtlas atlas;
    private TextureRegion walk2;

    public RemotePlayer(Vector2 position) {
        super(position, Box2DHelper.createDynamicBodyCircle(position, 4f, Box2DHelper.BIT_PLAYER));

        this.setObjectName("RemotePlayer");

        if (this.getBody() != null)
            this.getBody().setUserData(this);

        if (Assets.getInstance().getAssets().isLoaded("entities/player.atlas")) {
            atlas = Assets.getInstance().getAssets().get("entities/player.atlas");

            walk2 = atlas.findRegion("player-idle");
        }
    }

    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        //super.render(batch);
        if (walk2 != null)
            batch.draw(walk2, this.getBody().getPosition().x - 8, this.getBody().getPosition().y - 5);
    }
}
