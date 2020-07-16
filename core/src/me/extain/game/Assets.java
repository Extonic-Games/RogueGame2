package me.extain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

public class Assets {

    private AssetManager assets;
    private float progress;

    private static Assets instance = null;

    private Skin statusSkin, defaultSkin;

    private ShaderProgram shaderOutline;

    public Assets() {
        assets = new AssetManager();
        loadAssets();
        loadSkins();
        loadShaders();
        instance = this;
    }

    private void loadAssets() {
        assets.load("tiles/dungeon_tiles.atlas", TextureAtlas.class);
        assets.load("tiles/dungeon-tiles2.atlas", TextureAtlas.class);
        assets.load("entities/slime.atlas", TextureAtlas.class);
        assets.load("entities/player.atlas", TextureAtlas.class);
        assets.load("items/items.atlas", TextureAtlas.class);
        assets.load("projectiles/testProjectile.png", Texture.class);
        assets.load("projectiles/projectile1.png", Texture.class);
        assets.load("projectiles/projectiles.atlas", TextureAtlas.class);
        assets.load("skins/statusui/statusui.atlas", TextureAtlas.class);
        assets.load("bag/lootbags.atlas", TextureAtlas.class);

        progress = assets.getProgress();
    }

    public void update() {
        assets.update();
    }

    private void loadSkins() {
        assets.load("skins/statusui/statusui.atlas", TextureAtlas.class);
        assets.load("skins/default/uiskin.atlas", TextureAtlas.class);

        assets.finishLoading();

        if (assets.isLoaded("skins/statusui/statusui.atlas") && assets.isLoaded("skins/default/uiskin.atlas")) {
            statusSkin = new Skin(Gdx.files.internal("skins/statusui/statusui.json"), assets.get("skins/statusui/statusui.atlas", TextureAtlas.class));
            defaultSkin = new Skin(Gdx.files.internal("skins/default/uiskin.json"), assets.get("skins/default/uiskin.atlas", TextureAtlas.class));

            statusSkin.getFont("small-font").getData().markupEnabled = true;
        }
    }

    private void loadShaders() {
        String fragmentShader = Gdx.files.internal("shaders/outlineFrag.fs").readString();
        String vertexShader = Gdx.files.internal("shaders/outlineVertex.vs").readString();
        shaderOutline = new ShaderProgram(vertexShader, fragmentShader);

        if (!shaderOutline.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shaderOutline.getLog());
    }

    public static Assets getInstance() {
        if (instance == null) return new Assets();
        else return instance;
    }

    public void dispose() {
        assets.dispose();
        statusSkin.dispose();
        defaultSkin.dispose();
    }

    public AssetManager getAssets() {
        return assets;
    }

    public Skin getStatusSkin() {
        return statusSkin;
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    public ShaderProgram getShaderOutline() {
        return shaderOutline;
    }
}
