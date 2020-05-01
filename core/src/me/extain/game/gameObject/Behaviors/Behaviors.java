package me.extain.game.gameObject.Behaviors;

import me.extain.game.gameObject.GameObject;

public interface Behaviors {

    void update(float deltaTime);

    void setTarget(GameObject object);

}
