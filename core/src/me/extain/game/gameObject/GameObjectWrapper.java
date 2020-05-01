package me.extain.game.gameObject;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;

import me.extain.game.gameObject.Behaviors.Behaviors;

public class GameObjectWrapper {

    public String name;
    public TextureAtlas atlas;
    public float health;
    public String type;
    public String projectile;
    public ArrayList<String> behaviors;


}
