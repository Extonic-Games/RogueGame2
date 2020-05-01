package me.extain.game.gameObject.inventory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;

import me.extain.game.RogueGame;
import me.extain.game.gameObject.item.Item;

public class SlotSource extends Source {

    private Slot sourceSlot;
    private DragAndDrop dragAndDrop;

    public SlotSource(Slot actor, DragAndDrop dragAndDrop) {
        super(actor.getTopItem());
        this.sourceSlot = actor;
        this.dragAndDrop = dragAndDrop;
    }

    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        Payload payload = new Payload();

        Actor actor = getActor();
        if( actor == null ){
            return null;
        }

        Slot source = (Slot)actor.getParent();
        if( source == null ){
            return null;
        }else{
            sourceSlot = source;
        }

        sourceSlot.removeItem(true);

        payload.setDragActor(actor);
        dragAndDrop.setDragActorPosition(-x + actor.getWidth(), -y);

        return payload;
    }

    @Override
    public void dragStop (InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        if( target == null){
            sourceSlot.add(getActor());
        }
    }

    public Slot getSourceSlot() {
        return sourceSlot;
    }
}
