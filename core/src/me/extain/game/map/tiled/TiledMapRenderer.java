package me.extain.game.map.tiled;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.HashMap;

import me.extain.game.gameObject.GameObject;

public class TiledMapRenderer extends OrthogonalTiledMapRenderer {

    private int drawObjectsAfter = 3;
    private TileMap map;
    private SpriteBatch batch;

    public TiledMapRenderer(TileMap map, SpriteBatch batch) {
        super(map.getMap(), batch);

        this.map = map;
        this.batch = batch;
    }

    @Override
    public void render() {
        beginRender();
        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer)
                    renderTileLayer((TiledMapTileLayer) layer);
                    currentLayer++;
                if (currentLayer == drawObjectsAfter) {
                    for (GameObject object : map.getGameObjectManager().getGameObjects()) object.render(batch);
                }
            }
        }
        endRender();
    }
}
