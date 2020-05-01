package me.extain.game.gameObject.inventory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class SlotToolTipListener extends InputListener {

    private SlotToolTip toolTip;
    private boolean isInside = false;
    private Vector2 currentCoords;
    private Vector2 offset;

    public SlotToolTipListener(SlotToolTip toolTip) {
        this.toolTip = toolTip;
        this.currentCoords = new Vector2(0,0);
        this.offset = new Vector2(20, 10);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        Slot slot = (Slot)event.getListenerActor();

        if (isInside) {
            currentCoords.set(x,y);
            slot.localToStageCoordinates(currentCoords);

            toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
        }
        return false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        Slot slot = (Slot) event.getListenerActor();
        toolTip.setVisible(slot, false);
    }

    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Slot slot = (Slot) event.getListenerActor();

        isInside = true;

        currentCoords.set(x, y);
        slot.localToStageCoordinates(currentCoords);

        toolTip.updateDescription(slot);
        toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
        toolTip.toFront();
        toolTip.setVisible(slot, true);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        Slot slot = (Slot) event.getListenerActor();
        toolTip.setVisible(slot, false);
        isInside = false;

        currentCoords.set(x, y);
        slot.localToStageCoordinates(currentCoords);
    }

}
