package me.extain.game.gameObject.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

import me.extain.game.gameObject.item.Item;

public class SlotTarget extends Target {

    Slot targetSlot;

    public SlotTarget(Slot actor) {
        super(actor);

        targetSlot = actor;
    }

    @Override
    public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void drop(Source source, Payload payload, float x, float y, int pointer) {
        Item sourceActor = (Item) payload.getDragActor();
        Item targetActor = targetSlot.getTopItem();
        Slot sourceSlot = ((SlotSource) source).getSourceSlot();

        if (sourceActor == null) {
            return;
        }

        if (!targetSlot.hasItem()) {
            targetSlot.add(sourceActor);
        } else {
            Slot.swapSlots(sourceSlot, targetSlot, sourceActor);
        }

    }

    public void reset(Source source, Payload payload) {

    }
}
